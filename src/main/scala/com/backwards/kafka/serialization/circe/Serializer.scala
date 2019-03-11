package com.backwards.kafka.serialization.circe

import io.circe.Encoder
import monix.kafka.{Serializer => MonixSerializer}
import org.apache.kafka.common.serialization.{Serializer => ApacheSerializer}
import com.backwards.json.{Serializer => JsonSerializer}
import com.backwards.kafka.serialization.{DefaultKafkaSerializer, DefaultMonixSerializer}

class Serializer[T <: Product: Encoder] extends DefaultKafkaSerializer[T] {
  val serializer: JsonSerializer[T] =
    JsonSerializer[T]

  override def serialize(topic: String, data: T): Array[Byte] =
    serializer serialize data
}

object Serializer extends DefaultMonixSerializer {
  def apply[T <: Product: Encoder]: ApacheSerializer[T] =
    serializer[T].create()

  implicit def serializer[T <: Product: Encoder]: MonixSerializer[T] =
    monixSerializer(new Serializer[T])
}