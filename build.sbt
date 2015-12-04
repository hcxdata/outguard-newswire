name := "outguard-newswire"
 
version := "1.0"
 
scalaVersion := "2.10.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
 
resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
                  "Maven Repository" at "http://repo1.maven.org/maven2/",
                  "maven-restlet" at "http://maven.restlet.org",
                  "Coda Hale's Repository" at "http://repo.codahale.com/")
 
libraryDependencies ++=
{
     val spark_version = "1.5.1"
     val httpclient_version = "4.4.1"
     val xstream_version = "1.4.8"
     val mmseg_solr_version = "2.3.0"
     val solr_version = "5.0.0"
     val hbase_version="0.98.16.1-hadoop2"
     Seq(
         "org.apache.spark" %% "spark-core" % spark_version% "provided",
         //"org.apache.spark" %% "spark-sql" % spark_version% "provided",
         //"org.apache.spark" %% "spark-streaming" % spark_version% "provided",
         "org.apache.spark" %% "spark-mllib" % spark_version% "provided",
         //"org.apache.spark" %% "spark-hive" % spark_version% "provided",
         //"org.apache.spark" %% "spark-yarn" % spark_version% "provided",
         //"org.apache.spark" %% "spark-repl" % spark_version% "provided",
         "org.apache.httpcomponents" % "httpclient" % httpclient_version,
         "org.apache.httpcomponents" % "httpcore" % httpclient_version,
         //"com.thoughtworks.xstream" % "xstream" % xstream_version,
         //"com.chenlb.mmseg4j" % "mmseg4j-solr" % mmseg_solr_version,
         //"org.apache.solr" % "solr-core" % solr_version,
         //"org.apache.lucene" % "lucene-core" % solr_version,
         //"commons-codec" % "commons-codec" % "1.10",
         "mysql" % "mysql-connector-java" % "5.1.27",
         "org.apache.hbase" % "hbase-common" % hbase_version,
         "org.apache.hbase" % "hbase-client" % hbase_version,
         "org.apache.hbase" % "hbase-server" % hbase_version
         )
}

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "unwanted.txt"                                => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}