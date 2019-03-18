package com.backwards.kafka.scenario.five

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
  *     sbt "it:runMain com.backwards.kafka.scenario.five.CreateTopic"
  *
  *   - Run "ConsumerA" from IntelliJ or from command line:
  *     sbt "it:runMain com.backwards.kafka.scenario.five.ConsumerA"
  *
  *   - Run "ConsumerB" from IntelliJ or from command line:
  *     sbt "it:runMain com.backwards.kafka.scenario.five.ConsumerB"
  *
  *   - Run "Producer" from IntelliJ or from command line:
  *     sbt "it:runMain com.backwards.kafka.scenario.five.Producer"
  *
  *   - Run "ConsumerC" from IntelliJ or from command line:
  *     sbt "it:runMain com.backwards.kafka.scenario.five.ConsumerC"
  *
  *   - Run "ConsumerD" from IntelliJ or from command line:
  *     sbt "it:runMain com.backwards.kafka.scenario.five.ConsumerD"
  *
  *   - Run "ExtraConsumer" from IntelliJ or from command line:
  *     sbt "it:runMain com.backwards.kafka.scenario.five.ExtraConsumer"
  * </pre>
  */
trait MultiplePartitionsMultipleConsumersInSameGroup extends App with Scenario {
  val topic: String = topicOf[MultiplePartitionsMultipleConsumersInSameGroup]

  val group: String = topic
}

/**
  * Creating a topic with `replicationFactor` > 1 requires a cluster of at least 2 brokers
  * e.g. use docker-compose-cluster.yml
  */
object CreateTopic extends MultiplePartitionsMultipleConsumersInSameGroup {
  createTopic(topic, numberOfPartitions = 4, replicationFactor = 1)
}

object ConsumerA extends MultiplePartitionsMultipleConsumersInSameGroup {
  doConsume[Foo](group, topic, "A").runSyncUnsafe()
}

object ConsumerB extends MultiplePartitionsMultipleConsumersInSameGroup {
  doConsume[Foo](group, topic, "B").runSyncUnsafe()
}

object Producer extends MultiplePartitionsMultipleConsumersInSameGroup {
  doProduce(Foo("some-thing"), topic).runSyncUnsafe()
}

object ConsumerC extends MultiplePartitionsMultipleConsumersInSameGroup {
  doConsume[Foo](group, topic, "C").runSyncUnsafe()
}

object ConsumerD extends MultiplePartitionsMultipleConsumersInSameGroup {
  doConsume[Foo](group, topic, "D").runSyncUnsafe()
}

object ExtraConsumer extends MultiplePartitionsMultipleConsumersInSameGroup {
  doConsume[Foo](group, topic, "X").runSyncUnsafe()
}