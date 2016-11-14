package io.github.bugra.db.redshift

/**
  * Created by bugra on 11/6/16.
  */


import java.util.Calendar

object Run {
  def main (args: Array[String]) {
    val today = Calendar.getInstance.getTime
    val appName = s"firstReader-$today"
    val masterName = "local[8]" // with 8 core
    val db = new Redshift(appName, masterName)

    val users = db.getUsers

    // NOT USED FIELDS which could be useful for recommender(need further investigation)
    // if, first_name, last_name, lat, lng, batch_size, device_type, email, flipped_from_waitlist,
    // hinge_user, token_expires, about_me, email_settings, allow_push_notif, get_daily_batch_alerts,
    // miles_from_metro_center, num_photos, num_profile_tags, flagged, ethnicity, religion,
    val fieldsOfInterest = List("birth_date", "gender", "orientation", "metro_area", "city", "date_joined", "last_seen",
      "chat_enabled", "hinge_user", "height", "min_age", "max_age", "match_radius", "session_count", "timezone",
      "hinge_friends", "min_age", "max_age", "num_photos", "num_profile_tags", "flagged", "work", "location",
      "hometown", "save_rate_bayes", "saved_rate_bayes", "match_rate", "conversation_rate", "contact_info_rate",
      "friends", "likes")


    println(users)
    users.printSchema()



    // Show 10 users
    users.orderBy("saved_rate_bayes").show(10)

  }

}