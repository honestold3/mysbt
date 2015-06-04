package org.wq.mysbt.demo

import scala.util.parsing.combinator.JavaTokenParsers

/**
 * Created by wq on 15/2/11.
 */
class JsonParser extends JavaTokenParsers{

  //哈哈是来自JavaTokenParser的floatingPointNumber parser
  def jNum: Parser[Double] = floatingPointNumber ^^ (_.toDouble)

  //stringLiteral 也来自JavaTokenParser 这边解析出来的结果会是例如"string"的形式 我们需要把双引号给去掉
  def jStr: Parser[String] = stringLiteral ^^ (s => s.substring(1, s.length() - 1))

  //正则表达式会被隐式转换为Parser
  def jBool: Parser[Boolean] = "(true|false)".r ^^ (_.toBoolean)

  def jNull: Parser[Null] = "null".r ^^ (t => null)

  def term = jsonArray | jsonObject | jNum | jBool | jNull | jStr

  def jsonArray: Parser[List[Any]] = "[" ~> rep(term <~ ",?".r) <~ "]" ^^ (l => l)

  //String也会被隐式转化为Parser的形式
  def jsonObject: Parser[Map[String, Any]] =
    "{" ~> rep((ident ~ ":" ~ jNum |ident ~ ":" ~ jBool | ident ~ ":" ~ jNull | ident ~ ":" ~ jsonObject | ident ~ ":" ~ jsonArray | ident ~ ":" ~ jStr) <~ ",?".r) <~ "}" ^^ {
    os =>
      var map = Map[String, Any]()
      os.foreach(o =>
        o match {
          case k ~ ":" ~ v => map = map ++ Map(k -> v)
        })
      map

  }

}

object JsonParser extends JsonParser{
  def main(args: Array[String]) {
    val result = parseAll(jsonObject,"""{a:[1,2,"a",{name:"cc"}],b:1,c:"cc",d:null,e:true}""")
    println(result)
  }
}


