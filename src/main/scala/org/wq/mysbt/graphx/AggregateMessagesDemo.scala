package org.wq.mysbt.graphx

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.SparkContext
import org.apache.spark._
import org.apache.spark.graphx._
import org.apache.spark.graphx.Graph
import org.apache.spark.graphx.Edge
import org.apache.spark.graphx.VertexRDD
import org.apache.spark.graphx.util.GraphGenerators
import org.apache.spark.graphx.GraphLoader
import org.apache.spark.storage.StorageLevel
import org.apache.spark.rdd.RDD
/**
  * Created by wq on 16/4/11.
  */
object AggregateMessagesDemo {

  def main(args: Array[String]) {

    val conf = new SparkConf()
    val sc = new SparkContext("local", "test", conf)

    // Create a graph with "age" as the vertex property.  Here we use a random graph for simplicity.
    val graph: Graph[Double, Int] =
      GraphGenerators.logNormalGraph(sc, numVertices = 10).mapVertices( (id, _) => id.toDouble )

    graph.triplets.collect.foreach(triplet => println(triplet.srcId + "-" + triplet.srcAttr + "-" +
      triplet.attr + "-" + triplet.dstId + "-" + triplet.dstAttr))

    println("----------------------------------------------------------")
    // Compute the number of older followers and their total age
    val olderFollowers: VertexRDD[(Int, Double)] = graph.aggregateMessages[(Int, Double)](
      triplet => { // Map Function
        if (triplet.srcAttr > triplet.dstAttr) {
          // Send message to destination vertex containing counter and age
          triplet.sendToDst(1, triplet.srcAttr)
        }

      },
      // Add counter and age
      (a, b) => (a._1 + b._1, a._2 + b._2) // Reduce Function
    )
    // Divide total age by number of older followers to get average age of older followers
    val avgAgeOfOlderFollowers: VertexRDD[Double] =
      olderFollowers.mapValues( (id, value) => value match { case (count, totalAge) => totalAge / count } )
    // Display the results
    avgAgeOfOlderFollowers.collect.foreach(println(_))

  }

}
