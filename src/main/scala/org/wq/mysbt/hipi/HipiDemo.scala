package org.wq.mysbt.hipi

import org.apache.hadoop.io.{NullWritable, IntWritable, Text, LongWritable}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.spark._
import org.apache.spark.SparkContext
import org.apache.spark.deploy.SparkHadoopUtil
import org.bytedeco.javacpp.opencv_core
import org.hipi.image.{FloatImage, HipiImage, HipiImageHeader}
import org.hipi.imagebundle.mapreduce.HibInputFormat
import org.hipi.mapreduce.BinaryOutputFormat
import org.hipi.opencv.OpenCVMatWritable
import org.bytedeco.javacpp.opencv_core.Mat
import org.wq.inputformat.ETLLineInputFormat


/**
  * Created by wq on 16/8/9.
  */
object HipiDemo {

  def main(args: Array[String]) {
    //println(args(0))
    //println(args(1))
    val conf = new SparkConf()
    val sc = new SparkContext("local", "HipiDemo", conf)

    val hconf = sc.hadoopConfiguration
    hconf.set("fs.defaultFS", "hdfs://honest:8020")
    hconf.set("imagetype","FloatImage")

    println("11111111")
    val aa = sc.newAPIHadoopFile[HipiImageHeader,HipiImage,HibInputFormat]("hdfs://honest:8020/hipi/sampleimages.hib")
    println("2222222");
    val cc = aa.map{
      case (x: HipiImageHeader,y: FloatImage)=>
        //val im = y
        println("start image")
        println("hei:"+y.getHeight)
        val cvImage: Mat  = new Mat(y.getHeight, y.getWidth, opencv_core.CV_32FC1)

        (NullWritable.get, new OpenCVMatWritable(cvImage))

      case _ => println("mmmmmmmmmm");("","")
    }


    cc.saveAsNewAPIHadoopFile("hdfs://honest:8020/dw4",
      classOf[NullWritable],classOf[OpenCVMatWritable],classOf[BinaryOutputFormat[NullWritable, OpenCVMatWritable]])

    val aaa = sc.newAPIHadoopFile[LongWritable, Text, ETLLineInputFormat]("hdfs://honest:8020/data")

    aaa.map[(Int,Int)] {
      case (a: LongWritable, b: Text) => (1, 1)
    }

    val kk = sc.textFile("")
    val nn = kk.map{
      //case (a,b) => (1,1)
      case a => (1,a)
    }
    nn.map{
      a => (1,a._2)
    }

  }

}
