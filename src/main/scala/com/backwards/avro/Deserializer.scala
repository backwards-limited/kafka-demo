package com.backwards.avro

import java.nio.ByteBuffer
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
import org.apache.avro.io.DecoderFactory
import com.sksamuel.avro4s._
import com.typesafe.scalalogging.LazyLogging

class Deserializer[T <: Product: SchemaFor: Decoder](avroInputStreamBuilder: AvroInputStreamBuilder[T]) {
  val schema: Schema = AvroSchema[T]

  def deserialize(data: Array[Byte]): T = {
    val input = avroInputStreamBuilder from data build schema
    val t = input.iterator.next()
    input.close()
    t
  }
}

object Deserializer extends LazyLogging {
  object Data {
    def apply[T <: Product: SchemaFor: Decoder]: Deserializer[T] = new Deserializer[T](AvroInputStream.data[T])
  }

  object Json {
    def apply[T <: Product: SchemaFor: Decoder]: Deserializer[T] = new Deserializer[T](AvroInputStream.json[T])
  }

  object Binary {
    def apply[T <: Product: SchemaFor: Decoder]: Deserializer[T] = new Deserializer[T](AvroInputStream.binary[T])

    object Schema {
      def apply[T <: Product: SchemaId: SchemaFor: Decoder]: Deserializer[T] = {
        val avroInputStreamBuilder: AvroInputStreamBuilder[T] = AvroInputStream.binary[T]

        new Deserializer[T](avroInputStreamBuilder) {
          val consumeMagicByte: ByteBuffer => (ByteBuffer, Byte) = { bb =>
            val b = bb.get()
            (bb, b)
          }

          override def deserialize(data: Array[Byte]): T = {
            val (bb, _) = consumeMagicByte(ByteBuffer wrap data)
            val schemaId = bb.getInt
            logger.info(s"Deserializing binary data for given schema ID: $schemaId")

            // TODO - Interact with SchemaRegistry (a side effect)

            val input = avroInputStreamBuilder from data.slice(bb.position(), bb.position() + bb.remaining()) build schema
            val t = input.iterator.next()
            input.close()
            t
          }
        }
      }
    }
  }
}