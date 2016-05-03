package org.wq.mysbt.basicGraphX

import org.apache.spark._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.graphx._
import org.apache.spark.graphx.lib.ShortestPaths
import org.apache.spark.rdd.RDD

/**
  * Created by wq on 16/4/27.
  */
object ShortPathsDemo {

  def main(args: Array[String]) {

    val conf = new SparkConf()
    val sc = new SparkContext("local", "ShortPathsDemo", conf)

    // 测试的真实结果，后面用于对比
    val shortestPaths = Set(
      (1, Map(1 -> 0, 4 -> 2)), (2, Map(1 -> 1, 4 -> 2)), (3, Map(1 -> 2, 4 -> 1)),
      (4, Map(1 -> 2, 4 -> 0)), (5, Map(1 -> 1, 4 -> 1)), (6, Map(1 -> 3, 4 -> 1)))

    // 构造无向图的边序列
    val edgeSeq = Seq((1, 2), (1, 5), (2, 3), (2, 5), (3, 4), (4, 5), (4, 6)).flatMap {
      case e => Seq(e, e.swap)
    }
    edgeSeq.foreach(println)

    // 构造无向图
    val edges = sc.parallelize(edgeSeq).map { case (v1, v2) => (v1.toLong, v2.toLong) }

    edges.collect.foreach(println)
    val graph = Graph.fromEdgeTuples(edges, 1)

    // 要求最短路径的点集合
    val landmarks = Seq(1, 4).map(_.toLong)

    ShortestPaths.run(graph, landmarks).vertices.collect.foreach(println)

    // 计算最短路径
//    val results = ShortestPaths.run(graph, landmarks).vertices.collect.map {
//      case (v, spMap) => (v, spMap.mapValues(i => i))
//    }

    //results.foreach(println)

    // 与真实结果对比
    //assert(results.toSet === shortestPaths)



  }

}
