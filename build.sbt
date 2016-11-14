name := "SparkBase"

version := "1.0"

scalaVersion := "2.10.4"

val sparkVersion = "2.0.0"

resolvers ++= Seq(
  "apache-snapshots" at "http://repository.apache.org/snapshots/"
)

libraryDependencies += "com.amazonaws" % "aws-java-sdk-s3" % "1.10.29"

libraryDependencies += "com.amazon.redshift" % "jdbc4" % "1.1.7.1007" from "https://s3.amazonaws.com/redshift-downloads/drivers/RedshiftJDBC4-1.1.7.1007.jar"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-hive" % sparkVersion
)

libraryDependencies += "com.databricks" %% "spark-redshift" % "0.6.0"

// For postgres related dependencies in order to be able to use database models
libraryDependencies += "com.github.tminglei" %% "slick-pg" % "0.11.2"

libraryDependencies += "com.github.tminglei" %% "slick-pg_joda-time" % "0.11.2"

libraryDependencies += "com.github.tminglei" %% "slick-pg_json4s" % "0.11.2"

libraryDependencies += "com.github.tminglei" %% "slick-pg_play-json" % "0.11.2"

libraryDependencies += "com.github.tminglei" %% "slick-pg_spray-json" % "0.11.2"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.10.0"

libraryDependencies += "com.github.etaty" %% "rediscala" % "1.6.0"