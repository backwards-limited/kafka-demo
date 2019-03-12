package com.backwards.kafka.serialization.avro

import monix.kafka.{Serializer => MonixSerializer}
import org.apache.avro.Schema
import org.apache.kafka.common.serialization.{Serializer => KafkaSerializer}
import com.backwards.avro.{SchemaId, Serializer => AvroSerializer}
import com.backwards.kafka.serialization.{DefaultKafkaSerializer, DefaultMonixSerializer}
import com.sksamuel.avro4s.{AvroSchema, Encoder, SchemaFor}
import com.typesafe.scalalogging.LazyLogging

class Serializer[T <: Product: SchemaFor: Encoder](serializer: AvroSerializer[T]) extends DefaultKafkaSerializer[T] with LazyLogging {
  lazy val schema: Schema = {
    val schema = AvroSchema[T]
    logger.info(schema toString true)
    schema
  }

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

  object Json {
    def apply[T <: Product: SchemaFor: Encoder]: KafkaSerializer[T] =
      serializer[T].create()

    implicit def serializer[T <: Product: SchemaFor: Encoder]: MonixSerializer[T] =
      monixSerializer(new Serializer[T](AvroSerializer.Json[T]))
  }

  object Binary {
    def apply[T <: Product: SchemaFor: Encoder]: KafkaSerializer[T] =
      serializer[T].create()

    implicit def serializer[T <: Product: SchemaFor: Encoder]: MonixSerializer[T] =
      monixSerializer(new Serializer[T](AvroSerializer.Binary[T]))

    object Schema {
      def apply[T <: Product: SchemaId: SchemaFor: Encoder]: KafkaSerializer[T] =
        serializer[T].create()

      implicit def serializer[T <: Product: SchemaId: SchemaFor: Encoder]: MonixSerializer[T] =
        monixSerializer(new Serializer[T](AvroSerializer.Binary.Schema[T]))
    }
  }
}