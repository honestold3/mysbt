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
object KankanDemo {

  def main(args: Array[String]) {

    val conf = new SparkConf()
    val sc = new SparkContext("local", "test", conf)

    //创建点RDD
    val users: RDD[(VertexId, String)] = sc.parallelize(Array(
      (3L, "cross1"), (7L, "cross2"),
      (5L, "cross3"), (2L, "cross4")))
    //创建边RDD
    val relationships: RDD[Edge[(String,String)]] = sc.parallelize(Array(
      Edge(3L, 7L, ("A","C")), Edge(3L, 5L, ("A","B")),
      Edge(2L, 5L, ("E","B")), Edge(5L, 7L, ("C","D"))))

    //val relationships: RDD[Edge[(String,String)]] = sc.parallelize(Array(
      //Edge(3L, 5L, ("A","B")),Edge(3L, 6L, ("T","Y")), Edge(5L, 7L, ("C","D"))))
    //定义一个默认用户，避免有不存在用户的关系
    //val defaultUser = ("John Doe", "Missing")
    //构造Graph
    val graph = Graph(users, relationships)

    val data = ("A","B","C","D")

    val fcount2 = graph.edges.filter(edge => edge.attr._1==data._1).collect.foreach(println _)


    println("---------------------------------------------------------")

    /*val sssp = graph.pregel((Long,List[VertexId]()))(
      // Vertex Program
      (id, dist, newDist) => dist,

      // Send Message
      triplet => {
//        if (triplet.srcAttr._1 < triplet.dstAttr._1 - triplet.attr ) {
//          Iterator((triplet.dstId, (triplet.srcAttr._1 + triplet.attr , triplet.srcAttr._2 :+ triplet.dstId)))
//        } else {
//          Iterator.empty
//        }
        Iterator((triplet.dstId, (triplet.dstId , triplet.attr._1 +":"+ triplet.attr._2)))
      },
      //Merge Message
      (a, b) => b)
    println(sssp.vertices.collect.mkString("\n"))
*/

    val am = graph.aggregateMessages[(String, String)](
      triplet => { // Map Function
//        if (triplet.srcAttr > triplet.dstAttr) {
//          // Send message to destination vertex containing counter and age
//          triplet.sendToDst(1, triplet.srcAttr)
//        }
        triplet.sendToSrc(triplet.srcId.toString+"-"+triplet.dstId.toString,triplet.attr.toString())
      },
      //Reduce Function
      (a, b) => (a._1+";"+b._1, a._2+b._2)
    )

    am.collect.foreach(println(_))

    println("---------------------------------------------------------")



  }

}
