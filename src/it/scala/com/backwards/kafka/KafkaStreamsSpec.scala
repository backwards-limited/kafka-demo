package com.backwards.kafka

import java.util.concurrent.TimeUnit
import cats.syntax.show._
import io.circe.generic.auto._
import monix.eval.Task
import monix.execution.Scheduler
import org.apache.kafka.clients.admin.{AdminClient, NewTopic}
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.scalatest.{MustMatchers, WordSpec}
import com.backwards.adt.Foo
import com.backwards.console.Console
import com.backwards.kafka.RecordMetadataShow._
import com.backwards.kafka.serialization.circe.Serde._
import com.backwards.kafka.serialization.circe.Serializer._

class KafkaStreamsSpec extends WordSpec with MustMatchers with KafkaAdmin with Console {
  implicit val scheduler: Scheduler = monix.execution.Scheduler.global

  implicit val admin: AdminClient = adminClient()

  "An ADT" should {
    "be serialized/deserialized to Kafka via Streams" in {
      val topic: NewTopic = createTopic("streams", numberOfPartitions = 1, replicationFactor = 1)

      val builder = new StreamsBuilder()
      val stream: KStream[String, Foo] = builder.stream(topic.name)

      stream.foreach { case (k, v) =>
        out("Stream consume:", s"key: $k, value: $v")
      }

      val streams = new KafkaStreams(builder.build(), kafkaStreamsConfig())
      streams.start()

      val kafkaProducer = KafkaProducer[String, Foo](topic.name, kafkaProducerConfig)
      val task: Task[Option[RecordMetadata]] = kafkaProducer.send(Foo("some-thing"))

      (1 to 5).foreach { _ =>
        val Some(recordMetadata) = task.runSyncUnsafe()
        out(s"Published to ${topic.name}", recordMetadata.show)

        TimeUnit.SECONDS.sleep(2)
      }

      streams.close()
    }
  }
}