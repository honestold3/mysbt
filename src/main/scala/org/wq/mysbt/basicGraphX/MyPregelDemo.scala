package org.wq.mysbt.basicGraphX

import org.apache.spark._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

/**
  * Created by wq on 16/4/19.
  */
object MyPregelDemo {

  def main(args: Array[String]) {
    val conf = new SparkConf()
    val sc = new SparkContext("local", "MyPregelDemo", conf)

//    val graph = GraphLoader.edgeListFile(sc, "hdfs://honest:8020/graphdata/pregel.txt").cache()
//
//    val g = graph.mapEdges(edge => 1.0)
//
//    val gg = g.mapVertices((vid: VertexId, attr: Int) => 1.0)

//    4,5
//    0,1
//    1,4
//    3,3
//    5,0
    val vertexLines: RDD[String] = sc.textFile("hdfs://honest:8020/graphdata/pregel-vertices.txt")
    val vertices: RDD[(VertexId, Long)] = vertexLines.map{
      line => {
        val cols = line.split(",")
        (cols(0).toLong, cols(1).toLong)
      }
    }

//    0,2,1.0
//    1,0,1.0
//    1,0,1.0
//    1,2,1.0
//    1,2,1.0
//    2,0,1.0
//    2,1,1.0
//    2,4,1.0
//    2,4,1.0
//    2,5,1.0
//    3,0,1.0
//    3,4,1.0
//    3,5,1.0
//    4,0,1.0
//    4,0,1.0
//    4,3,1.0
//    4,4,1.0
    val format = new java.text.SimpleDateFormat("yyyy/MM/dd")
    val edgeLines: RDD[String] = sc.textFile("hdfs://honest:8020/graphdata/pregel-edge.txt")
    val edges:RDD[Edge[Double]] = edgeLines.map(line => {
      val cols = line.split(",")
      Edge(cols(0).toLong, cols(1).toLong, cols(2).toDouble)
    })

    val graph = Graph(vertices, edges)

    graph.edges.foreach(println)

    /*
    val sourceId: VertexId = 0 // The ultimate source
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

    val sourceId: VertexId = 0 // The ultimate source

    // Initialize the graph such that all vertices except the root have distance infinity.
    val initialGraph : Graph[(Double, List[VertexId]), Double] = graph.mapVertices {
      (id, _) => if (id == sourceId) (0.0, List[VertexId](sourceId)) else (Double.PositiveInfinity, List[VertexId]())
    }

    println("-----------------------------kankan---------------------------")
    initialGraph.triplets.collect().foreach(println)

    def vertexProgram: (VertexId, (Double, List[VertexId]), (Double, List[VertexId])) => (Double, List[VertexId]) = {
      (id, dist, newDist) => if (dist._1 < newDist._1) dist else newDist
    }
    def SendMessage: (EdgeTriplet[(Double, List[VertexId]), Double]) => Iterator[(VertexId, (Double, List[VertexId]))] = {
      triplet => {
        if (triplet.srcAttr._1 + triplet.attr < triplet.dstAttr._1) {
          Iterator((triplet.dstId, (triplet.srcAttr._1 + triplet.attr, triplet.srcAttr._2 :+ triplet.dstId)))
        } else {
          Iterator.empty
        }
      }
    }
    def MergeMessage: ((Double, List[VertexId]), (Double, List[VertexId])) => (Double, List[VertexId]) = {
      (a, b) => if (a._1 < b._1) a else b
    }
    //val sssp = initialGraph.pregel((Double.PositiveInfinity, List[VertexId]()), Int.MaxValue, EdgeDirection.Out)(
    val sssp = initialGraph.pregel((Double.PositiveInfinity,List[VertexId]()))(
      // Vertex Program
      vertexProgram,
      // Send Message
      SendMessage,
      //Merge Message
      MergeMessage
    )
    println(sssp.vertices.collect.mkString("\n"))

    sc.stop()
  }
}
