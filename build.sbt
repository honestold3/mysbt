name := "mysbt"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.2.0" % "test",
  "com.google.protobuf" % "protobuf-java" % "2.4.1",
  "org.apache.spark" % "spark-assembly_2.10" % "1.0.0"
  //"junit" % "junit" % "4.7 "
  )

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

resolvers += Resolver.url("cloudera", url("https://repository.cloudera.com/artifactory/cloudera-repos/."))

resolvers += Resolver.url("MavenOfficial", url("http://repo1.maven.org/maven2"))

resolvers += Resolver.url("springside", url("http://springside.googlecode.com/svn/repository"))

resolvers += Resolver.url("jboss", url("http://repository.jboss.org/nexus/content/groups/public-jboss"))
