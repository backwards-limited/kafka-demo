package com.backwards.kafka.serialization

import monix.kafka.{Serializer => MonixSerializer}
import org.apache.kafka.common.serialization.{Serializer => KafkaSerializer}

trait DefaultMonixSerializer {
  def monixSerializer[T](serializer: => KafkaSerializer[T]): MonixSerializer[T] = MonixSerializer[T](
    className = serializer.getClass.getName,
    classType = serializer.getClass,
    constructor = _ => serializer
  )
}