package com.backwards.kafka.serialization.avro.data

import java.io.ByteArrayOutputStream
import java.util
import monix.kafka.{Serializer => MonixSerializer}
import org.apache.avro.Schema
import org.apache.kafka.common.serialization.{Serializer => ApacheSerializer}
import com.sksamuel.avro4s.{AvroOutputStream, AvroSchema, Encoder, SchemaFor}

class Serializer[T <: Product: SchemaFor: Encoder] extends ApacheSerializer[T] {
  val schema: Schema = AvroSchema[T]

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def serialize(topic: String, data: T): Array[Byte] = {
    val baos = new ByteArrayOutputStream()
    val output = AvroOutputStream.data[T].to(baos).build(schema)
    output.write(data)
    output.close()
    baos.toByteArray
  }

  override def close(): Unit = ()
}

object Serializer {
  def apply[T <: Product: SchemaFor: Encoder]: ApacheSerializer[T] = serializer[T].create()

  implicit def serializer[T <: Product: SchemaFor: Encoder]: MonixSerializer[T] = MonixSerializer[T](
    className = Serializer.getClass.getName,
    classType = classOf[Serializer[T]],
    constructor = _ => new Serializer[T]
  )
}