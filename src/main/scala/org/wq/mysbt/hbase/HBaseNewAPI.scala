package org.wq.mysbt.hbase

import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, TableName, HBaseConfiguration}
import org.apache.hadoop.hbase.client._
import org.apache.spark.SparkContext
import scala.collection.JavaConversions._

/**
  * Created by wq on 2016/10/7.
  */
object HBaseNewAPI {
  def main(args: Array[String]) {
    val sc = new SparkContext("local", "SparkHBase")
    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set("hbase.zookeeper.quorum", "master")


    val conn = ConnectionFactory.createConnection(conf)

    val admin = conn.getAdmin

    val userTable = TableName.valueOf("user")

    val tableDescr = new HTableDescriptor(userTable)
    tableDescr.addFamily(new HColumnDescriptor("basic".getBytes))
    println("Creating table `user`. ")
    if (admin.tableExists(userTable)) {
      admin.disableTable(userTable)
      admin.deleteTable(userTable)
    }
    admin.createTable(tableDescr)
    println("Done!")

    try{
      val table = conn.getTable(userTable)

      try{
        val p = new Put("id001".getBytes)
        p.addColumn("basic".getBytes,"name".getBytes, "wuchong".getBytes)
        table.put(p)

        val g = new Get("id001".getBytes)
        val result = table.get(g)
        val value = Bytes.toString(result.getValue("basic".getBytes,"name".getBytes))
        println("GET id001 :"+value)

        val s = new Scan()
        s.addColumn("basic".getBytes,"name".getBytes)
        val scanner = table.getScanner(s)

        try{
          for(r <- scanner){
            println("Found row: "+r)
            println("Found value: "+Bytes.toString(r.getValue("basic".getBytes,"name".getBytes)))
          }
        }finally {
          scanner.close()
        }

        val d = new Delete("id001".getBytes)
        d.addColumn("basic".getBytes,"name".getBytes)
        table.delete(d)

      }finally {
        if(table != null) table.close()
      }

    }finally {
      conn.close()
    }
  }
}
