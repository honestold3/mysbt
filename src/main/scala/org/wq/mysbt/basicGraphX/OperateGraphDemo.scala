package org.wq.mysbt.basicGraphX

import org.apache.spark._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

import scala.reflect.ClassTag

/**
  * Created by wq on 16/4/15.
  */
object OperateGraphDemo {

  def main(args: Array[String]) {
    val conf = new SparkConf()
    val sc = new SparkContext("local", "OperateGraphDemo", conf)

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

//--------------------------------------openrating-----------------------------------
    val graph2:Graph[Long, (Long, java.util.Date)] = graph.mapVertices{
      (vid:VertexId, attr:(String, Long)) => attr._1.length * attr._2
    }
    println("\n\n~~~~~~~~~ Confirm Vertices Internal of graph2 ")
    graph2.vertices.collect.foreach(println(_))
//    (1,600)
//    (3,1200)
//    (2,800)

    val graph3:Graph[(String, Long), Long] = graph.mapEdges( edge => edge.attr._1 )
    println("\n\n~~~~~~~~~ Confirm Vertices Internal of graph3 ")
    graph3.edges.collect.foreach(println(_))
//    Edge(1,2,100)
//    Edge(2,3,200)
//    Edge(3,1,300)

    println("\n\n~~~~~~~~~ Confirm triplets Internal of graph ")
    graph.triplets.collect.foreach(println(_))
//    ((1,(kankan,100)),(2,(hehe,200)),(100,Sat Oct 01 00:00:00 CST 2016))
//    ((2,(hehe,200)),(3,(haha,300)),(200,Sun Oct 02 00:00:00 CST 2016))
//    ((3,(haha,300)),(1,(kankan,100)),(300,Mon Oct 03 00:00:00 CST 2016))

    val graph4:Graph[(String, Long), Long] = graph.mapTriplets(edge => edge.srcAttr._2 + edge.attr._1 + edge.dstAttr._2)
    println("\n\n~~~~~~~~~ Confirm Vertices Internal of graph4 ")
    graph4.edges.collect.foreach(println(_))
//    Edge(1,2,400)
//    Edge(2,3,700)
//    Edge(3,1,700)


    val filteredVertices:VertexRDD[(String, Long)] = graph.vertices.filter{
      case (vid:VertexId, (name:String, value:Long)) => value > 150
    }
    println("\n\n~~~~~~~~~ Confirm filtered vertices ")
    filteredVertices.collect.foreach(println(_))
//    (3,(haha,300))
//    (2,(hehe,200))

    val mappedVertices:VertexRDD[Long] = graph.vertices.mapValues{
      (vid:VertexId, attr:(String, Long)) => attr._2 * attr._1.length
    }
    println("\n\n~~~~~~~~~ Confirm mapped vertices ")
    mappedVertices.collect.foreach(println(_))
//    (1,600)
//    (3,1200)
//    (2,800)

    println("\n\n~~~~~~~~~ Confirm diffed vertices ")
    // Remove vertices from this set that appear in the other set
    // val diffedVertices:VertexRDD[(String, Long)] = filteredVertices.diff(vertices)
    val diffedVertices:VertexRDD[(String, Long)] = graph.vertices.diff(filteredVertices)

    // diffedVertices.collect.foreach(println(_))
    println("vertices : " + graph.vertices.count)
    println("filteredVertices : " + filteredVertices.count)
    println("diffedVertices : " + diffedVertices.count)

//    vertices : 3
//    filteredVertices : 2
//    diffedVertices : 0

    // 0L until 2L 生成 Range(0, 1)，再使用VertexRDD的map来修改顶点的属性
    val setA: VertexRDD[Int] = VertexRDD(sc.parallelize(0L until 2L).map(id => (id, id.toInt)))
    println("\n\n~~~~~~~~~ set A ")
    setA.collect.foreach(println(_))
    // (0,0)
    // (1,1)
    val setB: VertexRDD[Int] = VertexRDD(sc.parallelize(1L until 3L).map(id => (id, id.toInt)))
    println("\n\n~~~~~~~~~ set B ")
    setB.collect.foreach(println(_))
    // (2,2)
    // (1,1)
    val diff = setA.diff(setB)
    println("\n\n~~~~~~~~~ diff ")
    diff.collect.foreach(println(_))
    // diff输出为空？？这里是否使用有误？？

    sc.stop



//    def kankan[A: ClassTag](Func: A => A): Unit ={
//        Func
//        println("kankan")
//    }
//
//    val s = (x:Int)=> x + x

 //   kankan{s}
  }

}
