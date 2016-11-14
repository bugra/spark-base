package io.github.bugra.ml.cf

/**
  * Created by bugra on 11/6/16.
  */

import Constant.{MAX_NUMBER_OF_PEOPLE, MODEL_FILE_PATH, NUMBER_OF_RECOMMENDATIONS, NUMBER_OF_PARTITIONS, RANK_ORDER_OF_LATENT_MATRIX, NUMBER_OF_ITERATIONS, RATING_QUERY_SQL}
import java.util.Calendar

import breeze.linalg.{DenseMatrix, DenseVector}
import io.github.bugra.db.redshift.Redshift
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.util.MLUtils.kFold

object Train {
  def main(args: Array[String]): Unit = {
    // Recommender specific configuration and settings
    // Total number of people that we are going to ask from the recommender

    // If we are going to ask the recommender to explicitly rate people that are in the available pool
    // that is one thing, another thing we ask for recommender to recommend people and then do the hard filtering
    // second one is the least efficient in terms of asking recommendations from the recommender, but it does not
    // have to produce separate scores for each player_id and subject_id pair. Need timing comparison between both
    // approaches

    val numberOfRecommendations = NUMBER_OF_RECOMMENDATIONS
    val rank = RANK_ORDER_OF_LATENT_MATRIX
    val numIterations = NUMBER_OF_ITERATIONS
    val totalUsers = MAX_NUMBER_OF_PEOPLE
    val numPartitions = NUMBER_OF_PARTITIONS
    // Model path that the model is going to spilled to
    val modelPath = MODEL_FILE_PATH

    // Initialize the db connection with an appName and masterName
    // masterName is `local` for local development but needs to be changed to the spark master url
    val today = Calendar.getInstance.getTime
    val appName = s"training-$today"
    //val masterName = "spark://ip-192-168-5-199.ec2.internal:7077"
    val masterName = "local[8]"
    val db = new Redshift(appName, masterName)

    /*
    // We are treating the ids as strings as we are going to create a map to use integers for those
    val tempRatings = db.getDataFromRedshift(RATING_QUERY_SQL)
      .map(row => (row(0).toString, row(1).toString, 1.0))

    val playerIds = tempRatings.map(row => row._1)
      .distinct
    val subjectIds = tempRatings.map(row => row._2)
      .distinct
    // This could be a problem since zipWithUniqueId does not know anything about the input
    // recompute various unique ids for every ids that have been passed
    // We actually need a map to do traverse as well
    val playerIdUniqueIdMap = playerIds.union(subjectIds)
      .distinct
      .zipWithUniqueId()
      .collectAsMap()
    val uniqueIdPlayerIdMap = playerIdUniqueIdMap.map(_.swap)

    playerIdUniqueIdMap.take(100).map(println)

    val ratings = tempRatings.map { case (user, product, rate) =>
      Rating(playerIdUniqueIdMap(user).toInt, playerIdUniqueIdMap.getOrElse(product, 1L).toInt, rate)
    }

    // Partition the RDD with the weights of training, cross-validation and test portions
    /*
    val splits = ratings.randomSplit(Array(0.6, 0.2, 0.2), seed = 42)
    val (training, cv, test) = (splits(0).cache, splits(1).cache, splits(2).cache)

    val isPrintCount = false
    if (isPrintCount) {
      val trainingSize = training.count
      val cvSize = cv.count
      val testSize = test.count

      println(s"Training instance number is $trainingSize, cv is $cvSize and test size is $testSize")
    }

    */
    // Define the parameters that are going to be used by the cross-validation
    // These need to be defined
    val ranks = List(10)//, 50)
    val alphas = List(0.1)//, 10.0)
    val numIters = List(10)//, 20)

    def mostRatedUsers(ratings: RDD[Rating], uniqueIdPlayerIdMap: Map[Long, String], nUsers: Int=50) {
      val mostRated = ratings.map(_.product.toLong)
        .countByValue
        .toSeq
        .sortBy(-_._1)
        .take(nUsers)
        .map(_._1)
      mostRated map uniqueIdPlayerIdMap
    }

    def crossValidationALS(data: RDD[Rating], nFolds: Int = 10, seed: Int = 42,
                           rank: Int = 10, iter: Int = 20, alpha: Double = 0.01): (Double, Array[Double], Array[MatrixFactorizationModel]) = {
      // (train, test) pairs
      val folds = kFold(data, nFolds, seed)
      println("Folds are computed with size: \n")
      println(folds.size)
      // (model, test) pairs
      val models = folds.map { case (train, test) => (ALS.train(train, rank, iter, alpha), test) }
      println("models are finished")
      println(models.size)
      // evaluate model predictions on test data: RDD[((user, product), rating)]
      val pred = models.map { case (model, test) =>
        model.predict(test.map { case Rating(usr, prod, _) =>
          (usr, prod) }).map { case Rating(usr, prod, rate) =>
          ((usr,prod), rate) } }
      println("predictions are completed")
      // reformat truth: RDD[((user, product), rating)]
      val truth = models.map { case (_, test) => test.map { case Rating(usr, prod, rate) => ((usr, prod), rate) } }
      println("Truth is finished")
      // an array of RMSE for each test fold:
      val rmse = truth.zip(pred).map { case (tr, pd) =>
        tr.join(pd).map { case ((_, _), (t, p)) =>
          math.pow(p-t,2) }.mean() }.map(math.sqrt(_))
      // returns tuple: (total RMSE, RMSE per fold, model per fold)
      println("rmse is completed")
      (rmse.fold(0.0)(_+_)/rmse.length, rmse, models.map(_._1))
    }

    var results: Map[String, Tuple3[Double, Array[Double], Array[MatrixFactorizationModel]]] = Map()
    for (rank <- ranks; alpha <- alphas; numIter <- numIters) {
      val key = s"rank_$rank-alpha_$alpha-nIter_$numIter"
      println(key)
      println(ratings)
      val temp = (key -> crossValidationALS(ratings, rank=rank, iter=numIter, alpha=alpha))
      results = results + temp
      println("Temp Map is:\n")
      println(temp)
      println(s"The combination of $key is completed with RMSE:")
    }

    println("Results are: \n")
    println(results)

    // TODO: Get the best model and save it in some place from results
    // Report rmse of the model as well
    // val rmse = computeRMSE(ratings, model)
    // println(s"RMSE of the best model is: $rmse")
    // Then save
    def computeRMSE(ratings: RDD[Rating], model: MatrixFactorizationModel): Double = {
      val usersProducts = ratings.map { case Rating(user, product, rate) =>
        (user, product)
      }
      val predictions =  model.predict(usersProducts).map { case Rating(user, product, rate) =>
        ((user, product), rate)
      }
      val ratesAndPreds = ratings.map { case Rating(user, product, rate) =>
        ((user, product), rate)
      }.join(predictions)

      val meanSquaredError = ratesAndPreds.map { case ((user, product), (r1, r2)) =>
        val err = (r1 - r2)
        err * err
      }.mean()

      meanSquaredError
    }

    // After getting the training in one data structure and putting all of data in one place(putting all ducks in a row)
    // We need to first divide the dataset into three different groups => (training, cross-validation, test)

    // TRAINING
    val model = Util.time {ALS.train(tempRatings, rank, numIterations)}

    // This will retrieve all of the recommendations
    val allTheRecommendations = Util.time {model.recommendProductsForUsers(numberOfRecommendations)}
    println(allTheRecommendations.take(100).foreach(println))

    */

  }

}
