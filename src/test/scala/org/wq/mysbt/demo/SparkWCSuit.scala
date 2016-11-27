package org.wq.mysbt.demo

import org.apache.spark.sql.{Row, SQLContext}
import org.scalatest.FunSuite
import org.wq.mysbt.tools.LocalSparkContext

/**
  * Created by wq on 2016/11/27.
  */
class SparkWCSuit extends FunSuite
  with LocalSparkContext {

  //rdd wordCount
  test("test rdd wc") {
    sc.setLogLevel("ERROR")
    val rdd = sc.makeRDD(Seq("a", "b", "b"))
    val res = rdd.map((_, 1)).reduceByKey(_ + _).collect().sorted
    assert(res === Array(("a", 1), ("b", 2)))
  }

  //df wordCount
  test("test df wc") {
    val sqlContext = SQLContext.getOrCreate(sc)
    import sqlContext.implicits._
    val df = sc.makeRDD(Seq("a", "b", "b")).toDF("word")
    val res = df.groupBy("word").count().collect()
    assert(res === Array(Row("a",1),Row("b",2)))
  }
}
