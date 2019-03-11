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
import com.backwards.kafka.RecordMetadataShow._
import com.backwards.kafka.serialization.avro.data.Serializer._

class KafkaAvroSpec extends WordSpec with MustMatchers with Console {
  implicit val scheduler: Scheduler = monix.execution.Scheduler.global

  val topic = "avro"

  "An ADT" should {
    "be serialized/deserialized to Kafka as Avro" in {
      //val kafkaConsumer = KafkaConsumer[String, String](topic, kafkaConsumerConfig.lens(_.groupId).set("avro-group"))

      val kafkaProducer = KafkaProducer[String, Foo](topic, kafkaProducerConfig)

      val task: Task[Option[RecordMetadata]] = kafkaProducer.send("my-foo", Foo("some-thing"))
      val Some(recordMetadata) = task.runSyncUnsafe()
      out("Published", recordMetadata.show)

      /*val (key, value) = kafkaConsumer.pollHead()
      logger info s"Consumed key: $key, value: $value"*/
    }
  }
}