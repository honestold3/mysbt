package org.wq.mysbt.basicGraphX

import org.apache.spark._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

/**
  * Created by wq on 16/4/15.
  */
object GenerateGraphDemo {

  def main(args: Array[String]) {
    val conf = new SparkConf()
    val sc = new SparkContext("local", "GenerateGraphDemo", conf)

//    1,kankan,100
//    2,hehe,200
//    3,haha,300
    val vertexLines: RDD[String] = sc.textFile("hdfs://honest:8020/graphdata/generategraph-vertices.txt")
    val vertices: RDD[(VertexId, (String, Long))] = vertexLines.map{
      line => {
        val cols = line.split(",")
        (cols(0).toLong, (cols(1), cols(2).toLong))
      }
    }

//    1,2,100,2016/10/1
//    2,3,200,2016/10/2
//    3,1,300,2016/10/3
    val format = new java.text.SimpleDateFormat("yyyy/MM/dd")
    val edgeLines: RDD[String] = sc.textFile("hdfs://honest:8020/graphdata/generategraph-edge.txt")
    val edges:RDD[Edge[(Long, java.util.Date)]] = edgeLines.map(line => {
      val cols = line.split(",")
      Edge(cols(0).toLong, cols(1).toLong, (cols(2).toLong, format.parse(cols(3))))
    })

    val graph:Graph[(String, Long), (Long, java.util.Date)] = Graph(vertices, edges)

    //val g = Graph(edges)

    println("\n\n~~~~~~~~~ Confirm Edges Internal of graph ")
    graph.edges.collect.foreach(println(_))
//    Edge(1,2,(100,Sat Oct 01 00:00:00 CST 2016))
//    Edge(2,3,(200,Sun Oct 02 00:00:00 CST 2016))
//    Edge(3,1,(300,Mon Oct 03 00:00:00 CST 2016))

    println("\n\n~~~~~~~~~ Confirm Vertices Internal of graph ")
    graph.vertices.collect.foreach(println(_))
//    (1,(kankan,100))
//    (3,(haha,300))
//    (2,(hehe,200))

    sc.stop

  }

}
