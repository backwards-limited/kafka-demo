package com.backwards.kafka.serialization.avro.data

import java.io.ByteArrayInputStream
import org.apache.avro.file.DataFileStream
import org.apache.avro.generic.{GenericData, GenericDatumReader}
import org.scalatest.{MustMatchers, WordSpec}

case class Ingredient(name: String, sugar: Double, fat: Double)

case class Pizza(name: String, ingredients: Seq[Ingredient], vegetarian: Boolean, vegan: Boolean, calories: Int)

class SerializerSpec extends WordSpec with MustMatchers {
  "Blah 3" should {
    "" in {
      val pepperoni = Pizza("pepperoni", Seq(Ingredient("pepperoni", 12, 4.4), Ingredient("onions", 1, 0.4)), false, false, 598)
      val hawaiian = Pizza("hawaiian", Seq(Ingredient("ham", 1.5, 5.6), Ingredient("pineapple", 5.2, 0.2)), false, false, 391)


      val v = Serializer[Pizza].serialize("topic", pepperoni)


      val x = new DataFileStream(new ByteArrayInputStream(v), new GenericDatumReader[GenericData.Record]())
      println(x.getSchema)

      val y: GenericData.Record = x.iterator().next() // TODO Deserializer to get Pizza

      println(y)
    }
  }
}