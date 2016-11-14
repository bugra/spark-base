package io.github.bugra.db.postgresql.query

import io.github.bugra.db.postgresql.query.models._
import slick.lifted.TableQuery
import io.github.bugra.db.postgresql.MyPostgresDriver.api._

/**
  * Created by bugra on 11/6/16.
  */

object religions extends TableQuery(new ReligionTable(_)) {

  def byId(playerIds: String*) = religions
    .filter(_.playerId inSetBind playerIds)
    .map(t => t)
    .result

  def deleteById(playerIds: String*) = religions
    .filter(_.playerId inSetBind playerIds).delete

  def insert(religion: Religion) = religions += religion

  def insert(religionList: Seq[Religion]) = religions ++= religionList

  def update(religion: Religion) = religions
    .filter(_.playerId === religion.playerId)
    .update(religion)

}

object religionPreferences extends TableQuery(new ReligionPreferenceTable(_)) {
  def byId(playerIds: String*) = religions
    .filter(_.playerId inSetBind playerIds)
    .map(t => t)
    .result

  def deleteById(playerIds: String*) = religions
    .filter(_.playerId inSetBind playerIds).delete

  def insert(religion: Religion) = religions += religion

  def insert(religionList: Seq[Religion]) = religions ++= religionList

  def update(religion: Religion) = religions
    .filter(_.playerId === religion.playerId)
    .update(religion)
}

object ethnicities extends TableQuery(new EthnicityTable(_)) {
  def byId(playerIds: String*) = ethnicities
    .filter(_.playerId inSetBind playerIds)
    .map(t => t)
    .result

  def deleteById(playerIds: String*) = ethnicities
    .filter(_.playerId inSetBind playerIds).delete

  def insert(ethnicity: Ethnicity) = ethnicities += ethnicity

  def insert(ethnicityList: Seq[Ethnicity]) = ethnicities ++= ethnicityList

  def update(ethnicity: Ethnicity) = ethnicities
    .filter(_.playerId === ethnicity.playerId)
    .update(ethnicity)
}

object ethnicityPreferences extends TableQuery(new EthnicityPreferenceTable(_)) {
  def byId(playerIds: String*) = ethnicityPreferences
    .filter(_.playerId inSetBind playerIds)
    .map(t => t)
    .result

  def deleteById(playerIds: String*) = ethnicityPreferences
    .filter(_.playerId inSetBind playerIds).delete

  def insert(ethnicity: Ethnicity) = ethnicityPreferences += ethnicity

  def insert(ethnicityList: Seq[Ethnicity]) = ethnicityPreferences ++= ethnicityList

  def update(ethnicity: Ethnicity) = ethnicityPreferences
    .filter(_.playerId === ethnicity.playerId)
    .update(ethnicity)
}