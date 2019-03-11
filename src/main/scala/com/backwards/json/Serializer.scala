package com.backwards.json

import io.circe.Encoder

class Serializer[T <: Product: Encoder] {
  def serialize(t: T): Array[Byte] =
    implicitly[Encoder[T]].apply(t).noSpaces.getBytes
}

object Serializer {
  def apply[T <: Product: Encoder]: Serializer[T] = new Serializer[T]
}