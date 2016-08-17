package org.wq.mysbt.sql


/**
 * Created by wq on 14-7-21.
 */

case class Person1(name: String, age: Int)

object TestCaseClass {

  def main(args: Array[String]) {
    val record = new Record("01", "02", "03", "04", "05", "06", "07", "08", "09",
      "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24")
    val record1 = new Record("a01", "a02", "03", "04", "05", "06", "07", "08", "09",
      "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24")

//    for(a <- List(record,record1)){
//      a match {
//        case Record(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24) =>
//          println("haha")
//      }
//    }

  }

}
