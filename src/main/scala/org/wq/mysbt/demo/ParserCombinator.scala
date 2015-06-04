package org.wq.mysbt.demo

import scala.util.parsing.combinator.JavaTokenParsers

/**
 * Created by wq on 15/2/11.
 */
object ParserCombinator extends JavaTokenParsers {

  def main(args:Array[String]){

    val multiply:Parser[Double] = (floatingPointNumber ^^ {x=>println(x); x.toDouble}) ~ rep {
      ("*" | "/") ~ floatingPointNumber ^^ {
        case "*" ~ (factor: String) => left: Double => println("*"+factor);left * factor.toDouble
        case "/" ~ (factor: String) => left: Double => println("/"+factor);left / factor.toDouble

      }
    } ^^ {
      case (seed:Double) ~ (fnList:List[Double=>Double]) =>

        fnList.foldLeft(seed){(left, fn) => println("kankan:"+left);fn(left)}
    }

    val result = this.parseAll(multiply, "2*4/8*5")
    if(result.successful){
      println(result.get)
    }

  }
}
