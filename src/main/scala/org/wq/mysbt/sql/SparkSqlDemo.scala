package org.wq.mysbt.sql

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkContext._


/**
 * Created by wq on 14-7-20.
 */

case class Person(name: String, age: Int)

object SparkSqlDemo {

  def main(args: Array[String]) {
    //val sc = new SparkContext("local","sqldemo",System.getenv("SPARK_HOME"))
    val sparkConf = new SparkConf()
      .setAppName("SparkSql")
      .setMaster("spark://honest:8888")
      //.setSparkHome("/Users/wq/opt/spark-1.0.0-bin-hadoop2")
      .setSparkHome("/Users/wq/opt/spark-1.0.1-bin-hadoop2")
      .set("spark.executor.memory","2g")
      .setJars(List(SparkContext.jarOfClass(this.getClass).getOrElse("")))
    //.setJars(SparkContext.jarOfClass(this.getClass))
    val sc = new SparkContext(sparkConf)

    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    import sqlContext.createSchemaRDD

    val people =sc.textFile("hdfs://honest:8020/sql/sqldata").map(_.split(" ")).map(p => Person(p(0),p(1).trim.toInt))

    people.registerAsTable("people")

    sqlContext.cacheTable("people")
    val kankan =sqlContext.sql("SELECT name FROM people WHERE age >= 20 AND age <= 30")

    kankan.map(t => "Name: " +t(0)).collect().foreach(println)

    //----------------------------------------------------------------------------------

    val record = new Record("01", "02", "03", "04", "05", "06", "07", "08", "09",
      "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24")

    sc.parallelize(record :: Nil).registerAsTable("records")

    sqlContext.sql("SELECT * FROM records").collect().foreach(println)
  }

}
