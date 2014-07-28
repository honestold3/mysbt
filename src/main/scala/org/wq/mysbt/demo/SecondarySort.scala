package org.wq.mysbt.demo

import org.apache.spark.SparkContext._
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.api.java.JavaRDD

/**
 * Created by wq on 14-5-9.
 */
object SecondarySort {

  def main(args: Array[String]){
    val sparkConf = new SparkConf()
      .setAppName("SparkTC")
      .setMaster("spark://honest:8888")
      .setSparkHome("/Users/wq/opt/spark-1.0.0-bin-hadoop2")
      .set("spark.executor.memory","1g")
      .setJars(List(SparkContext.jarOfClass(this.getClass).getOrElse("")))
    //.setJars(SparkContext.jarOfClass(this.getClass))
    //val sc = new SparkContext(sparkConf)
    val sc = new SparkContext("local","SecondarySort","/Users/wq/opt/spark-1.0.0-bin-hadoop2")

    val data = Array[(String,Int,Int)](
      ("x", 2, 9), ("y", 2, 5),("c", 3, 6),
      ("x", 1, 3), ("y", 1, 7),
      ("y", 3, 1), ("x", 3, 6),
      ("a", 3, 1), ("b", 3, 6)
    )

    val pairs = sc.parallelize(data,3).persist(StorageLevel.MEMORY_ONLY)
    val test = pairs.map(k => (k._1,(k._2,k._3)))
    val reducerNumber = 3


    val result = test.groupByKey(reducerNumber).map(K => (K._1, K._2.toSeq.sortBy(Value => Value._1)))
    //val result = test.groupByKey(reducerNumber).map(K => (K._1, K._2.toSeq.sortWith(_._1 > _._1)))
    //result.foreach(println)

    result.sortByKey().collect().map(println _)

  }

}
