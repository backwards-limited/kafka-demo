package com.backwards.kafka.serialization.circe

import io.circe.Decoder
import monix.kafka.{Deserializer => MonixDeserializer}
import org.apache.kafka.common.serialization.{Deserializer => ApacheDeserializer}
import com.backwards.json.{Deserializer => JsonDeserializer}
import com.backwards.kafka.serialization.{DefaultKafkaDeserializer, DefaultMonixDeserializer}

class Deserializer[T <: Product: Decoder] extends DefaultKafkaDeserializer[T] {
  val deserializer: JsonDeserializer[T] =
    JsonDeserializer[T]

  override def deserialize(topic: String, data: Array[Byte]): T =
    deserializer deserialize data
}

object Deserializer extends DefaultMonixDeserializer {
  def apply[T <: Product: Decoder]: ApacheDeserializer[T] =
    deserializer[T].create()

  implicit def deserializer[T <: Product: Decoder]: MonixDeserializer[T] =
    monixDeserializer(new Deserializer[T])
}