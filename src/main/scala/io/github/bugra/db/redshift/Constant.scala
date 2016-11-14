package io.github.bugra.db.redshift

/**
  * Created by bugra on 11/6/16.
  */

object Constant {
  val AWS_ACCESS_KEY = sys.env("AWS_ACCESS_KEY")
  val AWS_SECRET_KEY = sys.env("AWS_SECRET_KEY")
  val REDSHIFT_PASSWORD = sys.env("REDSHIFT_PASSWORD")
  val REDSHIFT_PORT = sys.env("REDSHIFT_PORT")
  val REDSHIFT_USER = sys.env("REDSHIFT_USER")
  val REDSHIFT_DB_NAME = sys.env("REDSHIFT_DB_NAME")
  val REDSHIFT_DB_URL = sys.env("REDSHIFT_DB_URL")
  val S3_SPARK_BUCKET = sys.env("S3_SPARK_BUCKET")

  val REDSHIFT_JDBC_URL = s"jdbc:redshift://$REDSHIFT_DB_URL:$REDSHIFT_PORT/$REDSHIFT_DB_NAME?user=$REDSHIFT_USER&password=$REDSHIFT_PASSWORD".toString

}