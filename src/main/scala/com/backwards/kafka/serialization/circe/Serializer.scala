package com.backwards.kafka.serialization.circe

import java.util
import io.circe.Encoder
import monix.kafka.{Serializer => MonixSerializer}
import org.apache.kafka.common.serialization.{Serializer => ApacheSerializer}

class Serializer[T <: Product: Encoder] extends ApacheSerializer[T] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def serialize(topic: String, data: T): Array[Byte] =
    implicitly[Encoder[T]].apply(data).noSpaces.getBytes

  override def close(): Unit = ()
}

object Serializer {
  def apply[T <: Product: Encoder](): ApacheSerializer[T] = serializer[T].create()

  implicit def serializer[T <: Product: Encoder]: MonixSerializer[T] = MonixSerializer[T](
    className = Serializer.getClass.getName,
    classType = classOf[Serializer[T]],
    constructor = _ => new Serializer[T]
  )
}