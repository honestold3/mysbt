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
    if (args.length < 4) {
      System.err.println("Usage: StreamWordCountForWindow <master> <hostname> <port> <interval> " +
        "<windowLength> <slideInterval>\n" +
        "In local mode, <master> should be 'local[n]' with n > 1")
      System.exit(1)
    }

    val conf = new SparkConf()
    conf.setMaster("spark://honest:8888")
      .setAppName("HdfsWordCount")
      .set("spark.executor.memory","1g")
      //.setSparkHome("/Users/wq/opt/spark-1.0.1-bin-hadoop2")
      .setSparkHome("/Users/wq/opt/spark-1.0.0-bin-hadoop2")
      .setJars(List(SparkContext.jarOfClass(this.getClass).getOrElse("")))

    val ssc = new StreamingContext(conf,Seconds(args(1).toInt))
    ssc.checkpoint("/Users/wq/spark")

    val lines = ssc.socketTextStream(args(1), args(2).toInt, StorageLevel.MEMORY_ONLY_SER)
    val words = lines.flatMap(_.split(" "))

    //val wordCounts1 = words.map(x => (x , 1)).reduceByKeyAndWindow((x : Int, y : Int) => x + y, Seconds(10), Seconds(5))
    val wordCounts = words.map(x => (x , 1)).reduceByKeyAndWindow(_+_, _-_,Seconds((4).toInt), Seconds(args(5).toInt))

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
