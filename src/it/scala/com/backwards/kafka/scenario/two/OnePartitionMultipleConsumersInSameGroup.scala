package com.backwards.kafka.scenario.two

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
  *     sbt "it:runMain com.backwards.kafka.scenario.two.CreateTopic"
  *
  *   - Run "ConsumerA" from IntelliJ or from command line:
  *     sbt "it:runMain com.backwards.kafka.scenario.two.ConsumerA"
  *
  *   - Run "ConsumerB" from IntelliJ or from command line:
  *     sbt "it:runMain com.backwards.kafka.scenario.two.ConsumerB"
  *
  *   - Run "Producer" from IntelliJ or from command line:
  *     sbt "it:runMain com.backwards.kafka.scenario.two.Producer"
  * </pre>
  *
  * Stop the consumer that is consuming and the idle consumer should take over.
  */
trait OnePartitionMultipleConsumersInSameGroup extends App with Scenario {
  val topic: String = topicOf[OnePartitionMultipleConsumersInSameGroup]

  val group: String = topic
}

/**
  * Creating a topic with `replicationFactor` > 1 requires a cluster of at least 2 brokers
  * e.g. use docker-compose-cluster.yml
  */
object CreateTopic extends OnePartitionMultipleConsumersInSameGroup {
  createTopic(topic, numberOfPartitions = 1, replicationFactor = 1)
}

object ConsumerA extends OnePartitionMultipleConsumersInSameGroup {
  doConsume[Foo](group, topic, "A").runSyncUnsafe()
}

object ConsumerB extends OnePartitionMultipleConsumersInSameGroup {
  doConsume[Foo](group, topic, "B").runSyncUnsafe()
}

object Producer extends OnePartitionMultipleConsumersInSameGroup {
  doProduce(Foo("some-thing"), topic).runSyncUnsafe()
}