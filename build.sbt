scalaVersion := "3.5.0"

run / javaOptions ++= Seq(
  "-Xmx200m",
)

testFrameworks += new TestFramework("munit.Framework")

libraryDependencies += "com.github.zainab-ali" %% "aquascape" % "0.3.0" 

libraryDependencies ++= Seq(
  "org.typelevel" %% "munit-cats-effect" % "2.0.0" % Test,
  "org.typelevel" %% "cats-effect-testkit" % "3.5.4" % Test,
  "com.github.zainab-ali" %% "aquascape" % "0.3.0" % Test
)

