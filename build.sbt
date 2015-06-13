import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._

assemblySettings

name := "mysbt"

version := "1.0"

scalaVersion := "2.10.4"


libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.2.0" % "test" ,
  //"org.apache.hive" % "hive-jdbc" % "0.12.0",
  //"org.apache.kafka" % "kafka_2.10" % "0.8.1.1" ,
  //"com.google.protobuf" % "protobuf-java" % "2.4.1",
  "junit" % "junit" % "4.11" % "provided",
  "org.apache.hadoop" % "hadoop-client" % "2.3.0-mr1-cdh5.0.0" ,
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.0.2",
  //"org.pentaho" % "pentaho-aggdesigner-algorithm" % "5.1.5-jhyde",
  //("org.apache.spark" % "spark-assembly_2.10" % "1.1.0")
  "org.apache.spark" % "spark-core_2.10" % "1.3.1",
  "org.apache.spark" % "spark-hive_2.10" % "1.3.1",
  "org.apache.spark" % "spark-sql_2.10" % "1.3.1",
  "org.apache.spark" % "spark-streaming_2.10" % "1.3.1",
  "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.3.1"
  //"junit" % "junit" % "4.7 " % "provided"
  )

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

resolvers += Resolver.url("cloudera", url("https://repository.cloudera.com/artifactory/cloudera-repos/."))

resolvers += Resolver.url("MavenOfficial", url("http://repo1.maven.org/maven2"))

resolvers += Resolver.url("conjars", url("http://conjars.org/repo"))

resolvers += Resolver.url("jboss", url("http://repository.jboss.org/nexus/content/groups/public-jboss"))

resolvers += Resolver.mavenLocal

//externalResolvers <<= resolvers map
//  { rs =>   Resolver.withDefaultResolvers(rs, mavenCentral = true) }

//mainClass in (Compile, packageBin) :=Some("org.wq.mysbt.streaming.MessageSender")

//assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = true, includeDependency = false)

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("com", "esotericsoftware", "minlog", xs @ _*) => MergeStrategy.first
    case PathList("org", "jboss","netty", xs @ _*) => MergeStrategy.last
    case PathList("com", "google","common", xs @ _*) => MergeStrategy.last
    case PathList("com", "codahale", xs @ _*)=> MergeStrategy.first
    case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
    case PathList("org", "apache", xs @ _*)=> MergeStrategy.first
    case PathList("org", "eclipse", xs @ _*) => MergeStrategy.first
    case PathList("akka",  xs @ _*)=> MergeStrategy.first
    case PathList("parquet",  xs @ _*)=> MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith "jboss-beans.xml" => MergeStrategy.filterDistinctLines
    case PathList(ps @ _*) if ps.last endsWith "pom.properties" => MergeStrategy.filterDistinctLines
    case PathList(ps @ _*) if ps.last endsWith "pom.xml" => MergeStrategy.filterDistinctLines
    case PathList(ps @ _*) if ps.last endsWith "overview.html" => MergeStrategy.filterDistinctLines
    case PathList(ps @ _*) if ps.last endsWith "plugin.xml" => MergeStrategy.filterDistinctLines
    case PathList(ps @ _*) if ps.last endsWith "parquet.thrift" => MergeStrategy.filterDistinctLines
    case PathList(ps @ _*) if ps.last endsWith "Log$1.class" => MergeStrategy.filterDistinctLines
    case PathList(ps @ _*) if ps.last endsWith "Log.class" => MergeStrategy.filterDistinctLines
    case "application.conf" => MergeStrategy.concat
    case "unwanted.txt"     => MergeStrategy.discard
    case x => old(x)
  }
}

mainClass in assembly := Some("org.wq.mysbt.streaming.MessageSender")

jarName in assembly := "wqrun.jar"