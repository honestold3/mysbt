package org.wq.mysbt.demo

import org.apache.hadoop.io.{Text, LongWritable}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.spark.deploy.SparkHadoopUtil
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext._
import org.wq.inputformat.ETLLineInputFormat

import org.elasticsearch.spark._

/**
 * Created by wq on 14-6-30.
 */
object Sparkdemo {
  def main(args: Array[String]) {
    val sparkConf = new SparkConf()
      .setAppName("Sparkdemo")
      .setMaster("spark://honest:8888")
      //.setSparkHome("/Users/wq/opt/spark-1.0.0-bin-hadoop2")
      //.setSparkHome("/Users/wq/opt/spark-1.0.1-bin-hadoop2")
      .setSparkHome("/Users/wq/opt/spark-1.1.0-bin-hadoop2.4")
      .set("spark.executor.memory","1g")
      .setJars(List(SparkContext.jarOfClass(this.getClass).getOrElse("")))

      //.setJars(SparkContext.jarOfClass(this.getClass))
    //val sc = new SparkContext(sparkConf)
    val sc = new SparkContext("local","my hadoop file","/Users/wq/opt/spark-1.1.0-bin-hadoop2.4")
    sc.hadoopConfiguration.set("","")

    val file = List("2013-09-22 16:00:00\t2013-09-23 16:00:00\t111.175.243.101\t1710468975\t9\t1\t12\tr1.ykimg.com\t241\t27371\t14574\t0")
    val data = sc.parallelize(file)


    val List(head,tail @ _ *) = List(1,2,3,4)


    val list = List("1","2","3","4","5")
    val b = list.map(sc.textFile(_))


    val a = sc.parallelize(list).cache()

    val ddd = "eee"
    println(a.count())

//    val data = sc.textFile(args(0))
//    data.filter(_.split(' ').length==3).map(_.split(' ')(1)).map((_,1)).reduceByKey(_+_)
//      .map(x => (x._2,x._1)).sortByKey(false).map(x => (x._2,x._1)).map(_._2).saveAsTextFile(args(2))
    val wqlist = List(("11","11a"),("22","22b"))
    val kankan2 =  sc.parallelize(wqlist).cache()
    //kankan2.mapPartitionsWithSplit(){iter => iter.}


    kankan2.map{x=>(x._2,1)}.reduceByKey(_+_).collectAsMap()
//    val kk2 = kankan2.filter(x => x._1=="11").take(1).head._2
//    println("kk2:"+kk2)
//    println("kk2 of list:"+kk2.toList)

    val z = sc.parallelize(List("12","23","345","4567"),2)
    z.aggregate("")((x,y) => math.max(x.length, y.length).toString, (x,y) => x + y)
    z.aggregate("")((x,y) => math.min(x.length, y.length).toString, (x,y) => x + y)


    println("-------------------")
    val aa = sc.textFile("hdfs://honest:8020/data")
    println("kankan:"+aa.count)

    val aaa = sc.newAPIHadoopFile[LongWritable, Text, ETLLineInputFormat]("hdfs://honest:8020/data")
    println("kankan1:"+aaa.count)

    val aaaa = sc.newAPIHadoopFile("hdfs://honest:8020/data",classOf[ETLLineInputFormat],classOf[LongWritable],classOf[Text],SparkHadoopUtil.get.newConfiguration())
    println("kankan2:"+aaaa.count)
    aaaa.map(x=>x).count()
    aaaa.saveToEs("/data/kankan")
    println("end")

    val xmlFile =
      <symbols>
        <symbol ticker="AAPL">
          <units>200</units>
        </symbol>

        <units>300</units>

        <symbol ticker="IBM">
          <units>400</units>
        </symbol>
      </symbols>

    xmlFile match {
      case <symbols>{allSymbol @ _*}</symbols> =>
        for(symbolNode@ <symbol>{_*}</symbol> <- allSymbol){
          println(symbolNode \"@ticker")
          println(symbolNode \"units" text)
        }
    }
  }
}
