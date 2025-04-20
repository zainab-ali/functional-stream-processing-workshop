package cheatsheet

import cats.syntax.all.*
import scala.quoted.*

object MethodCallMacro {
  private case class Method(
      name: String,
      types: List[String],
      params: List[List[String]]
  )
  private case class MethodCall(on: String, method: Method)

  inline def extractMethodCalls: List[String] = ${ extractMethodCallsImpl }

  def extractMethodCallsImpl(using Quotes): Expr[List[String]] = {
    (new Macro).value
  }

  private final class Macro(using Quotes) {
    import quotes.reflect.*

    def value: Expr[List[String]] = {
      val fs2StreamType = TypeRepr.of[fs2.Stream]
      val fs2StreamClass = fs2StreamType.classSymbol.get
      val declaredMethodsOnStream = fs2StreamClass.declaredMethods
      val declaredMethodsOnOps = fs2StreamClass.companionModule.declaredTypes
        .filter(isStreamOpsClass)
        .flatMap(_.declaredMethods)
      val methodCallsOnStream =
        (declaredMethodsOnStream ++ declaredMethodsOnOps)
          .mapFilter(methodIfRelevant)
          .map(MethodCall("s", _))
      val methodsOnCompanion =
        fs2StreamClass.companionModule.declaredMethods
          .mapFilter(methodIfRelevant)
      val valuesOnCompanion =
        fs2StreamClass.companionModule.declaredFields
          .mapFilter(valueIfRelevant)

      val methodCallsOnCompanion = (valuesOnCompanion ++ methodsOnCompanion)
        .map(MethodCall("Stream", _))

      val compileOpsType = TypeRepr.of[fs2.Stream.CompileOps]
      val methodCallsOnCompileOps =
        compileOpsType.classSymbol.get.declaredMethods
          .mapFilter(methodIfRelevant)
          .map(MethodCall("s.compile", _))
      val methodCalls =
        methodCallsOnStream ++ methodCallsOnCompanion ++ methodCallsOnCompileOps

      Expr(methodCalls.map(mkString))
    }

    /** Get the name of a parameter.
      *
      * In `def take(n: Int)`, the parameter is `n: Int` and its name is `n`
      */
    private def parameterName(param: Tree): String = {
      param match {
        case ValDef(name, _, _) => name
        case _ =>
          val structure = param.show(using Printer.TreeStructure)
          sys.error(s"Unknown parameter structure: ${structure}")
      }
    }

    private def typeName(tpe: Tree): String = {
      tpe match {
        case TypeDef(name, _) => name
        case _ =>
          val structure = tpe.show(using Printer.TreeStructure)
          sys.error(s"Unknown type parameter structure: ${structure}")
      }
    }

    /** Determine whether a method should be omitted from the cheat sheet. */
    private def shouldIgnoreMethod(method: Symbol): Boolean = {
      // TODO: Zainab - Inspect the method annotations for deprecated methods
      // Filter out synthesized methods and private methods
      method.name.contains("$default$") || method.flags.is(
        Flags.Private
      ) || method.flags.is(Flags.Implicit) || method.flags.is(Flags.Override)
    }

    /** Determine whether a parameter should be omitted from the cheat sheet. */
    private def shouldIgnoreParameter(param: Symbol): Boolean = {
      val isEvidence = param.flags.is(Flags.Implicit)
      val isDefault = param.flags.is(Flags.HasDefault)
      isEvidence || isDefault
    }

    private def partitionTypesAndParameters(
        typesAndParams: List[List[Symbol]]
    ): (List[Symbol], List[List[Symbol]]) = {
      typesAndParams match {
        case typesOrParams :: params =>
          val isTypeBlock =
            typesOrParams.map(_.tree).forall {
              case TypeDef(_, _) => true
              case _             => false
            }
          val isParamBlock =
            typesOrParams.map(_.tree).forall {
              case ValDef(_, _, _) => true
              case _               => false
            }
          if (isTypeBlock) {
            (typesOrParams, params)
          } else if (isParamBlock) {
            (Nil, typesOrParams :: params)
          } else {
            val structures = typesOrParams
              .map(_.tree.show(using Printer.TreeStructure))
              .mkString("\n")
            sys.error(s"Unknown parameter structures: ${structures}")
          }
        case Nil => (Nil, Nil)
      }
    }

    private def mkString(methodCall: MethodCall): String = {
      val typeSuffix = methodCall.method.types.mkString("[", ",", "]")
      val paramSuffix =
        methodCall.method.params.map(_.mkString("(", ", ", ")")).mkString
      // Only display type parameters if there are no parameters present.
      val suffix =
        if (
          methodCall.method.params.isEmpty && methodCall.method.types.nonEmpty
        ) typeSuffix
        else paramSuffix
      s"${methodCall.on}.${methodCall.method.name}$suffix"
    }

    private def methodIfRelevant(method: Symbol): Option[Method] = {
      if (shouldIgnoreMethod(method)) None
      else {
        val (types, paramLists) = partitionTypesAndParameters(
          method.paramSymss
        )
        val typeNames =
          if (shouldIncludeTypes(method))
            types.map(tpe => typeName(tpe.tree))
          else Nil
        val paramNames = paramLists
          .map { params =>
            params
              .filterNot { param => shouldIgnoreParameter(param) }
              .map { param => parameterName(param.tree) }
          }
          .filter(_.nonEmpty)
        Some(Method(method.name, typeNames, paramNames))
      }
    }

    private def shouldIncludeTypes(method: Symbol): Boolean = {
      method.name.contains("covary") || method.name === "apply"
    }

    private def isStreamOpsClass(cls: Symbol): Boolean = {
      val fs2StreamType = TypeRepr.of[fs2.Stream]
      val fs2StreamClass = fs2StreamType.classSymbol.get

      val convertsStream = cls.declaredFields.exists(maybeStream =>
        maybeStream.typeRef.baseClasses.headOption.exists(_ == fs2StreamClass)
      )

      cls.name.contains("Ops") && cls.flags.is(Flags.Implicit) && convertsStream
    }

    private def valueIfRelevant(field: Symbol): Option[Method] = {
      if (isStreamValue(field)) fieldName(field).map(Method(_, Nil, Nil))
      else None
    }
    private def isStreamValue(field: Symbol): Boolean = {
      val fs2StreamType = TypeRepr.of[fs2.Stream]
      val fs2StreamClass = fs2StreamType.classSymbol.get
      field.typeRef.baseClasses.headOption.exists(_ == fs2StreamClass)
    }

    private def fieldName(field: Symbol): Option[String] = {
      field.tree match {
        case ValDef(name, _, _) => Some(name)
        case _                  => None
      }
    }
  }
}
