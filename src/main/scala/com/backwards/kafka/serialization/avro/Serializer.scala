package com.backwards.kafka.serialization.avro

import monix.kafka.{Serializer => MonixSerializer}
import org.apache.avro.Schema
import org.apache.kafka.common.serialization.{Serializer => KafkaSerializer}
import com.backwards.avro.{Serializer => AvroSerializer}
import com.backwards.kafka.serialization.{DefaultKafkaSerializer, DefaultMonixSerializer}
import com.sksamuel.avro4s.{AvroSchema, Encoder, SchemaFor}

class Serializer[T <: Product: SchemaFor: Encoder](serializer: AvroSerializer[T]) extends DefaultKafkaSerializer[T] {
  val schema: Schema = AvroSchema[T]

  override def serialize(topic: String, data: T): Array[Byte] =
    serializer serialize data
}

object Serializer extends DefaultMonixSerializer {
  object Data {
    def apply[T <: Product: SchemaFor: Encoder]: KafkaSerializer[T] =
      serializer[T].create()

    implicit def serializer[T <: Product: SchemaFor: Encoder]: MonixSerializer[T] =
      monixSerializer(new Serializer[T](AvroSerializer.Data[T]))
  }

  object Binary {
    def apply[T <: Product: SchemaFor: Encoder]: KafkaSerializer[T] =
      serializer[T].create()

    implicit def serializer[T <: Product: SchemaFor: Encoder]: MonixSerializer[T] =
      monixSerializer(new Serializer[T](AvroSerializer.Binary[T]))
  }

  object Json {
    def apply[T <: Product: SchemaFor: Encoder]: KafkaSerializer[T] =
      serializer[T].create()

    implicit def serializer[T <: Product: SchemaFor: Encoder]: MonixSerializer[T] =
      monixSerializer(new Serializer[T](AvroSerializer.Json[T]))
  }
}