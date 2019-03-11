package com.backwards.json

import io.circe.generic.auto._
import org.scalatest.{MustMatchers, WordSpec}
import com.backwards.adt.{Ingredient, Pizza}
import com.backwards.console.Console

class SerializerSpec extends WordSpec with MustMatchers with Console {
  "ADT" should {
    "be serialized and deserialized as Json data," in {
      val pepperoni = Pizza("pepperoni", Seq(Ingredient("pepperoni", 12, 4.4), Ingredient("onions", 1, 0.4)), vegetarian = false, vegan = false, 598)

      val bytes: Array[Byte] = Serializer[Pizza] serialize pepperoni

      out("Data", new String(bytes))
    }
  }
}