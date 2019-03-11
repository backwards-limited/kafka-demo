package com.backwards.kafka.serialization

import monix.kafka.{Deserializer => MonixDeserializer}
import org.apache.kafka.common.serialization.{Deserializer => KafkaDeserializer}

trait DefaultMonixDeserializer {
  def monixDeserializer[T](deserializer: => KafkaDeserializer[T]): MonixDeserializer[T] = MonixDeserializer[T](
    className = deserializer.getClass.getName,
    classType = deserializer.getClass,
    constructor = _ => deserializer
  )
}