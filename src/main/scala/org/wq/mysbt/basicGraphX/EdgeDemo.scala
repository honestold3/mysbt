package org.wq.mysbt.basicGraphX

import org.apache.spark._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

/**
  * Created by wq on 16/4/15.
  */
object EdgeDemo {

  def main(args: Array[String]) {
    //println(args(0))
    //println(args(1))
    val conf = new SparkConf()
    val sc = new SparkContext("local", "DegeDemo", conf)


//    1 2
//    2 3
//    3 1
    val graph = GraphLoader.edgeListFile(sc, "hdfs://honest:8020/graphdata/edges.txt").cache()


    //不带属性的边，其属性会属性默认为1
    println("\n\n~~~~~~~~~ Confirm Edges Internal of graph ")
    graph.edges.collect.foreach(println(_))
//    Edge(1,2,1)
//    Edge(2,3,1)
//    Edge(3,1,1)


    //只输入边的Id信息，则顶点属性默认为1
    println("\n\n~~~~~~~~~ Confirm Vertices Internal of graph ")
    graph.vertices.collect.foreach(println(_))
//    (1,1)
//    (3,1)
//    (2,1)

    sc.stop

  }

}
