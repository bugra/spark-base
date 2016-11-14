package io.github.bugra.db.redis

import io.github.bugra.db.redis.Constant.{REDIS_DB_URL, REDIS_PORT}
import io.github.bugra.db.redis.models.Friend

/**
  * Created by bugra on 11/7/16.
  */
object Red {
  implicit val akkaSystem = akka.actor.ActorSystem()
  val redisClient = redis.RedisClient(host=REDIS_DB_URL, port=REDIS_PORT)
  val redisBlockingClient = redis.RedisBlockingClient(host=REDIS_DB_URL, port=REDIS_PORT)
  val redisTransaction = redisClient.transaction()

  val playerId = "296a5454486b5ff0b4340c6adc27d68d"

  def main(args: Array[String]): Unit = {
    val friends = Friend.getFriends(playerId)
    val friendsOfFriends = Friend.getFriendsOfFriends(playerId)
    val users = Friend.getUsers
    val totalNumberOfUsers = users.size
    val ping = Friend.ping()

    println(s"All of the friends from object $friends")
    println(s"All of the friends of friends from object $friendsOfFriends")
    println(s"Ping is $ping")
    println(s"All of the users are $users")
    println(s"Total Number of users is $totalNumberOfUsers")
    akkaSystem.shutdown()
  }

}
