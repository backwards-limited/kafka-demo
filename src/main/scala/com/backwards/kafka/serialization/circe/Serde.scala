package com.backwards.kafka.serialization.circe

import io.circe.{Decoder, Encoder}
import org.apache.kafka.common.serialization.{Serdes, Serde => ApacheSerde}

object Serde {
  implicit def serde[T <: Product: Encoder: Decoder]: ApacheSerde[T] = Serdes.serdeFrom(Serializer[T], Deserializer[T])
}