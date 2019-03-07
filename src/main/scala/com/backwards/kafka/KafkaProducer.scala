package com.backwards.kafka

import monix.eval.Task
import monix.execution.Scheduler
import monix.kafka.{KafkaProducerConfig, Serializer, KafkaProducer => MonixKafkaProducer}
import org.apache.kafka.clients.producer.RecordMetadata

class KafkaProducer[K, V](topic: String, config: KafkaProducerConfig)(implicit K: Serializer[K], V: Serializer[V], scheduler: Scheduler) {
  lazy val underlying: MonixKafkaProducer[K, V] = MonixKafkaProducer[K, V](config, scheduler)

  def send(key: K, value: V): Task[Option[RecordMetadata]] = underlying.send(topic, key, value)

  def send(value: V): Task[Option[RecordMetadata]] = underlying.send(topic, value)
}

object KafkaProducer {
  def apply[K, V](topic: String, config: KafkaProducerConfig)(implicit K: Serializer[K], V: Serializer[V], scheduler: Scheduler): KafkaProducer[K, V] =
    new KafkaProducer[K, V](topic, config)
}