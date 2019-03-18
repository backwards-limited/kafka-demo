package com.backwards.kafka.scenario.four

import scala.language.postfixOps
import io.circe.generic.auto._
import com.backwards.adt.Foo
import com.backwards.kafka.scenario.Scenario
import com.backwards.kafka.serialization.circe.Deserializer._
import com.backwards.kafka.serialization.circe.Serializer._

/**
  * Make sure Kafka is running e.g. within IntelliJ right click and run "docker-compose-lenses.yml" or from command line:
  * docker-compose -f docker-compose-lenses.yml up
  *
  * Run each App (object) in order i.e.
  * <pre>
  *   - Run "CreateTopic" from IntelliJ or from command line:
  *     sbt "it:runMain com.backwards.kafka.scenario.four.CreateTopic"
  *
  *   - Run "Consumer" from IntelliJ or from command line:
  *     sbt "it:runMain com.backwards.kafka.scenario.four.Consumer"
  *
  *   - Run "Producer" from IntelliJ or from command line:
  *     sbt "it:runMain com.backwards.kafka.scenario.four.Producer"
  * </pre>
  */
trait MultiplePartitionsSingleConsumer extends App with Scenario {
  val topic: String = topicOf[MultiplePartitionsSingleConsumer]

  val group: String = topic
}

/**
  * Creating a topic with `replicationFactor` > 1 requires a cluster of at least 2 brokers
  * e.g. use docker-compose-cluster.yml
  */
object CreateTopic extends MultiplePartitionsSingleConsumer {
  createTopic(topic, numberOfPartitions = 4, replicationFactor = 1)
}

object Consumer extends MultiplePartitionsSingleConsumer {
  doConsume[Foo](group, topic).runSyncUnsafe()
}

object Producer extends MultiplePartitionsSingleConsumer {
  doProduce(Foo("some-thing"), topic).runSyncUnsafe()
}