package com.backwards.avro.data

import java.io.ByteArrayOutputStream
import org.apache.avro.Schema
import com.sksamuel.avro4s.{AvroOutputStream, AvroSchema, Encoder, SchemaFor}

class Serializer[T <: Product: SchemaFor: Encoder] {
  val schema: Schema = AvroSchema[T]

  def serialize(t: T): Array[Byte] = {
    val baos = new ByteArrayOutputStream()
    val output = AvroOutputStream.data[T] to baos build schema
    output write t
    output.close
    baos.toByteArray
  }
}

object Serializer {
  def apply[T <: Product: SchemaFor: Encoder]: Serializer[T] = new Serializer[T]
}