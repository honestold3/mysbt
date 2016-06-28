package org.wq.mysbt.streaming

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.storage.StorageLevel
import org.apache.spark.SparkContext._

/**
 * Created by wq on 14-7-27.
 */
object StreamWordCountForWindow {
  def main(args: Array[String]) {
    if (args.length < 3) {
      System.err.println("Usage: StreamWordCountForWindow <hostname> <port> <interval> " +
        "<windowLength> <slideInterval>\n" +
        "In local mode, <master> should be 'local[n]' with n > 1")
      System.exit(1)
    }

    val conf = new SparkConf()
    //conf.setMaster("spark://cloud38:7077")
      conf.setMaster("spark://honest:7077")
      .setAppName("StreamWordCountForWindow")
      .set("spark.executor.memory","1g")
      //.setSparkHome("/Users/wq/opt/spark-1.0.1-bin-hadoop2")
      //.setSparkHome("/Users/wq/opt/spark-1.0.0-bin-hadoop2")
      .setJars(List(SparkContext.jarOfClass(this.getClass).getOrElse("")))

    val ssc = new StreamingContext(conf,Seconds(args(2).toInt))
    //ssc.checkpoint("/Users/wq/spark")
    ssc.checkpoint(".")
    //ssc.checkpoint("hdfs://cloud83:8020/user/boco/checkpoint")

    val lines = ssc.socketTextStream(args(0), args(1).toInt, StorageLevel.MEMORY_ONLY_SER)
    val words = lines.flatMap(_.split(" "))

    //val wordCounts = words.map(x => (x , 1)).reduceByKeyAndWindow((x : Int, y : Int) => x + y, Seconds(10), Seconds(5))
    val wordCounts = words.map(x => (x , 1)).reduceByKeyAndWindow(_+_, _-_,Seconds(args(3).toInt), Seconds(args(4).toInt))

    val sortedWordCount = wordCounts.map{
      case (char,count) => (count,char)
    }.transform(_.sortByKey(false)).map{
      case (char,count) => (count,char)
    }

    sortedWordCount.print()
    ssc.start()

    ssc.awaitTermination()
  }
}
