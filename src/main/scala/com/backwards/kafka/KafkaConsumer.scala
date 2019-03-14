package com.backwards.kafka

import scala.collection.JavaConverters._
import scala.concurrent.duration.{Duration, _}
import scala.language.postfixOps
import monix.eval.Task
import monix.execution.Scheduler
import monix.kafka.{Deserializer, KafkaConsumerConfig, KafkaConsumerObservable}
import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer => ApacheKafkaConsumer}
import com.backwards.duration.DurationOps._

class KafkaConsumer[K, V](topic: String, config: KafkaConsumerConfig)(implicit K: Deserializer[K], V: Deserializer[V], scheduler: Scheduler) {
  import KafkaConsumer._

  lazy val underlying: ApacheKafkaConsumer[K, V] = {
    val observable: Task[ApacheKafkaConsumer[K, V]] = KafkaConsumerObservable.createConsumer[K, V](config, List(topic))
    observable runSyncUnsafe timeout
  }

  def init: KafkaConsumer[K, V] = {
    underlying poll timeout
    this
  }
}

object KafkaConsumer {
  val timeout: Duration = 10 seconds

  def apply[K, V](topic: String, config: KafkaConsumerConfig)(implicit K: Deserializer[K], V: Deserializer[V], scheduler: Scheduler): KafkaConsumer[K, V] =
    new KafkaConsumer[K, V](topic, config)

  implicit class KafkaConsumerOps[K, V](kafkaConsumer: KafkaConsumer[K, V]) {
    def poll(duration: Duration = timeout): Iterable[ConsumerRecord[K, V]] =
      kafkaConsumer.underlying.poll(duration).asScala
  }
}