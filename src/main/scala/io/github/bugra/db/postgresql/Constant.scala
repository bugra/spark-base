package io.github.bugra.db.postgresql

/**
  * Created by bugra on 11/6/16.
  */


object Constant {
  val DB_URL = sys.env("DB_URL")
  val DB_USERNAME = sys.env("DB_USERNAME")
  val DB_PASSWORD = sys.env("DB_PASSWORD")
  val DB_NAME = sys.env("DB_NAME")
  val DB_PORT = sys.env("DB_PORT")
  val DB_JDBC_URL = s"jdbc:postgresql://$DB_URL:$DB_PORT/$DB_NAME?user=$DB_USERNAME&password=$DB_PASSWORD".toString
}
