package org.wq.mysbt.basicGraphX

import org.apache.spark._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

/**
  * Created by wq on 16/4/16.
  */
object JoinDemo {

  def main(args: Array[String]) {
    val conf = new SparkConf()
    val sc = new SparkContext("local", "JoinDemo", conf)

    //    1,kankan,100
    //    2,hehe,200
    //    3,haha,300
    val vertexLines: RDD[String] = sc.textFile("hdfs://honest:8020/graphdata/generategraph-vertices.txt")
    val v: RDD[(VertexId, (String, Long))] = vertexLines.map(line => {
      val cols = line.split(",")
      (cols(0).toLong, (cols(1), cols(2).toLong))
    })

    //    1,2,100,2016/10/1
    //    2,3,200,2016/10/2
    //    3,1,300,2016/10/3
    val format = new java.text.SimpleDateFormat("yyyy/MM/dd")
    val edgeLines: RDD[String] = sc.textFile("hdfs://honest:8020/graphdata/generategraph-edge.txt")
    val e:RDD[Edge[((Long, java.util.Date))]] = edgeLines.map(line => {
      val cols = line.split(",")
      Edge(cols(0).toLong, cols(1).toLong, (cols(2).toLong, format.parse(cols(3))))
    })

    val graph:Graph[(String, Long), (Long, java.util.Date)] = Graph(v, e)

    val vertices:VertexRDD[(String, Long)] = graph.vertices
    val edges:EdgeRDD[((Long, java.util.Date))] = graph.edges


//    1,MJ
//    2,KB
    val verticesWithIn: RDD[(VertexId, String)] = sc.textFile("hdfs://honest:8020/graphdata/join.txt").map{
      line => {
        (line.split(",")(0).toLong, line.split(",")(1))
      }
    }
    val leftJoinedVertices = vertices.leftJoin(verticesWithIn){
      (vid, left, right) => (left._1, left._2, right.getOrElse("haha"))
    }
    println("\n\n~~~~~~~~~ Confirm leftJoined vertices ")
    leftJoinedVertices.collect.foreach(println(_))
//    (1,(kankan,100,MJ))
//    (3,(haha,300,haha))
//    (2,(hehe,200,KB))


    val innerJoinedVertices = vertices.innerJoin(verticesWithIn){
      (vid, left, right) => (left._1, left._2, right)
    }
    println("\n\n~~~~~~~~~ Confirm innerJoined vertices ")
    innerJoinedVertices.collect.foreach(println(_))
//    (1,(kankan,100,MJ))
//    (2,(hehe,200,KB))


    sc.stop()
  }

}
