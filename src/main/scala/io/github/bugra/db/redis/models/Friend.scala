package io.github.bugra.db.redis.models

import io.github.bugra.db.redis.Red

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by bugra on 11/7/16.
  */

object Friend {
  val WAIT_TIME = 1.5
  val redisTransaction = Red.redisTransaction

  def getFriends(playerId: String) = {
    val op = redisTransaction.smembers(playerId)
    redisTransaction.exec()
    val friendList = Await.result(op, WAIT_TIME seconds)
    friendList.map(_.utf8String).toSet - playerId
  }

  def numberOfFriends(playerId: String) = {
    val op = redisTransaction.scard(playerId)
    redisTransaction.exec()
    Await.result(op, WAIT_TIME seconds).toInt
  }

  def getFriendsOfFriends(playerId: String) = {
    val friends = getFriends(playerId).toSeq
    // TODO: Use sunion rather than function call, it would be faster
    //redisTransaction.sunion(friends: _.*)
    friends.flatMap(getFriends(_)).toSet - friends - Set(playerId)
  }

  def getMutualFriends(playerId: String, subjectId: String) = {
    val op = redisTransaction.sinter(playerId, subjectId)
    redisTransaction.exec()
    val mutualFriends = Await.result(op, WAIT_TIME seconds)
    mutualFriends.toSet
  }

  def ping() = {
    val op = redisTransaction.ping()
    redisTransaction.exec()
    val pong = Await.result(op, WAIT_TIME seconds)
    pong.toString
  }

  def getUsers = {
    val op = redisTransaction.scan()
    redisTransaction.exec()
    val userList = Await.result(op, WAIT_TIME seconds)
    userList.data.toSet
  }

  def size = {
    val op = redisTransaction.dbsize()
    redisTransaction.exec()
    val size = Await.result(op, WAIT_TIME seconds)
    size.toInt
  }

  def flush = {
    val op = redisTransaction.flushdb()
    redisTransaction.exec()
    Await.result(op, WAIT_TIME seconds).toString
  }

  def flushAll = {
    val op = redisTransaction.flushall()
    redisTransaction.exec()
    Await.result(op, WAIT_TIME seconds).toString
  }

  def isExist(playerId: String, subjectId: String) = {
    val op = redisTransaction.sismember(playerId, subjectId)
    redisTransaction.exec()
    Await.result(op, WAIT_TIME seconds)
  }

  def insert(playerId: String, subjectIds: List[String]) = {
    val op = redisTransaction.sadd(playerId, subjectIds)
    redisTransaction.exec()
    Await.result(op, WAIT_TIME seconds).toInt
  }

}
