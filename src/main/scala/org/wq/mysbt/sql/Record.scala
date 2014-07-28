package org.wq.mysbt.sql

/**
 * Created by wq on 14-7-21.
 */
class Record(val x1: String,
             val x2: String,
             val x3: String,
             val x4: String,
             val x5: String,
             val x6: String,
             val x7: String,
             val x8: String,
             val x9: String,
             val x10: String,
             val x11: String,
             val x12: String,
             val x13: String,
             val x14: String,
             val x15: String,
             val x16: String,
             val x17: String,
             val x18: String,
             val x19: String,
             val x20: String,
             val x21: String,
             val x22: String,
             val x23: String,
             val x24: String) extends Product with Serializable {

  def canEqual(that: Any) = that.isInstanceOf[Record]

  def productArity = 24


  def productElement(n: Int) = n match {
    case 0 => x1
    case 1 => x2
    case 2 => x3
    case 3 => x4
    case 4 => x5
    case 5 => x6
    case 6 => x7
    case 7 => x8
    case 8 => x9
    case 9 => x10
    case 10 => x11
    case 11 => x12
    case 12 => x13
    case 13 => x14
    case 14 => x15
    case 15 => x16
    case 16 => x17
    case 17 => x18
    case 18 => x19
    case 19 => x20
    case 20 => x21
    case 21 => x22
    case 22 => x23
    case 23 => x24
  }
}
