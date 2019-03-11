package com.backwards.kafka.serialization.avro.data

import monix.kafka.{Deserializer => MonixDeserializer}
import org.apache.avro.Schema
import org.apache.kafka.common.serialization.{Deserializer => KafkaDeserializer}
import com.backwards.avro.data.{Deserializer => AvroDeserializer}
import com.backwards.kafka.serialization.{DefaultKafkaDeserializer, DefaultMonixDeserializer}
import com.sksamuel.avro4s.{AvroSchema, Decoder, SchemaFor}

class Deserializer[T <: Product: SchemaFor: Decoder] extends DefaultKafkaDeserializer[T] {
  val schema: Schema = AvroSchema[T]

  val deserializer: AvroDeserializer[T] = AvroDeserializer[T]

  override def deserialize(topic: String, data: Array[Byte]): T =
    deserializer deserialize data
}

object Deserializer extends DefaultMonixDeserializer {
  def apply[T <: Product: SchemaFor: Decoder]: KafkaDeserializer[T] =
    deserializer[T].create()

  implicit def deserializer[T <: Product: SchemaFor: Decoder]: MonixDeserializer[T] =
    monixDeserializer(new Deserializer[T])
}