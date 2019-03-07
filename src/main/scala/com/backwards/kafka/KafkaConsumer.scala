package com.backwards.kafka

import scala.collection.JavaConverters._
import scala.concurrent.duration.{Duration, _}
import scala.language.postfixOps
import monix.eval.Task
import monix.execution.Scheduler
import monix.kafka.{Deserializer, KafkaConsumerConfig, KafkaConsumerObservable}
import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer => ApacheKafkaConsumer}

class KafkaConsumer[K, V](topic: String, config: KafkaConsumerConfig)(implicit K: Deserializer[K], V: Deserializer[V], scheduler: Scheduler) {
  val underlying: ApacheKafkaConsumer[K, V] = {
    val observable: Task[ApacheKafkaConsumer[K, V]] = KafkaConsumerObservable.createConsumer[K, V](config, List(topic))
    val consumer = observable runSyncUnsafe 10.seconds
    consumer poll 0
    consumer
  }
}

object KafkaConsumer {
  def apply[K, V](topic: String, config: KafkaConsumerConfig)(implicit K: Deserializer[K], V: Deserializer[V], scheduler: Scheduler): KafkaConsumer[K, V] =
    new KafkaConsumer[K, V](topic, config)

  implicit class KafkaConsumerOps[K, V](kafkaConsumer: KafkaConsumer[K, V]) {
    def poll(duration: Duration = 10 seconds): Iterable[ConsumerRecord[K, V]] =
      kafkaConsumer.underlying.poll(duration.toMillis).asScala

    def pollHead(duration: Duration = 10 seconds): (K, V) = {
      val consumerRecord = kafkaConsumer.underlying.poll(duration.toMillis).asScala.head
      (consumerRecord.key(), consumerRecord.value())
    }
  }
}