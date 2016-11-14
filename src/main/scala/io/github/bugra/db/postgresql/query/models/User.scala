package io.github.bugra.db.postgresql.query.models

/**
  * Created by bugra on 11/6/16.
  */

import io.github.bugra.db.postgresql.MyPostgresDriver.api._
import org.joda.time.LocalDate

// TODO: Uncomment the dates
case class User(
                 id: String,
                 facebookId: BigDecimal,
                 firstName: String,
                 lastName: String,
                 //birthDate: LocalDate,
                 gender: String,
                 orientation: Option[String],
                 location: Option[String],
                 neighborhood: Option[String],
                 hometown: Option[String],
                 //joinDate: Option[LocalDate],
                 height: Option[Int],
                 maxHeight: Option[Int],
                 minHeight: Option[Int],
                 minAge: Option[Int],
                 maxAge: Option[Int],
                 minDistance: Option[Int],
                 maxDistance: Option[Int],
                 facebookToken: Option[String],
                 isTokenValid: Option[Boolean],
                 educationIds: Option[List[String]],
                 workIds: Option[List[String]],

                 /*
                 isAgeDealbreaker: Option[Boolean],
                 isDistanceDealbreaker: Option[Boolean],
                 isHeightDealbreaker: Option[Boolean],
                 isEthnicityDealbreaker: Option[Boolean],
                 isReligionDealbreaker: Option[Boolean],
                 */
                 state: Option[String]
               )


// TODO: Foreignkey and other bits and pieces need to be adjusted
class UserTable(tag: Tag) extends Table[User](tag, "user") {
  def id = column[String]("id", O.PrimaryKey)
  def facebookId = column[BigDecimal]("facebook_id")
  def firstName = column[String]("first_name")
  def lastName = column[String]("last_name")
  //def birthDate = column[LocalDate]("birth_date")
  def gender = column[String]("gender")
  def orientation = column[Option[String]]("orientation")
  def location = column[Option[String]]("location")
  def neighborhood = column[Option[String]]("neighborhood")
  def hometown = column[Option[String]]("hometown")

  // def joinDate = column[Option[LocalDate]]("join_date")

  def height = column[Option[Int]]("height")
  def maxHeight = column[Option[Int]]("max_height")
  def minHeight = column[Option[Int]]("min_height")
  def minAge = column[Option[Int]]("min_age")
  def maxAge = column[Option[Int]]("max_age")
  def minDistance = column[Option[Int]]("min_distance")
  def maxDistance = column[Option[Int]]("max_distance")
  def facebookToken = column[Option[String]]("facebook_token")
  def isTokenValid = column[Option[Boolean]]("is_token_valid")
  def educationIds = column[Option[List[String]]]("education_ids")
  def workIds = column[Option[List[String]]]("work_ids")

  /*
  def isAgeDealbreaker = column[Option[Boolean]]("is_age_dealbreaker")
  def isDistanceDealbreaker = column[Option[Boolean]]("is_distance_dealbreaker")
  def isHeightDealbreaker = column[Option[Boolean]]("is_height_dealbreaker")
  def isEthnicityDealbreaker = column[Option[Boolean]]("is_ethnicity_dealbreaker")
  def isReligionDealbreaker = column[Option[Boolean]]("is_religion_dealbreaker")
  */

  def state = column[Option[String]]("state")
  // def updatedAt = column[Option[LocalDate]]("updated_at")

  def * = (id, facebookId, firstName, lastName, gender, orientation, location, neighborhood, hometown, height, maxHeight, minHeight, minAge, maxAge, minDistance, maxDistance, facebookToken, isTokenValid, educationIds, workIds, state) <> (User.tupled, User.unapply)

}
