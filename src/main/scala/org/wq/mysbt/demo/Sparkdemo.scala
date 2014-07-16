package org.wq.mysbt.demo

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext._

/**
 * Created by wq on 14-6-30.
 */
object Sparkdemo {
  def main(args: Array[String]) {
    val sparkConf = new SparkConf()
      .setAppName("SparkTC")
      .setMaster("spark://honest:7077")
      .setSparkHome("/Users/wq/opt/spark-1.0.0-bin-hadoop2")
    val sc = new SparkContext(sparkConf)
    //val sc = new SparkContext("local","my hadoop file",System.getenv("SPARK_HOME"))
    val list = List("1","2","3","4","5")
    val a = sc.parallelize(list)

    println(a.count())

//    val wqlist = List(("11","11a"),("22","22b"))
//    val kankan2 =  sc.parallelize(wqlist).cache()
//    kankan2.map{x=>(x._2,1)}.reduceByKey(_+_)
//    val kk2 = kankan2.filter(x => x._1=="11").take(1).head._2
//    println("kk2:"+kk2)
//    println("kk2 of list:"+kk2.toList)
  }
}
