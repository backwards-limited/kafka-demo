package com.backwards.avro

import java.io.ByteArrayInputStream
import org.apache.avro.file.DataFileStream
import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
import org.scalatest.{MustMatchers, WordSpec}
import com.backwards.adt.PizzaSchemaId._
import com.backwards.adt.{Ingredient, Pizza}
import com.backwards.console.Console
import com.sksamuel.avro4s.RecordFormat

class SerializerSpec extends WordSpec with MustMatchers with Console {
  "ADT" should {
    "be serialized and deserialized as Avro data, which will include an autogenerated schema" in {
      val pepperoni = Pizza("pepperoni", Seq(Ingredient("pepperoni", 12, 4.4), Ingredient("onions", 1, 0.4)), vegetarian = false, vegan = false, 598)

      val bytes: Array[Byte] = Serializer.Data[Pizza] serialize pepperoni

      val stream: DataFileStream[GenericRecord] = new DataFileStream(new ByteArrayInputStream(bytes), new GenericDatumReader[GenericRecord]())
      out("Generated Schema", stream.getSchema)

      val genericRecord: GenericRecord = stream.iterator().next()
      out("Data", RecordFormat[Pizza] from genericRecord)
      out("Data JSON", genericRecord)
    }

    "be serialized and deserialized as Avro binary" in {
      val pepperoni = Pizza("pepperoni", Seq(Ingredient("pepperoni", 12, 4.4), Ingredient("onions", 1, 0.4)), vegetarian = false, vegan = false, 598)

      val bytes: Array[Byte] = Serializer.Binary[Pizza] serialize pepperoni

      val pizza = Deserializer.Binary[Pizza].deserialize(bytes)
      out("From Binary without schema", pizza)
    }

    "be serialized and deserialized as Avro binary with SchemaId" in {
      val pepperoni = Pizza("pepperoni", Seq(Ingredient("pepperoni", 12, 4.4), Ingredient("onions", 1, 0.4)), vegetarian = false, vegan = false, 598)

      val bytes: Array[Byte] = Serializer.Binary.Schema[Pizza] serialize pepperoni

      val pizza = Deserializer.Binary.Schema[Pizza] deserialize bytes
      out("From Binary with schema ID", pizza)
    }
  }
}