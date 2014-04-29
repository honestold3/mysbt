package org.wq.mysbt.demo

import org.scalatest.FunSuite

/**
 * Created by wq on 14-4-26.
 */
class HelloTest extends FunSuite{

  test("hehe"){
    val hello = new Hello
    assert(hello.sayHello("scala") == "hello,scala")
  }

  //(new HelloTest).execute

}


