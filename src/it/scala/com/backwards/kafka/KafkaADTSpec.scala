package com.backwards.kafka

import cats.syntax.show._
import io.circe.generic.auto._
import monix.eval.Task
import monix.execution.Scheduler
import monocle.macros.syntax.lens._
import org.apache.kafka.clients.producer.RecordMetadata
import org.scalatest.{MustMatchers, WordSpec}
import com.backwards.adt.Foo
import com.backwards.console.Console
import com.backwards.kafka.ConsumerRecordShow._
import com.backwards.kafka.RecordMetadataShow._
import com.backwards.kafka.serialization.circe.Deserializer._
import com.backwards.kafka.serialization.circe.Serializer._

class KafkaADTSpec extends WordSpec with MustMatchers with Console {
  implicit val scheduler: Scheduler = monix.execution.Scheduler.global

  val topic = "adt"

  "An ADT" should {
    "be serialized/deserialized to Kafka" in {
      val kafkaConsumer = KafkaConsumer[String, Foo](topic, kafkaConsumerConfig.lens(_.groupId).set("adt-group")).init

      val kafkaProducer = KafkaProducer[String, Foo](topic, kafkaProducerConfig)

      val task: Task[Option[RecordMetadata]] = kafkaProducer.send("foo-key", Foo("some-thing"))
      val Some(recordMetadata) = task.runSyncUnsafe()
      out(s"Published to $topic", recordMetadata.show)

      val consumerRecord = kafkaConsumer.poll().head
      out(s"Consumed from $topic", consumerRecord.show)
    }
  }
}