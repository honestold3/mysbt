package org.wq.mysbt.hbase

import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.protobuf.ProtobufUtil
import org.apache.hadoop.hbase.util.{Base64, Bytes}
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.SparkContext
import org.apache.hadoop.hbase.client._

/**
  * Created by wq on 2016/10/7.
  * Spark 读取和写入 HBase
  **/
object SparkOnHBase {


  def convertScanToString(scan: Scan) = {
    val proto = ProtobufUtil.toScan(scan)
    Base64.encodeBytes(proto.toByteArray)
  }

  def main(args: Array[String]) {
    val sc = new SparkContext("local","SparkOnHBase")

    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set("hbase.zookeeper.quorum", "master")


    // ======Save RDD to HBase========
    // step 1: JobConf setup
    val jobConf = new JobConf(conf,this.getClass)
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    jobConf.set(TableOutputFormat.OUTPUT_TABLE,"user")


    // step 2: rdd mapping to table

    // 在 HBase 中表的 schema 一般是这样的
    // *row   cf:col_1    cf:col_2
    // 而在Spark中，我们操作的是RDD元组，比如(1,"lilei",14) , (2,"hanmei",18)
    // 我们需要将 *RDD[(uid:Int, name:String, age:Int)]* 转换成 *RDD[(ImmutableBytesWritable, Put)]*
    // 我们定义了 convert 函数做这个转换工作
    def convert(triple: (Int, String, Int)) = {
      val p = new Put(Bytes.toBytes(triple._1))
      p.addColumn(Bytes.toBytes("basic"),Bytes.toBytes("name"),Bytes.toBytes(triple._2))
      p.addColumn(Bytes.toBytes("basic"),Bytes.toBytes("age"),Bytes.toBytes(triple._3))
      (new ImmutableBytesWritable, p)
    }

    // step 3: read RDD data from somewhere and convert
    val rawData = List((1,"lilei",14), (2,"hanmei",18), (3,"someone",38))
    val localData = sc.parallelize(rawData).map(convert)

    //step 4: use `saveAsHadoopDataset` to save RDD to HBase
    localData.saveAsHadoopDataset(jobConf)
    // =================================


    // ======Load RDD from HBase========
    // use `newAPIHadoopRDD` to load RDD from HBase
    //直接从 HBase 中读取数据并转成 Spark 能直接操作的 RDD[K,V]

    //设置查询的表名
    conf.set(TableInputFormat.INPUT_TABLE, "user")

    //添加过滤条件，年龄大于 18 岁
    val scan = new Scan()
    scan.setFilter(new SingleColumnValueFilter("basic".getBytes,"age".getBytes,
      CompareOp.GREATER_OR_EQUAL,Bytes.toBytes(18)))
    conf.set(TableInputFormat.SCAN,convertScanToString(scan))

    val usersRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    val count = usersRDD.count()
    println("Users RDD Count:" + count)
    usersRDD.cache()

    //遍历输出
    usersRDD.foreach{ case (_,result) =>
      val key = Bytes.toInt(result.getRow)
      val name = Bytes.toString(result.getValue("basic".getBytes,"name".getBytes))
      val age = Bytes.toInt(result.getValue("basic".getBytes,"age".getBytes))
      println("Row key:"+key+" Name:"+name+" Age:"+age)
    }
    // =================================
  }
}
