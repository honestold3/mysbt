package org.wq.mysbt.demo

import scala.reflect.runtime.universe

/**
 * Created by wq on 14-9-25.
 */
object ReflectDemo extends App{

//  val runtimeMirror = ru.runtimeMirror(getClass.getClassLoader)
//  val module = runtimeMirror.staticModule("test")
//  val obj = runtimeMirror.reflectModule(module)
//  println(obj.instance)

  val s = "2013-09-22 16:00:00\t2013-09-23 16:00:00\t111.175.243.101\t1710468975\t9\t1\t12\tr1.ykimg.com\t241\t27371\t14574\t0"
  println(s.split("\\t").size)

  val ru = scala.reflect.runtime.universe
  println("ru:"+ru)
  val m = ru.runtimeMirror(getClass.getClassLoader)
  //m.runtimeClass(typeOf[X.Y].typeSymbol.asClass)
  println("m:"+m)
  val im = m.reflect(new Test)
  val method = ru.typeOf[Test].declaration(ru.newTermName("kankan")).asMethod
  val mm = im.reflectMethod(method)

  mm("heheh")

}


class Test{ def kankan(s: String) = println(s"kankan: $s") }
