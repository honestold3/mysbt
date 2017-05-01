import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._


assemblySettings

name := "mysbt"

version := "1.0"

scalaVersion := "2.10.5"



libraryDependencies ++= Seq(
  "org.elasticsearch" % "elasticsearch" % "1.7.2",
  "com.databricks" % "spark-avro_2.10" % "2.0.1",
  "org.scala-lang" % "scala-reflect" % "2.10.4",
  "org.scala-lang" % "scala-library" % "2.10.4",
  "org.bytedeco" % "javacpp" % "1.0",
  "org.bytedeco" % "javacv" % "1.0",
  "org.bytedeco.javacpp-presets" % "opencv" % "3.0.0-1.0",
  "org.bytedeco.javacpp-presets" % "opencv" % "3.0.0-1.0" classifier "macosx-x86_64",
  "com.drewnoakes" % "metadata-extractor" % "2.8.1",
  "com.twelvemonkeys.imageio" % "imageio-core" % "3.1.1",
  "com.twelvemonkeys.imageio" % "imageio-jpeg" % "3.1.1",
  "com.twelvemonkeys.imageio" % "imageio-pnm" % "3.1.1",
  "com.twelvemonkeys.imageio" % "imageio-tiff" % "3.1.1",
  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
  "org" % "hipi" % "2.1.0",
  "org.apache.hbase" % "hbase-client" % "1.2.3",
  "org.apache.hbase" % "hbase-common" % "1.2.3",
  "org.apache.hbase" % "hbase-server" % "1.2.3" excludeAll ExclusionRule(organization = "org.mortbay.jetty"),
  "org.scalatest" % "scalatest_2.10" % "2.2.0" % "test" excludeAll (
    ExclusionRule(organization = "org.scala-lang"),
    ExclusionRule("jline", "jline")
    ),
  //"org.apache.hive" % "hive-jdbc" % "0.12.0",
  //"org.apache.kafka" % "kafka_2.10" % "0.8.1.1" ,
  //"com.google.protobuf" % "protobuf-java" % "2.4.1",
  //"junit" % "junit" % "4.11" % "provided",
  //"org.apache.hadoop" % "hadoop-yarn-api" % "2.6.0-cdh5.4.0",
  //"org.apache.hadoop" % "hadoop-mapreduce-client-core" % "2.6.0-cdh5.4.0",
  //"org.apache.hadoop" % "hadoop-mapreduce-client-jobclient" % "2.6.0-cdh5.4.0",
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.0.2" excludeAll (
    ExclusionRule(organization = "org.scala-lang"),
    ExclusionRule("jline", "jline")
    ),
  //"org.pentaho" % "pentaho-aggdesigner-algorithm" % "5.1.5-jhyde",
  //("org.apache.spark" % "spark-assembly_2.10" % "1.1.0")
  //"org.apache.spark" % "spark-core_2.10" % "1.5.2",
  "org.apache.spark" % "spark-parent_2.10" % "1.6.1" excludeAll (
    ExclusionRule(organization = "org.scala-lang"),
    ExclusionRule("jline", "jline")
    ),
  "org.apache.spark" % "spark-sql_2.10" % "1.6.1" excludeAll (
    ExclusionRule(organization = "org.scala-lang"),
    ExclusionRule("jline", "jline")
    ),
  "org.apache.spark" % "spark-streaming_2.10" % "1.6.1" excludeAll (
    ExclusionRule(organization = "org.scala-lang"),
    ExclusionRule("jline", "jline")
    ),
  "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.6.1" excludeAll (
    ExclusionRule(organization = "org.scala-lang"),
    ExclusionRule("jline", "jline")
    ),
  "org.apache.spark" % "spark-graphx_2.10" % "1.6.1" excludeAll (
    ExclusionRule(organization = "org.scala-lang"),
    ExclusionRule("jline", "jline")
    ),
  //"com.oracle" % "ojdbc14" % "10.2.0.4.0",
  //"com.oracle" % "ojdbc6" % "11.2.0.4",
  "org.elasticsearch" % "elasticsearch-spark_2.10" % "2.3.2" excludeAll (
    ExclusionRule(organization = "org.scala-lang"),
    ExclusionRule("jline", "jline")
    ),
  "com.twitter" % "scrooge-core_2.10" % "4.2.0" excludeAll (
    ExclusionRule(organization = "org.scala-lang"),
    ExclusionRule("jline", "jline")
    ),
  "io.searchbox" % "jest" % "1.0.3" excludeAll (
    ExclusionRule(organization = "org.scala-lang"),
    ExclusionRule("jline", "jline")
    ),
  "com.sksamuel.elastic4s" % "elastic4s-core_2.10" % "1.7.6" excludeAll (
    ExclusionRule(organization = "org.scala-lang"),
    ExclusionRule("jline", "jline")
    ),
  "com.sksamuel.elastic4s" % "elastic4s-json4s_2.10" % "1.7.6" excludeAll (
    ExclusionRule(organization = "org.scala-lang"),
    ExclusionRule("jline", "jline")
    ),
  "com.sksamuel.elastic4s" % "elastic4s-jackson_2.10" % "1.7.6" excludeAll (
    ExclusionRule(organization = "org.scala-lang"),
    ExclusionRule("jline", "jline")
    ),
  "com.sksamuel.elastic4s" % "elastic4s-streams_2.10" % "1.7.6" excludeAll (
    ExclusionRule(organization = "org.scala-lang")
    )
  //"org.apache.spark" % "spark-streaming-kafka_2.10" % "1.5.2"
  //"junit" % "junit" % "4.11 " % "provided"
)

//ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

resolvers += "Local Maven Repository" at "file:///Users/wq/.m2/repository"

resolvers += Resolver.mavenLocal

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
resolvers += Resolver.url("cloudera", url("https://repository.cloudera.com/artifactory/cloudera-repos/"))

resolvers += Resolver.url("MavenOfficial", url("http://repo1.maven.org/maven2"))

resolvers += Resolver.url("conjars", url("http://conjars.org/repo"))

//resolvers += Resolver.url("conjars", url("http://repo1.maven.org/maven2"))

//resolvers += Resolver.url("jboss", url("http://repository.jboss.org/nexus/content/groups/public-jboss"))



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
    case _ => MergeStrategy.deduplicate
  }
}

mainClass in assembly := Some("org.wq.mysbt.streaming.MessageSender")

jarName in assembly := "wqrun.jar"