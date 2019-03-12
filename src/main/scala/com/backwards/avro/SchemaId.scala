package com.backwards.avro

import scala.language.implicitConversions

trait SchemaId[T] {
  def apply(t: T): Int
}

object SchemaId {
  def apply[T: SchemaId](t: T): Int = implicitly[SchemaId[T]] apply t

  trait Ops[T] {
    def schemaId: Int
  }

  object ops {
    implicit def toSchemaIdOps[T: SchemaId](t: T): Ops[T] = new Ops[T] {
      override def schemaId: Int = apply(t)
    }
  }
}