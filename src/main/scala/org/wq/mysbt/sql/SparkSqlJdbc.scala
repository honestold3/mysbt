package org.wq.mysbt.sql

import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.sql.Connection

/**
 * Created by wq on 15/3/7.
 */
object SparkSqlJdbc {

  def main(args: Array[String]) {
    Class.forName("org.apache.hive.jdbc.HiveDriver")
    var con: Connection = null
    var rs: ResultSet = null
    var statement: Statement = null
    try {
      con = DriverManager.getConnection("jdbc:hive2://cloud83:10000", "hive", "hive")
      statement = con.createStatement
      rs = statement.executeQuery("select name,age from person2 limit 2")
      while (rs.next) {
        val name = rs.getString("name")
        val age = rs.getString("age")
        println("name=%s, age=%s".format(name, age))
      }
    } catch {
      case e: Exception => e.printStackTrace
    } finally {
      rs.close()
      statement.close()
      con.close()
    }

  }

}
