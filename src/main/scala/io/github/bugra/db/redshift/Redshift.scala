package io.github.bugra.db.redshift

import Constant.{AWS_ACCESS_KEY, AWS_SECRET_KEY, S3_SPARK_BUCKET, REDSHIFT_JDBC_URL}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SQLContext}


/**
  * Created by bugra on 11/6/16.
  */


class Redshift(appName: String, masterName: String) {
  // Constant variables
  val conf = new SparkConf().setAppName(appName)
    .setMaster(masterName)
    .set("spark.executor.memory", "10g")
    .set("spark.driver.memory", "4g")

  val sc = SparkContext.getOrCreate(conf)
  val sqlContext = SQLContext.getOrCreate(sc)

  // TODO: This s3ApplicationBucket could be suffixed with the date so that we could have
  // data files for every day in their respective folders
  val s3ApplicationBucket = S3_SPARK_BUCKET + "/spark-tutorial"

  // Hdfs file format and
  sc.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", AWS_ACCESS_KEY)
  sc.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", AWS_SECRET_KEY)

  sc.hadoopConfiguration.set("fs.s3.awsAccessKeyId", AWS_ACCESS_KEY)
  sc.hadoopConfiguration.set("fs.s3.awsSecretAccessKey", AWS_SECRET_KEY)






  def getUsers: DataFrame = {
    val df: DataFrame = sqlContext.read
      .format("com.databricks.spark.redshift")
      .option("url", REDSHIFT_JDBC_URL)
      .option("dbtable", "users")
      .option("tempdir", s3ApplicationBucket)
      .load()
    df
  }


  def getDataFromRedshift(query: String): DataFrame = {
    val sqlContext = new SQLContext(sc)

    val df: DataFrame = sqlContext.read
      .format("com.databricks.spark.redshift")
      .option("url", REDSHIFT_JDBC_URL)
      .option("tempdir", s3ApplicationBucket)
      .option("query", query)
      .load()
    df
  }


  def cleanUp: Unit = {
    sc.stop()
  }


}

