package com.backwards.kafka.scenarios

import java.util.concurrent.TimeUnit
import cats.syntax.show._
import io.circe.generic.auto._
import monix.eval.Task
import monix.execution.Scheduler
import org.apache.kafka.clients.admin.{AdminClient, NewTopic}
import org.apache.kafka.clients.producer.RecordMetadata
import com.backwards.adt.Foo
import com.backwards.console.Console
import com.backwards.kafka.RecordMetadataShow._
import com.backwards.kafka.serialization.circe.Serializer._
import com.backwards.kafka.{KafkaAdmin, KafkaProducer, kafkaProducerConfig}

/**
  * Creating topics with `replicationFactor` > 1 requires a cluster of at least 2 brokers
  * e.g. use docker-compose-cluster.yml
  */
object OnePartitionOneConsumer extends App with KafkaAdmin with Console {
  implicit val scheduler: Scheduler = monix.execution.Scheduler.global

  implicit val admin: AdminClient = adminClient()

  val topic: NewTopic = createTopic("one-partition-one-consumer", numberOfPartitions = 1, replicationFactor = 2)

  val kafkaProducer = KafkaProducer[String, Foo](topic.name, kafkaProducerConfig)
  val task: Task[Option[RecordMetadata]] = kafkaProducer.send(Foo("some-thing"))

  while (true) {
    val Some(recordMetadata) = task.runSyncUnsafe()
    out(s"Published to ${topic.name}", recordMetadata.show)

    TimeUnit.SECONDS.sleep(3)
  }
}