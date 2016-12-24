package org.wq.mysbt.elasticsearch

import com.sksamuel.elastic4s.mappings.FieldType.{IntegerType, StringType}
import com.sksamuel.elastic4s.source.StringDocumentSource
import com.sksamuel.elastic4s._


import scala.concurrent.duration._
import ElasticDsl._

/**
  * Created by wq on 16/6/4.
  */


object ElasticTest extends App{

  //val client = ElasticClient.local

  val client = ElasticClient.remote("honest", 9300)

  //create index
  client.execute {
    create index "places" mappings (
      "cities" as (
        "id" typed IntegerType,
        "name" typed StringType boost 4,
        "content" typed StringType analyzer StopAnalyzer
        )
      )
  }


  def writeToElasticsearch(bulkList: List[EventMessage]) {
    val ops = for (message <- bulkList) yield {
      index into "places" ttl 7.days.toMillis doc StringDocumentSource(message.toJSon())
    }
    client.execute(bulk(ops: _*)).await
  }

  trait EventMessage {
    def toJSon(): String
  }
}
