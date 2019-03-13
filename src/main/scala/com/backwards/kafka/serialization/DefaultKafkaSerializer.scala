package com.backwards.kafka.serialization

import java.util
import org.apache.kafka.common.serialization.{Serializer => KafkaSerializer}

trait DefaultKafkaSerializer[T] extends KafkaSerializer[T] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def close(): Unit = ()
}