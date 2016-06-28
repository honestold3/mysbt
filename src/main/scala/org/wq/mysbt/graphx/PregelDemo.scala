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
object PregelDemo {

  def main(args: Array[String]) {
    val conf = new SparkConf()
    val sc = new SparkContext("local", "PregelDemo", conf)

    // A graph with edge attributes containing distances
    val graph: Graph[Long, Double] =
      GraphGenerators.logNormalGraph(sc, numVertices = 6).mapEdges(e => e.attr.toDouble)

    graph.edges.foreach(println)

    println("-------------------------------")

    graph.vertices.foreach(println)

    //graph.triplets.collect.foreach(triplet => println(triplet.srcId + "-" + triplet.srcAttr + "-" +triplet.attr + "-" + triplet.dstId + "-" + triplet.dstAttr))
    /*
    val sourceId: VertexId = 42 // The ultimate source
    // Initialize the graph such that all vertices except the root have distance infinity.
    val initialGraph = graph.mapVertices((id, _) => if (id == sourceId) 0.0 else Double.PositiveInfinity)
    val sssp = initialGraph.pregel(Double.PositiveInfinity)(
      (id, dist, newDist) => math.min(dist, newDist), // Vertex Program
      triplet => {  // Send Message
        if (triplet.srcAttr + triplet.attr < triplet.dstAttr) {
          Iterator((triplet.dstId, triplet.srcAttr + triplet.attr))
        } else {
          Iterator.empty
        }
      },
      (a,b) => math.min(a,b) // Merge Message
    )
    println(sssp.vertices.collect.mkString("\n"))
*/

    //graph.edges.collect.foreach(println _)

    val sourceId: VertexId = 0 // The ultimate source

    // Initialize the graph such that all vertices except the root have distance infinity.
    val initialGraph : Graph[(Double, List[VertexId]), Double] = graph.mapVertices {
      (id, _) => if (id == sourceId) (0.0, List[VertexId](sourceId)) else (Double.PositiveInfinity, List[VertexId]())
    }

    //val sssp = initialGraph.pregel((Double.PositiveInfinity, List[VertexId]()), Int.MaxValue, EdgeDirection.Out)(
    val sssp = initialGraph.pregel((Double.PositiveInfinity,List[VertexId]()))(
      // Vertex Program
      (id, dist, newDist) => if (dist._1 < newDist._1) dist else newDist,

      // Send Message
      triplet => {
//        if (triplet.srcAttr._1 + triplet.attr  < triplet.dstAttr._1) {
//          Iterator((triplet.dstId, (triplet.srcAttr._1 + triplet.attr , triplet.srcAttr._2 :+ triplet.dstId)))
//        } else {
//          Iterator.empty
//        }
        Iterator.empty
      },
      //Merge Message
      (a, b) => if (a._1 < b._1) a else b)
    println(sssp.vertices.collect.mkString("\n"))

/*
    //创建点RDD
    val v: RDD[(VertexId, Int)] = sc.parallelize(Array( (1L, 1), (2L, 2),(3L, 3), (4L, 4)))
    //创建边RDD
    val e: RDD[Edge[Double]] = sc.parallelize(Array(
      //Edge(1L, 2L, 1.0),
      //Edge(1L, 3L, 1.0),
      Edge(2L, 4L, 1.0),
      Edge(2L, 3L, 1.0),
      Edge(3L, 4L, 1.0)
    ))
    val g = Graph(v,e)

    val sId: VertexId = 0
    val initial  = g.mapVertices{
      (id, _) => if (id == sId) 0.0 else 1.0
    }
    println("-----------------------------inital--------------------")
    initial.edges.collect.foreach(println _)

    val sp = initial.pregel(Double.PositiveInfinity)(
      // Vertex Program
      (id, dist, newDist) => newDist ,

      // Send Message
      triplet => {
        if (triplet.srcAttr+triplet.attr < triplet.dstAttr ) {
          println(s"${triplet.srcAttr}::${triplet.dstAttr}::${triplet.attr}")
          Iterator((triplet.dstId, triplet.srcAttr+triplet.dstAttr))
        } else {
          println("kankan!!!!")
          println(s"${triplet.srcAttr}:::${triplet.dstAttr}:::${triplet.attr}")
          //Iterator((triplet.dstId, triplet.srcAttr+triplet.dstAttr))
          Iterator.empty
        }
        //Iterator((triplet.dstId, triplet.srcAttr+triplet.dstAttr ))
      },
      //Merge Message
      (a, b) => a+b
    )
    //println(sp.vertices.collect.mkString("\n"))
    */
    sc.stop()
  }

}
