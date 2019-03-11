package com.backwards.kafka.serialization.avro

import monix.kafka.{Deserializer => MonixDeserializer}
import org.apache.avro.Schema
import org.apache.kafka.common.serialization.{Deserializer => KafkaDeserializer}
import com.backwards.avro.{Deserializer => AvroDeserializer}
import com.backwards.kafka.serialization.{DefaultKafkaDeserializer, DefaultMonixDeserializer}
import com.sksamuel.avro4s.{AvroSchema, Decoder, SchemaFor}

class Deserializer[T <: Product: SchemaFor: Decoder](deserializer: AvroDeserializer[T]) extends DefaultKafkaDeserializer[T] {
  val schema: Schema = AvroSchema[T]

  override def deserialize(topic: String, data: Array[Byte]): T =
    deserializer deserialize data
}

object Deserializer extends DefaultMonixDeserializer {
  object Data {
    def apply[T <: Product: SchemaFor: Decoder]: KafkaDeserializer[T] =
      deserializer[T].create()

    implicit def deserializer[T <: Product: SchemaFor: Decoder]: MonixDeserializer[T] =
      monixDeserializer(new Deserializer[T](AvroDeserializer.Data[T]))
  }

  object Binary {
    def apply[T <: Product: SchemaFor: Decoder]: KafkaDeserializer[T] =
      deserializer[T].create()

    implicit def deserializer[T <: Product: SchemaFor: Decoder]: MonixDeserializer[T] =
      monixDeserializer(new Deserializer[T](AvroDeserializer.Binary[T]))
  }

  object Json {
    def apply[T <: Product: SchemaFor: Decoder]: KafkaDeserializer[T] =
      deserializer[T].create()

    implicit def deserializer[T <: Product: SchemaFor: Decoder]: MonixDeserializer[T] =
      monixDeserializer(new Deserializer[T](AvroDeserializer.Json[T]))
  }
}