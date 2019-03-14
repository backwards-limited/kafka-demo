package com.backwards.kafka.scenarios

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{Duration, _}
import scala.language.postfixOps
import cats.syntax.show._
import monix.eval.Task
import monix.execution.Scheduler
import monix.kafka.{Deserializer, Serializer}
import monocle.macros.syntax.lens._
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.producer.RecordMetadata
import com.backwards.console.Console
import com.backwards.kafka.ConsumerRecordShow._
import com.backwards.kafka.RecordMetadataShow._
import com.backwards.kafka._

trait ScenarioFixture extends KafkaAdmin with Console {
  implicit val scheduler: Scheduler = monix.execution.Scheduler.global

  implicit val admin: AdminClient = adminClient()

  val producePause: Duration = 3 seconds

  def runSyncUnsafe(tasks: Task[Unit]*): Seq[Unit] =
    Task gather tasks runSyncUnsafe()

  def doConsume[V: Deserializer](group: String, topic: String, name: Option[String] = None): Task[Unit] = Task {
    val kafkaConsumer = KafkaConsumer[String, V](topic, kafkaConsumerConfig.lens(_.groupId).set(group))

    while (true) {
      kafkaConsumer.poll().foreach { consumerRecord =>
        val captionPrefix = name.fold("")(name => s"$name - ")
        out(s"${captionPrefix}Consumed from $topic", consumerRecord.show)
      }
    }
  }

  def doConsume[V: Deserializer](group: String): (String, String) => Task[Unit] =
    (name, topic) => doConsume(group, topic, Option(name))

  def doProduce[V: Serializer](data: => V, topic: String): Task[Unit] = Task {
    val kafkaProducer = KafkaProducer[String, V](topic, kafkaProducerConfig)
    val task: Task[Option[RecordMetadata]] = kafkaProducer.send(data)

    while (true) {
      val Some(recordMetadata) = task.runSyncUnsafe()
      out(s"Published to $topic", recordMetadata.show)

      TimeUnit.MILLISECONDS.sleep(producePause.toMillis)
    }
  }

  def doProduce[V: Serializer](data: => V): String => Task[Unit] =
    topic => doProduce(data, topic)
}