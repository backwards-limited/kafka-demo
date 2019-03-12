package com.backwards.adt

import com.backwards.avro.SchemaId

object PizzaSchemaId {
  val schemaId: Int = 0

  implicit val pizzaSchemaId: SchemaId[Pizza] =
    _ => schemaId
}