package com.backwards.kafka.serialization.circe

import java.util
import io.circe.Decoder
import io.circe.parser._
import monix.kafka.{Deserializer => MonixDeserializer}
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.{Deserializer => ApacheDeserializer}

class Deserializer[T <: Product: Decoder] extends ApacheDeserializer[T] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def deserialize(topic: String, data: Array[Byte]): T =
    Option(data).fold(null.asInstanceOf[T]) { data =>
      decode[T](new String(data)).fold(error => throw new SerializationException(error), identity)
    }

  override def close(): Unit = ()
}

object Deserializer {
  def apply[T <: Product: Decoder]: ApacheDeserializer[T] = deserializer[T].create()

  implicit def deserializer[T <: Product: Decoder]: MonixDeserializer[T] = MonixDeserializer[T](
    className = Deserializer.getClass.getName,
    classType = classOf[Deserializer[T]],
    constructor = _ => new Deserializer[T]
  )
}