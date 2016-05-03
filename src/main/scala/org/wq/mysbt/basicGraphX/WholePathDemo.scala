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
object WholePathDemo {

  def main(args: Array[String]) {
    val conf = new SparkConf()
    val sc = new SparkContext("local", "WholePathDemo", conf)

    val graph = GraphLoader.edgeListFile(sc, "hdfs://honest:8020/graphdata/edges.txt").cache()

    val sourceId: VertexId = 0

    val initialGraph = graph.mapVertices {
      (id, _) => if (id == sourceId) (0.0, List[VertexId](sourceId)) else (Double.PositiveInfinity, List[VertexId]())
    }


    def vertexProgram(id: VertexId, attr: Int, msg: List[String]): Int = {
      attr
    }

    def sendMessage(triple: EdgeTriplet[Int,Int]): Iterator[(VertexId,List[String])] = {
      Iterator((triple.dstId, List("")))
    }

    def MergeMessage(v1: List[String], v2: List[String]): List[String] = {
      v2
    }

    //graph.pregel(List[String]())(vertexProgram,sendMessage,MergeMessage)


    //创建点RDD
    val vertex: RDD[(VertexId, List[String])] = sc.parallelize(Array(
        (3L, List("3")), (7L, List("7")),
        (5L, List("5")), (2L, List("2"))
      )
    )
    //创建边RDD
    val edge: RDD[Edge[List[String]]] = sc.parallelize(Array(
        Edge(3L, 7L, List("a")), //Edge(5L, 3L, List("b")),
        Edge(2L, 5L, List("c")), Edge(5L, 7L, List("d"))
      )
    )

    //构造Graph
    val g = Graph(vertex, edge)

    val start = "2"
    val end = "7"

    def vp(id: VertexId, attr: List[String], message: List[String]): List[String] = {
      println("attr:"+attr)
      println("message:"+message)
      var ss = ""
      if (attr.length>1&&attr(0)=="x") ss = attr(1)

      //message :+ ss
      if(message.length==1&&message(0)=="x"){
        attr
      }else {
        message ++ attr:+"->"
      }
//      if(attr.length ==1){
//        message++attr
//      } else {
//        message:+attr.tail(1)
//      }
//      val str = new StringBuilder
//      val list = List()
//      val kan = attr(0).toString
//      println("kan:"+kan)
//      if(kan!=end){
//        println("llll")
//        if(message.length==1&&message(0)=="x"){
//          attr
//        }else {
//          message.foreach{
//              x => //str.append(x+"->"+attr)
//              //list.addString(str)
//              //x:+attr
//              println("kankan:"+x)
//              println("kankan1:"+attr)
//
//          }
//          //list
//          message++attr
//        }
//      } else {
//        println("hhhh")
//        message++attr
//      }

    }

    def sm(triple: EdgeTriplet[List[String],List[String]]): Iterator[(VertexId,List[String])] = {
      println("srcAttr:"+triple.srcAttr+";dstAttr:"+triple.dstAttr+";edge attr:"+triple.attr)
      //println("sm:"+List(triple.srcId.toString):+triple.dstId.toString)
      if(triple.srcAttr.head.toString.contains(start)){
//        if(triple.dstAttr.toString.contains(end)){
//          Iterator.empty
//        } else {
//          Iterator((triple.dstId, triple.srcAttr))
//        }
        //Iterator((triple.dstId, List(triple.srcId.toString):+triple.dstId.toString))
        Iterator((triple.dstId, triple.srcAttr))
      } else {
        Iterator.empty
      }
    }

    def mm(v1: List[String],v2: List[String]): List[String] = {
      v1++v2
    }

    val kk = g.pregel(List("x"),Int.MaxValue,EdgeDirection.Out)(vp,sm,mm)

    println(kk.vertices.collect.mkString("\n"))

    sc.stop
  }

}
