name := "swords"
version := "0.1"
scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.softwaremill.quicklens" %% "quicklens" % "1.4.11",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.scalafx" %% "scalafx" % "8.0.181-R13"
)

fork := true
