package com.backwards.kafka.serialization

import org.apache.kafka.common.serialization.Serializer
import monix.kafka.{Serializer => MonixSerializer}

trait DefaultMonixSerializer {
  def monixSerializer[T](serializer: => Serializer[T]): MonixSerializer[T] = MonixSerializer[T](
    className = serializer.getClass.getName,
    classType = classOf[Serializer[T]],
    constructor = _ => serializer
  )
}