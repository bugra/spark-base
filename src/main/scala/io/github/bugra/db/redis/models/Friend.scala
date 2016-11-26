package io.github.bugra.db.redis.models

import akka.util.ByteString
import io.github.bugra.db.redis.Red
import redis.commands.TransactionBuilder

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by bugra on 11/7/16.
  */

object Friend {
  val WAIT_TIME = 1.5
  val redisTransaction: TransactionBuilder = Red.redisTransaction

  def getFriends(playerId: String): Set[String] = {
    val op = redisTransaction.smembers(playerId)
    redisTransaction.exec()
    val friendList = Await.result(op, WAIT_TIME seconds)
    friendList.map(_.utf8String).toSet - playerId
  }

  def numberOfFriends(playerId: String): Int = {
    val op = redisTransaction.scard(playerId)
    redisTransaction.exec()
    Await.result(op, WAIT_TIME seconds).toInt
  }

  def getFriendsOfFriends(playerId: String): Set[String] = {
    // May not be a great idea to convert this into a sequence, could be used as a list
    var friendsOfFriends = Set[String]()
    val friends = getFriends(playerId).toList

    if (friends.nonEmpty) {
      val head::tail = friends
      val op = redisTransaction.sunion(head, tail:_*)
      friendsOfFriends = Await.result(op, WAIT_TIME seconds).map(_.utf8String).toSet

    }
    friendsOfFriends
  }

  def getMutualFriends(playerId: String, subjectId: String): Set[ByteString] = {
    val op = redisTransaction.sinter(playerId, subjectId)
    redisTransaction.exec()
    val mutualFriends = Await.result(op, WAIT_TIME seconds)
    mutualFriends.toSet
  }

  def ping(): String = {
    val op = redisTransaction.ping()
    redisTransaction.exec()
    val pong = Await.result(op, WAIT_TIME seconds)
    pong.toString
  }

  def getUsers: Set[String] = {
    val op = redisTransaction.scan()
    redisTransaction.exec()
    val userList = Await.result(op, WAIT_TIME seconds)
    userList.data.toSet
  }

  def size: Int = {
    val op = redisTransaction.dbsize()
    redisTransaction.exec()
    val size = Await.result(op, WAIT_TIME seconds)
    size.toInt
  }

  def flush: String = {
    val op = redisTransaction.flushdb()
    redisTransaction.exec()
    Await.result(op, WAIT_TIME seconds).toString
  }

  def flushAll: String = {
    val op = redisTransaction.flushall()
    redisTransaction.exec()
    Await.result(op, WAIT_TIME seconds).toString
  }

  def isExist(playerId: String, subjectId: String): Boolean = {
    val op = redisTransaction.sismember(playerId, subjectId)
    redisTransaction.exec()
    Await.result(op, WAIT_TIME seconds)
  }

  def insert(playerId: String, subjectIds: List[String]): Int = {
    val op = redisTransaction.sadd(playerId, subjectIds)
    redisTransaction.exec()
    Await.result(op, WAIT_TIME seconds).toInt
  }

}
