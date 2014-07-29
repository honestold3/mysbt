package org.wq.mysbt.streaming

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext._

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
    conf.setMaster("spark://honest:8888")
      .setAppName("StreamWordCount")
      .set("spark.executor.memory","1g")
      .setSparkHome("/Users/wq/opt/spark-1.0.1-bin-hadoop2")
      //.setSparkHome("/Users/wq/opt/spark-1.0.0-bin-hadoop2")
      .setJars(List(SparkContext.jarOfClass(this.getClass).getOrElse("")))
    val ssc = new StreamingContext(conf,Seconds(args(2).toInt))

    val lines = ssc.socketTextStream(args(0), args(1).toInt, StorageLevel.MEMORY_ONLY_SER)
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
    wordCounts.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
