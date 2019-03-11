package com.backwards.avro

import org.apache.avro.Schema
import com.sksamuel.avro4s._

class Deserializer[T <: Product: SchemaFor: Decoder](avroInputStreamBuilder: AvroInputStreamBuilder[T]) {
  val schema: Schema = AvroSchema[T]

  def deserialize(data: Array[Byte]): T = {
    val input = avroInputStreamBuilder from data build schema
    val t = input.iterator.next()
    input.close()
    t
  }
}

object Deserializer {
  object Data {
    def apply[T <: Product: SchemaFor: Decoder]: Deserializer[T] = new Deserializer[T](AvroInputStream.data[T])
  }

  object Binary {
    def apply[T <: Product: SchemaFor: Decoder]: Deserializer[T] = new Deserializer[T](AvroInputStream.binary[T])
  }

  object Json {
    def apply[T <: Product: SchemaFor: Decoder]: Deserializer[T] = new Deserializer[T](AvroInputStream.json[T])
  }
}