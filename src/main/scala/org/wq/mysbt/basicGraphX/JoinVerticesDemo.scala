package org.wq.mysbt.basicGraphX

import org.apache.spark._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

/**
  * Created by wq on 16/4/15.
  */
object JoinVerticesDemo {

  def main(args: Array[String]) {
    val conf = new SparkConf()
    val sc = new SparkContext("local", "JoinVerticesDemo", conf)

    //    1 2
    //    2 3
    //    3 1
    val graph = GraphLoader.edgeListFile(sc, "hdfs://honest:8020/graphdata/edges.txt").cache()

//    1,kankan
//    2,kankan1
    val vertexLines = sc.textFile("hdfs://honest:8020/graphdata/vertices.txt")
    val users: RDD[(VertexId, String)] = vertexLines.map{
      line => {
        val cols = line.split(",")
        (cols(0).toLong, cols(1))
      }
    }


    val graph2 = graph.mapVertices((id, attr) => "default").joinVertices(users){(vid, empty, user) => user}
    println("\n\n~~~~~~~~~ Confirm Vertices Internal of graph2 ")
    graph2.vertices.collect.foreach(println(_))
    graph2.edges.collect.foreach(println _)
//    (1,kankan)
//    (3,)
//    (2,kankan1)


    val graph3 = graph.mapVertices((id, attr) => "").outerJoinVertices(users){(vid, empty, user) => user.getOrElse("None")}
    println("\n\n~~~~~~~~~ Confirm Vertices Internal of graph3 ")
    graph3.vertices.collect.foreach(println(_))
//    (1,kankan)
//    (3,None)
//    (2,kankan1)

    sc.stop()

  }

}
