package org.wq.mysbt.streaming

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.StreamingContext._
/**
 * Created by wq on 14-7-27.
 */
object StatefulStreamWordCount {
  def main(args: Array[String]) {
    if (args.length < 3) {
      System.err.println("Usage: StatefulStreamWordCount <hostname> <port> <seconds>\n" +
        "In local mode, <master> should be 'local[n]' with n > 1")
      System.exit(1)
    }

    val conf = new SparkConf()
    conf.setMaster("spark://honest:7077")
      .setAppName("StatefulStreamWordCount")
      .set("spark.executor.memory","1g")
      .set("spark.streaming.concurrentJobs","1")
      //.setSparkHome("/Users/wq/opt/spark-1.0.1-bin-hadoop2")
      //.setSparkHome("/Users/wq/opt/spark-1.0.0-bin-hadoop2")
      .setJars(List(SparkContext.jarOfClass(this.getClass).getOrElse("")))


    val updateFunc = (values: Seq[Int], state: Option[Int]) => {
      //val currentCount = values.foldLeft(0)(_ + _)
      val currentCount = (0/:values)(_ + _)
      val previousCount = state.getOrElse(0)
      Some(currentCount + previousCount)
    }

    //创建StreamingContext
    val ssc = new StreamingContext(conf,Seconds(args(2).toInt))
    //ssc.checkpoint("/Users/wq/spark")
    ssc.checkpoint(".")
    //创建NetworkInputDStream，需要指定ip和端口
    val lines = ssc.socketTextStream(args(0), args(1).toInt)
    val words = lines.flatMap(_.split(" "))
    //val wordDstream = words.map{x =>  Thread.sleep(30000);(x, 1)}
    val wordDstream = words.map{x => (x, 1)}

    //使用updateStateByKey来更新状态
    val ll = Option(2)
    val list = List(1,2)
    val stateDstream = wordDstream.updateStateByKey[Int](updateFunc)
    stateDstream.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
