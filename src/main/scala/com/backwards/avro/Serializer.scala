package com.backwards.avro

import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import org.apache.avro.Schema
import com.backwards.avro.Format._
import com.backwards.avro.SchemaId.ops._
import com.backwards.console.Console
import com.sksamuel.avro4s._

class Serializer[T <: Product: SchemaFor: Encoder](avroOutputStreamBuilder: AvroOutputStreamBuilder[T]) extends Console {
  lazy val schema: Schema = {
    val schema = AvroSchema[T]
    out("Schema", schema toString true)
    schema
  }

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

  object Json {
    def apply[T <: Product: SchemaFor: Encoder]: Serializer[T] = new Serializer[T](AvroOutputStream.json[T])
  }

  object Binary {
    def apply[T <: Product: SchemaFor: Encoder]: Serializer[T] = new Serializer[T](AvroOutputStream.binary[T])

    object Schema {
      def apply[T <: Product: SchemaId: SchemaFor: Encoder]: Serializer[T] = {
        val avroOutputStreamBuilder: AvroOutputStreamBuilder[T] = AvroOutputStream.binary[T]

        new Serializer[T](avroOutputStreamBuilder) {
          override def serialize(t: T): Array[Byte] = {
            val baos = new ByteArrayOutputStream()
            baos.write(magicByte)
            baos.write(ByteBuffer.allocate(4).putInt(t.schemaId).array())

            val output = avroOutputStreamBuilder to baos build schema
            output write t
            output.close

            baos.toByteArray
          }
        }
      }
    }
  }
}