package com.backwards.kafka

import cats.Show
import org.apache.kafka.clients.consumer.ConsumerRecord

object ConsumerRecordShow {
  implicit def consumerRecordShow[K, V]: Show[ConsumerRecord[K, V]] = Show[ConsumerRecord[K, V]] { record =>
    s"ConsumerRecord: topic = ${record.topic()}, partition = ${record.partition()}, offset = ${record.offset()}, serializedKeySize = ${record.serializedKeySize()}, serializedValueSize = ${record.serializedValueSize()}, key = ${record.key()}, value = ${record.value()}"
  }
}