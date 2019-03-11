package com.backwards.avro.data

import org.apache.avro.Schema
import com.sksamuel.avro4s._

class Deserializer[T <: Product: SchemaFor: Decoder] {
  val schema: Schema = AvroSchema[T]

  def deserialize(data: Array[Byte]): T = {
    val input = AvroInputStream.data[T] from data build schema
    val t = input.iterator.next()
    input.close()
    t
  }
}

object Deserializer {
  def apply[T <: Product: SchemaFor: Decoder]: Deserializer[T] = new Deserializer[T]
}