package io.github.bugra.ml.cf

/**
  * Created by bugra on 11/6/16.
  */
object Constant {
  val NUMBER_OF_RECOMMENDATIONS = 500
  // Rank of the latent matrix factorization
  val RANK_ORDER_OF_LATENT_MATRIX = 50
  // Total number of iterations that als will go through to learn the latent factors from the matrix
  val NUMBER_OF_ITERATIONS = 100
  // The limit of the user ids that we are going to retrieve from the rating table
  val MAX_NUMBER_OF_PEOPLE = 100000000
  // Number of Partitions that are going to be used for various operations
  val NUMBER_OF_PARTITIONS = 20
  // File path that we are going to spill the model(s3 file path should be better)
  val MODEL_FILE_PATH = "/Users/bugra/spark.model"

  val RATING_QUERY_SQL = s"SELECT * FROM prod_rating where created_at > '2016-06-30' LIMIT $MAX_NUMBER_OF_PEOPLE"

  val DB_URL = sys.env("DB_URL")
  val DB_USERNAME = sys.env("DB_USERNAME")
  val DB_PASSWORD = sys.env("DB_PASSWORD")
  val DB_NAME = sys.env("DB_NAME")
  val DB_PORT = sys.env("DB_PORT")
  val DB_JDBC_URL = s"jdbc:postgresql://$DB_URL:$DB_PORT/$DB_NAME?user=$DB_USERNAME&password=$DB_PASSWORD".toString
}
