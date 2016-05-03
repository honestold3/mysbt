package org.wq.mysbt.basicGraphX

import org.apache.spark._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

/**
  * Created by wq on 16/4/16.
  */
object MapReduceTripletsDemo {

  def main(args: Array[String]) {
    val conf = new SparkConf()
    val sc = new SparkContext("local", "MapReduceTripletsDemo", conf)

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
    val edges:RDD[Edge[((Long, java.util.Date))]] = edgeLines.map(line => {
      val cols = line.split(",")
      Edge(cols(0).toLong, cols(1).toLong, (cols(2).toLong, format.parse(cols(3))))
    })

    val graph:Graph[(String, Long), (Long, java.util.Date)] = Graph(vertices, edges)

    // 使用mapReduceTriplets来生成新的VertexRDD
    // 利用map对每一个三元组进行操作
    // 利用reduce对相同Id的顶点属性进行操作
    val newVertices:VertexRDD[Long] = graph.mapReduceTriplets(
      mapFunc = (edge:EdgeTriplet[(String, Long), (Long, java.util.Date)]) => {
        val toSrc = Iterator((edge.srcId, edge.srcAttr._2 - edge.attr._1+1))
        val toDst = Iterator((edge.dstId, edge.dstAttr._2 + edge.attr._1))
        //toSrc.foreach(println(_))
        //println("kankan")
        //toDst.foreach(x => println("ddd:"+x))
        toSrc ++ toDst
      },
      reduceFunc = (a1:Long, a2:Long) => {println(s"$a1,,$a2"); a1 + a2 }
    )
    println("\n\n~~~~~~~~~ Confirm Vertices Internal of newVertices ")
    newVertices.collect.foreach(println(_))
    //    (1,400)
    //    (3,500)
    //    (2,300)


    //创建点RDD
    val v: RDD[(VertexId, Int)] = sc.parallelize(Array( (1L, 1), (2L, 2),(3L, 3), (4L, 4)))
    //创建边RDD
    val e: RDD[Edge[(String,String)]] = sc.parallelize(Array(
      //Edge(1L, 2L, ("A","C")),
      //Edge(1L, 3L, ("A","B")),
      Edge(2L, 4L, ("B","D")),
      Edge(2L, 3L, ("E","B")),
      Edge(3L, 4L, ("C","D"))
    ))

    val g = Graph(v,e)

    val k = g.mapReduceTriplets(
      //Map
      (edge:EdgeTriplet[Int, (String,String)]) => {
        //val src = Iterator((edge.srcId, edge.srcAttr))
//        edge.srcAttr
//        edge.dstAttr
//        edge.attr
        val dst = Iterator((edge.dstId, edge.srcAttr+edge.dstAttr))
        //src ++ dst
        dst
      },
      //Reduce
      (a1:Int, a2:Int) => {println(s"$a1,,,$a2"); a1 + a2 }
    )

    println("\n\n~~~~~~~~~ Confirm mapReduceTriplets Internal of newVertices ")
    k.collect.foreach(println(_))


    val kankan = g.aggregateMessages[Int](
      triplet => { // Map Function
        // Send message to destination vertex
        triplet.sendToDst(triplet.srcAttr+triplet.dstAttr)

      },
      // Add sum attr
      (a1:Int, a2:Int) => {println(s"$a1,,$a2"); a1 + a2 } // Reduce Function
    )

    println("\n\n~~~~~~~~~ Confirm aggregateMessages Internal of newVertices ")
    kankan.collect.foreach(println(_))

  }

}
