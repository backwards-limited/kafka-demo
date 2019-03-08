package com.backwards.kafka

import cats.syntax.show._
import io.circe.generic.auto._
import monix.eval.Task
import monix.execution.Scheduler
import monocle.macros.syntax.lens._
import org.apache.kafka.clients.producer.RecordMetadata
import org.scalatest.{MustMatchers, WordSpec}
import com.backwards.adt.Foo
import com.backwards.adt.FooShow._
import com.backwards.kafka.RecordMetadataShow._
import com.backwards.kafka.serialization.circe.Deserializer._
import com.backwards.kafka.serialization.circe.Serializer._
import com.typesafe.scalalogging.LazyLogging

class KafkaADTSpec extends WordSpec with MustMatchers with LazyLogging {
  implicit val scheduler: Scheduler = monix.execution.Scheduler.global

  val topic = "my-topic"

  "An ADT" should {
    "be serialized/deserialized to Kafka" in {
      val kafkaConsumer = KafkaConsumer[String, Foo](topic, kafkaConsumerConfig.lens(_.groupId).set("my-group-7"))

      val kafkaProducer = KafkaProducer[String, Foo](topic, kafkaProducerConfig)

      val task: Task[Option[RecordMetadata]] = kafkaProducer.send("my-foo", Foo("some-thing"))
      val Some(recordMetadata) = task.runSyncUnsafe()
      logger info s"Published: ${recordMetadata.show}"

      val (key, value) = kafkaConsumer.pollHead()
      logger info s"Consumed key: $key, value: ${value.show}"
    }
  }
}