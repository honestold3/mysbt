package org.wq.mysbt.basicGraphX

import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by wq on 16/6/12.
  */
object WholePathDemoV2 {

  def main(args: Array[String]) {
    val conf = new SparkConf()
    val sc = new SparkContext("local", "WholePathDemoV2", conf)

    //创建点RDD
    val vertex: RDD[(VertexId, List[String])] = sc.parallelize(Array(
      (3L, List("3")), (7L, List("7")),(4L,List("4")),
      (5L, List("5")), (2L, List("2"))
    )
    )
    //创建边RDD
    val edge: RDD[Edge[List[String]]] = sc.parallelize(Array(
      Edge(2L, 3L, List("a")), Edge(3L, 7L, List("b")),Edge(2L,5L,List("e")),Edge(3L,5L,List("f")),//Edge(5L,7L,List("g")),
      Edge(2L, 4L, List("c")), Edge(4L, 7L, List("d"))
    )
    )

    //构造Graph
    val g = Graph(vertex, edge)

    val start = "2"

    def vp(id: VertexId, attr: List[String], message: List[String]): List[String] = {
      println("attr:"+attr)
      println("message:"+message)
      var ss = ""
      if (attr.length>1&&attr(0)=="x") ss = attr(1)

      //message :+ ss
      if(message.length==1&&message(0)=="x"){
        attr
      }else {
        println("kankan:"+changeList(message,attr,start).distinct)
        changeList(message,attr,start).distinct
        //message ++ attr
      }

    }

    def sm(triple: EdgeTriplet[List[String],List[String]]): Iterator[(VertexId,List[String])] = {
      println("srcAttr:"+triple.srcAttr+";dstAttr:"+triple.dstAttr+";edge attr:"+triple.attr)
      //if(triple.srcAttr.head.toString.contains(start)){
      if(triple.srcAttr.filter(_.contains(start))!=Nil){
        if(triple.srcAttr.filter(_.contains("->"))!=Nil){
          Iterator((triple.dstId, triple.srcAttr.map(_+"->"+triple.dstId.toString)))
        } else {
          Iterator((triple.dstId, triple.srcAttr))
        }
      } else {
        Iterator.empty
      }
    }

    def mm(v1: List[String],v2: List[String]): List[String] = {
      println("v1:"+v1)
      println("v2:"+v2)

      v1++v2
    }

    def rm(list: List[String],str: String) = {
      list.filter(!_.contains(str))
    }

    def changeList(message: List[String], attr: List[String],ss: String) = {
      if(attr.filter(_.contains("->"))!=Nil){
        message ++ attr
      } else {
        val a = attr.map{x=>
          val s = x.split("->")
          var str = ""
          //for(i<-s if i!=ss) yield if(i==s.reverse.head) str+=i+"" else str+=i+"->"
          for(i<-s) yield if(i==s.reverse.head) str+=i+"" else str+=i+"->"
          str
        }
        println("a:"+a)
        println("a size:"+a(0).size)

        if(message.filter(_.contains(a(0)))==Nil){
          message.map{x=> x+"->"+a(0)}
        } else {
          message
        }
      }

    }

    val kk = g.pregel(List("x"),Int.MaxValue,EdgeDirection.Out)(vp,sm,mm)

    println(kk.vertices.collect.mkString("\n"))

    sc.stop
  }

}
