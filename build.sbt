scalaVersion := "2.13.4"

val zioVersion = "1.0.3"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio",
  "dev.zio" %% "zio-macros",
).map(_ % zioVersion)

libraryDependencies ++= Seq(
  "dev.zio" %% "zio-test",
  "dev.zio" %% "zio-test-sbt",
).map(_ % zioVersion % Test)

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

scalacOptions += "-Ymacro-annotations"