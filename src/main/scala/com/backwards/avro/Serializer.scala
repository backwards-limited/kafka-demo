package com.backwards.avro

import java.io.ByteArrayOutputStream
import org.apache.avro.Schema
import com.sksamuel.avro4s._

class Serializer[T <: Product: SchemaFor: Encoder](avroOutputStreamBuilder: AvroOutputStreamBuilder[T]) {
  val schema: Schema = AvroSchema[T]

  def serialize(t: T): Array[Byte] = {
    val baos = new ByteArrayOutputStream()
    val output = avroOutputStreamBuilder to baos build schema
    output write t
    output.close
    baos.toByteArray
  }
}

object Serializer {
  object Data {
    def apply[T <: Product: SchemaFor: Encoder]: Serializer[T] = new Serializer[T](AvroOutputStream.data[T])
  }

  object Binary {
    def apply[T <: Product: SchemaFor: Encoder]: Serializer[T] = new Serializer[T](AvroOutputStream.binary[T])
  }

  object Json {
    def apply[T <: Product: SchemaFor: Encoder]: Serializer[T] = new Serializer[T](AvroOutputStream.json[T])
  }
}