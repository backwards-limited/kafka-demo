package com.backwards.adt

import com.backwards.avro.SchemaId

object FooSchemaId {
  val schemaId: Int = 0

  implicit val fooSchemaId: SchemaId[Foo] =
    _ => schemaId
}