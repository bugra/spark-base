package io.github.bugra.db.postgresql.query.models

/**
  * Created by bugra on 11/6/16.
  */

import io.github.bugra.db.postgresql.MyPostgresDriver.api._

case class Ethnicity(playerId: String, religionId: String)
case class Religion(playerId: String, religionId: String)

class EthnicityTable(tag: Tag) extends Table[Ethnicity](tag, "ethnicity") {
  def playerId = column[String]("player_id", O.PrimaryKey)
  def ethnicityId = column[String]("ethnicity_id")

  def * = (playerId, ethnicityId) <> (Ethnicity.tupled, Ethnicity.unapply)

}

class EthnicityPreferenceTable(tag: Tag) extends Table[Ethnicity](tag, "ethnicity_preference") {
  def playerId = column[String]("player_id", O.PrimaryKey)
  def ethnicityId = column[String]("ethnicity_id")

  def * = (playerId, ethnicityId) <> (Ethnicity.tupled, Ethnicity.unapply)

}

class ReligionTable(tag: Tag) extends Table[Religion](tag, "religion") {
  def playerId = column[String]("player_id", O.PrimaryKey)
  def religionId = column[String]("religion_id")

  def * = (playerId, religionId) <> (Religion.tupled, Religion.unapply)

}

class ReligionPreferenceTable(tag: Tag) extends Table[Religion](tag, "religion_preference") {
  def playerId = column[String]("player_id", O.PrimaryKey)
  def religionId = column[String]("religion_id")

  def * = (playerId, religionId) <> (Religion.tupled, Religion.unapply)

}
