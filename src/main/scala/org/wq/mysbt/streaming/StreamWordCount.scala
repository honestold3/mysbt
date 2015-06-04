package org.wq.mysbt.streaming

import org.apache.spark.SparkContext.IntAccumulatorParam
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{Accumulator, SparkContext, SparkConf}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql._

/**
 * Created by wq on 14-7-20.
 */
object StreamWordCount {
  def main(args: Array[String]) {
    if(args.length < 3){
      System.err.println("Usage: StreamWordCount <hostname> <port> <second> \n" +
        "In local mode, <master> should be 'local[n]' with n > 1")
      System.exit(1)
    }

    val conf = new SparkConf()
    conf.setMaster("spark://honest:7077")
      //conf.setMaster("spark://cloud38:7077")
    //conf.setMaster(args(3))
      .setAppName("StreamWordCount")
      .set("spark.executor.memory","1g")
      //.setSparkHome("/Users/wq/opt/spark-1.0.1-bin-hadoop2")
      //.setSparkHome("/Users/wq/opt/spark-1.0.0-bin-hadoop2")
      .setJars(List(SparkContext.jarOfClass(this.getClass).getOrElse("")))
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc,Seconds(args(2).toInt))
    val sqlsc = new HiveContext(sc)
    //val sqlContext = new org.apache.spark.sql.hive.HiveContext(sc)

    //val sc = ssc.sparkContext
    val accum:Accumulator[Int] = sc.accumulator(0,"kankan counter")(IntAccumulatorParam)

    val lines = ssc.socketTextStream(args(0), args(1).toInt, StorageLevel.DISK_ONLY)
    //val lines = ssc.socketTextStream(args(0), args(1).toInt)
    val words = lines.flatMap(_.split(" "))
    words.foreachRDD{rdd =>
      val sqlContext = SQLContextSingleton.getInstance(rdd.sparkContext)
      import sqlContext.implicits._
      sqlContext.sql("select count(*) from person").foreach(x =>println("kankan11:"+x))
    }
    val wordCounts = words.map{x => accum +=1; (x, 1)}.reduceByKey(_ + _)

    println("kkkkk:"+accum.value)
    //sc.parallelize(accum.value+"").saveAsTextFile("hdfs://master2:8020/user/tescomm/bao/kankan/abc/")

    //wordCounts.saveAsTextFiles("hdfs://honest:8020/streaming/")
    wordCounts.print()
    sqlsc.sql("select count(*) from person").foreach(x =>println("kankan:"+x))
    ssc.start()
    ssc.awaitTermination()
  }
}

object SQLContextSingleton {
  @transient private var instance: SQLContext = null

  // Instantiate SQLContext on demand
  def getInstance(sparkContext: SparkContext): SQLContext = synchronized {
    if (instance == null) {
      instance = new HiveContext(sparkContext)
    }
    instance
  }
}
