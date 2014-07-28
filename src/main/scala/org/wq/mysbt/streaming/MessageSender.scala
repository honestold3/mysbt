package org.wq.mysbt.streaming

import scala.collection.mutable.ListBuffer
import scala.util.Random
import java.net.ServerSocket
import java.io.PrintWriter

/**
 * Created by wq on 14-7-27.
 */
object MessageSender {

  def gen(index: Int): String = {
    val charList = ListBuffer[Char]()
    for(i <- 65 to 90) {
      charList += i.toChar
    }
    val charArray = charList.toArray
    charArray(index).toString
  }

  def index = {
    val rdm = new Random
    rdm.nextInt(7)
  }

  def main(args: Array[String]){
    if(args.length != 2){
      System.out.println("Usage: <port> <millisecond>")
      System.exit(1)
    }

    val listener = new ServerSocket(args(0).toInt)
    while(true){
      val socket = listener.accept()
      new Thread(){
        override def run() ={
          println("Got client connected from: "+socket.getInetAddress)
          val out = new PrintWriter(socket.getOutputStream(), true)
          while(true){
            Thread.sleep(args(1).toLong)
            val content = gen(index)
            println(content)
            out.write(content + '\n')
            out.flush()
          }
          socket.close()
        }
      }.start()
    }
  }

}
