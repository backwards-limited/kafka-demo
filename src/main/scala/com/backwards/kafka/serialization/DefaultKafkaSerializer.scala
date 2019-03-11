package com.backwards.kafka.serialization

import java.util
import org.apache.kafka.common.serialization.Serializer

trait DefaultKafkaSerializer[T] extends Serializer[T] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def close(): Unit = ()
}