scalaVersion := "3.7.0"

run / javaOptions ++= Seq(
  "-Xmx200m",
)

testFrameworks += new TestFramework("munit.Framework")

libraryDependencies ++= Seq(
  "com.github.zainab-ali" %% "aquascape" % "0.4.0" ,
  "co.fs2" %% "fs2-core" % "3.12.0" ,
  "co.fs2" %% "fs2-io" % "3.12.0" ,
  "com.lihaoyi" %% "scalatags" % "0.13.1" 
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "munit-cats-effect" % "2.1.0" % Test,
  "org.typelevel" %% "cats-effect-testkit" % "3.6.3" % Test,
  "com.github.zainab-ali" %% "aquascape" % "0.4.0" % Test,
  "co.fs2" %% "fs2-core" % "3.12.0" % Test,
  "co.fs2" %% "fs2-io" % "3.12.0" % Test,
  "com.lihaoyi" %% "scalatags" % "0.13.1" % Test
)

