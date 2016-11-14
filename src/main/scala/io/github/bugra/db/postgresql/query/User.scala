package io.github.bugra.db.postgresql.query

/**
  * Created by bugra on 11/6/16.
  */
import io.github.bugra.db.postgresql.Constant.DB_JDBC_URL
import io.github.bugra.db.postgresql.MyPostgresDriver.api.{Database, _}
import io.github.bugra.db.postgresql.query.models.{User, UserTable}
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration._


object users extends TableQuery(new UserTable(_)) {

  // This accepts a single or multiple ids to be able to do filtering
  // on top of the users table and filter based on that one
  // You can pass a single id or multiple ids to be able to return them
  def byId(ids: String*) = users
    .filter(_.id inSetBind ids)
    .map(t => t)
    .result

  def byFirstName(first: String) = users
      .filter(_.firstName === first)
      .map(t => t)
      .result

  def deleteById(ids: String*) = users
    .filter(_.id inSetBind ids).delete

  def insert(user: User) = users += user
  def insert(userList: Seq[User]) = users ++= userList
  def update(user: User) = users
    .filter(_.id === user.id)
    .update(user)

  def compatible(playerId: String) = {
    val ethnicityIds = ethnicities.byId(playerId)
    val ethnicityPreferenceIds = ethnicityPreferences.byId(playerId)
    val religionIds = religions.byId(playerId)
    val religionPreferenceIds = religionPreferences.byId(playerId)

    // TODO: to be implemented
    /*

    val compatibleIds = users join ethnicities on(_.id is _.playerId)
        join religions on(_.id === _.playerId)
        join ethnicityPreferences on(_.id === _.playerId)
        join religionPreferences on(_.id === _.playerId)
    */



  }

  def main(args: Array[String]): Unit = {

    val anId = "296a5454486b5ff0b4340c6adc27d68d"
    val db = Database.forURL(url = DB_JDBC_URL, driver = "org.postgresql.Driver")
    val waitTime = 1.5
    val q = byId(anId)
    val future = db.run(q)
    val temp = Await.result(future, waitTime seconds)
    println(temp)

    val q2 = deleteById("19533fd239f84c449f2f82177d00bfdf")
    val future2 = db.run(q2)
    val temp2 = Await.result(future2, waitTime seconds)
    println(temp2)




  }
}