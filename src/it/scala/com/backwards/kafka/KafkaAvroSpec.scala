package com.backwards.kafka

import cats.syntax.show._
import monix.eval.Task
import monix.execution.Scheduler
import monocle.macros.syntax.lens._
import org.apache.kafka.clients.producer.RecordMetadata
import org.scalatest.{MustMatchers, WordSpec}
import com.backwards.adt.Foo
import com.backwards.adt.FooShow._
import com.backwards.console.Console
import com.backwards.kafka.RecordMetadataShow._

class KafkaAvroSpec extends WordSpec with MustMatchers with Console {
  implicit val scheduler: Scheduler = monix.execution.Scheduler.global

  "An ADT" should {
    "be serialized/deserialized to Kafka as Avro including schema" in {
      import com.backwards.kafka.serialization.avro.data.Deserializer._
      import com.backwards.kafka.serialization.avro.data.Serializer._

      val topic = "avro-with-schema"

      val kafkaConsumer = KafkaConsumer[String, Foo](topic, kafkaConsumerConfig.lens(_.groupId).set("avro-group"))

      val kafkaProducer = KafkaProducer[String, Foo](topic, kafkaProducerConfig)

      val task: Task[Option[RecordMetadata]] = kafkaProducer.send("foo-key", Foo("some-thing"))
      val Some(recordMetadata) = task.runSyncUnsafe()
      out("Published", recordMetadata.show)

      val (key, value) = kafkaConsumer.pollHead()
      out("Consumed", s"key: $key, value: ${value.show}")
    }

    "be serialized/deserialized to Kafka as Avro excluding schema" in {
      import com.backwards.kafka.serialization.avro.binary.Deserializer._
      import com.backwards.kafka.serialization.avro.binary.Serializer._

      val topic = "avro-with-schema"

      val kafkaConsumer = KafkaConsumer[String, Foo](topic, kafkaConsumerConfig.lens(_.groupId).set("avro-group"))

      val kafkaProducer = KafkaProducer[String, Foo](topic, kafkaProducerConfig)

      val task: Task[Option[RecordMetadata]] = kafkaProducer.send("foo-key", Foo("some-thing"))
      val Some(recordMetadata) = task.runSyncUnsafe()
      out("Published", recordMetadata.show)

      val (key, value) = kafkaConsumer.pollHead()
      out("Consumed", s"key: $key, value: ${value.show}")
    }
  }
}