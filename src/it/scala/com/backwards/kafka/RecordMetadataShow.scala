package com.backwards.kafka

import cats.Show
import org.apache.kafka.clients.producer.RecordMetadata

object RecordMetadataShow {
  implicit val recordeMetadataShow: Show[RecordMetadata] = Show[RecordMetadata] { meta =>
    s"RecordMetadata: topic = ${meta.topic()}, partition = ${meta.partition()}, offset = ${meta.offset()}, serializedKeySize = ${meta.serializedKeySize()}, serializedValueSize = ${meta.serializedValueSize()}"
  }
}