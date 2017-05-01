package org.wq.mysbt.demo

import com.databricks.spark.avro._
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.SparkContext._
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.SaveMode

/**
  * Created by wq on 2016/11/23.
  */
object SparkAvro {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("Sparkdemo")
      .setMaster("spark://honest:7077")
      .set("spark.executor.memory","1g")

    val sc = new SparkContext("local","avro demo")

    //val sc = new SparkContext(sparkConf)

    val sqlContext = new SQLContext(sc)

    import sqlContext.implicits._

    val df = Seq(
      (2012, 8, "Batman", 9.8),
      (2012, 8, "Hero", 8.7),
      (2012, 7, "Robot", 5.5),
      (2011, 7, "Git", 2.0)).toDF("year", "month", "title", "rating","c1")

    df.write.mode(SaveMode.Append).partitionBy("year", "month").avro("hdfs://honest:8020/avro")

    //val schema = sqlContext.read.avro("hdfs://honest:8020/avro")

    df.printSchema()
    df.show(10)
  }

}
