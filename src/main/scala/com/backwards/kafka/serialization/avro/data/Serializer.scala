package com.backwards.kafka.serialization.avro.data

import monix.kafka.{Serializer => MonixSerializer}
import org.apache.avro.Schema
import org.apache.kafka.common.serialization.{Serializer => ApacheSerializer}
import com.backwards.avro.data.{Serializer => AvroSerializer}
import com.backwards.kafka.serialization.{DefaultKafkaSerializer, DefaultMonixSerializer}
import com.sksamuel.avro4s.{AvroSchema, Encoder, SchemaFor}

class Serializer[T <: Product: SchemaFor: Encoder] extends DefaultKafkaSerializer[T] {
  val schema: Schema = AvroSchema[T]

  val serializer: AvroSerializer[T] = AvroSerializer[T]

  override def serialize(topic: String, data: T): Array[Byte] =
    serializer serialize data
}

object Serializer extends DefaultMonixSerializer {
  def apply[T <: Product: SchemaFor: Encoder]: ApacheSerializer[T] = serializer[T].create()

  implicit def serializer[T <: Product: SchemaFor: Encoder]: MonixSerializer[T] = monixSerializer(new Serializer[T])
}