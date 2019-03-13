package com.backwards.kafka.scenarios

import java.util.concurrent.TimeUnit
import cats.syntax.show._
import io.circe.generic.auto._
import monix.eval.Task
import monix.execution.Scheduler
import monocle.macros.syntax.lens._
import org.apache.kafka.clients.admin.{AdminClient, NewTopic}
import org.apache.kafka.clients.producer.RecordMetadata
import com.backwards.adt.Foo
import com.backwards.console.Console
import com.backwards.kafka.ConsumerRecordShow._
import com.backwards.kafka.RecordMetadataShow._
import com.backwards.kafka._
import com.backwards.kafka.serialization.circe.Deserializer._
import com.backwards.kafka.serialization.circe.Serializer._

/**
  * Creating topics with `replicationFactor` > 1 requires a cluster of at least 2 brokers
  * e.g. use docker-compose-cluster.yml
  */
object OnePartitionOneConsumer extends App with KafkaAdmin with Console {
  implicit val scheduler: Scheduler = monix.execution.Scheduler.global

  implicit val admin: AdminClient = adminClient()

  val topic: NewTopic = createTopic("one-partition-one-consumer", numberOfPartitions = 1, replicationFactor = 1)

  Task gather Seq(consume(topic.name), produce(topic.name)) runSyncUnsafe()

  def consume(topic: String): Task[Unit] = Task {
    val kafkaConsumer = KafkaConsumer[String, Foo](topic, kafkaConsumerConfig.lens(_.groupId).set("adt-group"))

    while (true) {
      kafkaConsumer.poll().foreach { consumerRecord =>
        out(s"Consumed from $topic", consumerRecord.show)
      }
    }
  }

  def produce(topic: String): Task[Unit] = Task {
    val kafkaProducer = KafkaProducer[String, Foo](topic, kafkaProducerConfig)
    val task: Task[Option[RecordMetadata]] = kafkaProducer.send(Foo("some-thing"))

    while (true) {
      val Some(recordMetadata) = task.runSyncUnsafe()
      out(s"Published to $topic", recordMetadata.show)

      TimeUnit.SECONDS.sleep(3)
    }
  }
}