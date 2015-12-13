package org.wq.mysbt.scalademo

/**
  * Created by wq on 15/12/13.
  */
object QuickSort extends App{

  def sort(xs: Array[Int]): Array[Int]= {
    if(xs.length<=1){
      xs
    }else{
      val pivot = xs(xs.length/2)
      Array.concat(sort(xs.filter(pivot >)),xs.filter(pivot ==),sort(xs.filter(pivot <)))
    }
  }

  val arr = Array(4,6,9,2,8,3)
  sort(arr).map(println _)

}
