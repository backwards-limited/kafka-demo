package com.backwards.avro

import org.scalatest.{MustMatchers, WordSpec}
import com.sksamuel.avro4s.{AvroInputStream, AvroOutputStream, AvroSchema, SchemaFor}
import java.io.{BufferedOutputStream, ByteArrayOutputStream, File, FileOutputStream}
import org.apache.avro.Schema
import com.sksamuel.avro4s.kafka.GenericSerde

case class Ingredient(name: String, sugar: Double, fat: Double)

case class Pizza(name: String, ingredients: Seq[Ingredient], vegetarian: Boolean, vegan: Boolean, calories: Int)

class AvroSpec extends WordSpec with MustMatchers {
  "Blah 1" should {
    "" in {
      val pepperoni = Pizza("pepperoni", Seq(Ingredient("pepperoni", 12, 4.4), Ingredient("onions", 1, 0.4)), false, false, 598)
      val hawaiian = Pizza("hawaiian", Seq(Ingredient("ham", 1.5, 5.6), Ingredient("pineapple", 5.2, 0.2)), false, false, 391)

      val schema = AvroSchema[Pizza]

      val os = AvroOutputStream.data[Pizza].to(new File("pizzas.avro")).build(schema)
      os.write(Seq(pepperoni, hawaiian))
      os.flush()
      os.close()

      // ======================================

      val is = AvroInputStream.data[Pizza].from(new File("pizzas.avro")).build(schema)
      val pizzas = is.iterator.toSet
      is.close()

      println(pizzas.mkString("\n"))
    }
  }

  "Blah 2" should {
    "" in {
      val pepperoni = Pizza("pepperoni", Seq(Ingredient("pepperoni", 12, 4.4), Ingredient("onions", 1, 0.4)), false, false, 598)
      val hawaiian = Pizza("hawaiian", Seq(Ingredient("ham", 1.5, 5.6), Ingredient("pineapple", 5.2, 0.2)), false, false, 391)

      val schema = AvroSchema[Pizza]

      val bos = new ByteArrayOutputStream()
      val os = AvroOutputStream.data[Pizza].to(bos).build(schema)
      os.write(Seq(pepperoni, hawaiian))
      os.flush()
      os.close()

      // ======================================

      val is = AvroInputStream.data[Pizza].from(bos.toByteArray).build(schema)
      val pizzas = is.iterator.toSet
      is.close()

      println(pizzas.mkString("\n"))
    }
  }

  "Blah 3" should {
    "" in {
      val pepperoni = Pizza("pepperoni", Seq(Ingredient("pepperoni", 12, 4.4), Ingredient("onions", 1, 0.4)), false, false, 598)
      val hawaiian = Pizza("hawaiian", Seq(Ingredient("ham", 1.5, 5.6), Ingredient("pineapple", 5.2, 0.2)), false, false, 391)

      val blah = new GenericSerde[Pizza] {
        def serialize(topic: String, data: Pizza, file: File): Array[Byte] = {
          val baos = new ByteArrayOutputStream()
          //val output = AvroOutputStream.binary[Pizza].to(file).build(schema)
          val output = AvroOutputStream.binary[Pizza].to(file).build(schema)
          output.write(data)
          output.close()
          baos.toByteArray
        }
      }



      println(new String(blah.serialize("topic", pepperoni, new File("generic.avro"))))

      /*val v = blah.serialize("topic", pepperoni)

      val bos = new BufferedOutputStream(new FileOutputStream("generic.avro"))
      bos.write(v)
      bos.close()*/

    }
  }
}