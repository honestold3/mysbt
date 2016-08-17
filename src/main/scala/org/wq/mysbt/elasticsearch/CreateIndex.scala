package org.wq.mysbt.elasticsearch

import com.sksamuel.elastic4s.ElasticDsl.create
import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s._
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse

import scala.concurrent.Future

/**
  * Created by wq on 16/6/5.
  */
class CreateIndex extends ElasticDsl{

  val client = ElasticClient.remote("127.0.0.1", 9300)

  def main(args: Array[String]) {
    createIndexWithMappings
  }


  def createIndexWithMappings(): Future[CreateIndexResponse] = {

    client.execute {
      create index "images" shards 2 replicas 1 mappings (
        "exif" as (
          field("taken") typed DateType,
          field("filename") typed StringType,
          field("path") typed StringType,
          field("location") typed GeoPointType,
          field("focalLength") typed DoubleType analyzer StopAnalyzer
          )
        )
    }
  }


}
