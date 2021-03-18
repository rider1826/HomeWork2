
name := "HomeWork2"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "3.3.0"
libraryDependencies += "org.apache.hadoop" % "hadoop-hdfs" % "3.3.0"
libraryDependencies += "org.apache.logging.log4j" % "log4j" % "2.14.1"


assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
