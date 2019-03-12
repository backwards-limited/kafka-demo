package com.backwards.kafka

import java.util.Properties
import java.util.concurrent.TimeUnit
import cats.syntax.show._
import io.circe.generic.auto._
import monix.eval.Task
import monix.execution.Scheduler
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.scalatest.{MustMatchers, WordSpec}
import com.backwards.adt.Foo
import com.backwards.console.Console
import com.backwards.kafka.RecordMetadataShow._
import com.backwards.kafka.serialization.circe.Serde._
import com.backwards.kafka.serialization.circe.Serializer._

// TODO - WIP
class KafkaStreamsSpec extends WordSpec with MustMatchers with Console {
  implicit val scheduler: Scheduler = monix.execution.Scheduler.global

  val topic = "streams"

  "An ADT" should {
    "be serialized/deserialized to Kafka via Streams" in {
      val builder = new StreamsBuilder()
      val stream: KStream[String, Foo] = builder.stream(topic)

      stream.foreach { case (k, v) =>
        println(s"===> Wow! Got key: $k, value: $v")
      }


      val kafkaStreamsConfig = {
        val properties = new Properties()
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-test")
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        properties
      }

      val blah = new KafkaStreams(builder.build(), kafkaStreamsConfig)
      blah.start()


      val kafkaProducer = KafkaProducer[String, Foo](topic, kafkaProducerConfig)

      val task: Task[Option[RecordMetadata]] = kafkaProducer.send("foo-key", Foo("some-thing"))
      val Some(recordMetadata) = task.runSyncUnsafe()
      out(s"Published to $topic", recordMetadata.show)

      TimeUnit.SECONDS.sleep(5)

      /*val (key, value) = kafkaConsumer.pollHead()
      out(s"Consumed from $topic", s"key: $key, value: ${value.show}")*/
    }
  }
}