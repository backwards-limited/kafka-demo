package com.backwards.json

import io.circe.Decoder
import io.circe.parser.decode
import com.backwards.serialization.DeserializationException

class Deserializer[T <: Product: Decoder] {
  def deserialize(data: Array[Byte]): T =
    Option(data).fold(null.asInstanceOf[T]) { data =>
      decode[T](new String(data)).fold(error => throw DeserializationException(error), identity)
    }
}

object Deserializer {
  def apply[T <: Product: Decoder]: Deserializer[T] = new Deserializer[T]
}