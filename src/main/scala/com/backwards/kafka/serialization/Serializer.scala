package com.backwards.kafka.serialization

import java.util
import monix.kafka.{Serializer => MonixSerializer}
import org.apache.kafka.common.serialization.{IntegerSerializer, Serializer => KafkaSerializer}

object Serializer {
  implicit val intSerializer: MonixSerializer[Int] =
    MonixSerializer[Int](
      className = "com.backwards.kafka.serialization.Serializer.IntSerializer",
      classType = classOf[IntSerializer]
    )

  class IntSerializer extends KafkaSerializer[Int] {
    val serializer = new IntegerSerializer

    override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

    override def serialize(topic: String, data: Int): Array[Byte] = serializer.serialize(topic, data)

    override def close(): Unit = ()
  }
}