package org.wq.mysbt.elasticsearch

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s._
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse
import ElasticDsl._


/**
  * Created by wq on 16/6/5.
  */

object Test extends App{
  //val client = ElasticClient.local
  val client = ElasticClient.remote("honest", 9300)

  // await is a helper method to make this operation sync instead of async
  // You would normally avoid doing this in a real program as it will block
  //client.execute { index into "bands/artists" fields "name"->"coldplay" }.await

  val resp = client.execute { search in "library/book" query "1" }.await
  println(resp)
}

