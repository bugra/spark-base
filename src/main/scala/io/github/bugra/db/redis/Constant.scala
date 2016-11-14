package io.github.bugra.db.redis

/**
  * Created by bugra on 11/7/16.
  */
object Constant {
  val REDIS_DB_URL = sys.env("REDIS_DB_URL")
  val REDIS_PORT = sys.env("REDIS_PORT").toInt
}
