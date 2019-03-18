package com.backwards.kafka.scenario.three

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
  *     sbt "it:runMain com.backwards.kafka.scenario.three.CreateTopic"
  *
  *   - Run "ConsumerInGroup1" from IntelliJ or from command line:
  *     sbt "it:runMain com.backwards.kafka.scenario.three.ConsumerInGroup1"
  *
  *   - Run "ConsumerInGroup2" from IntelliJ or from command line:
  *     sbt "it:runMain com.backwards.kafka.scenario.three.ConsumerInGroup2"
  *
  *   - Run "Producer" from IntelliJ or from command line:
  *     sbt "it:runMain com.backwards.kafka.scenario.three.Producer"
  * </pre>
  */
trait OnePartitionMultipleConsumersEachInDifferentGroup extends App with Scenario {
  val topic: String = topicOf[OnePartitionMultipleConsumersEachInDifferentGroup]

  val group: String = topic
}

/**
  * Creating a topic with `replicationFactor` > 1 requires a cluster of at least 2 brokers
  * e.g. use docker-compose-cluster.yml
  */
object CreateTopic extends OnePartitionMultipleConsumersEachInDifferentGroup {
  createTopic(topic, numberOfPartitions = 1, replicationFactor = 1)
}

object ConsumerInGroup1 extends OnePartitionMultipleConsumersEachInDifferentGroup {
  doConsume[Foo](s"$group-1", topic, "1").runSyncUnsafe()
}

object ConsumerInGroup2 extends OnePartitionMultipleConsumersEachInDifferentGroup {
  doConsume[Foo](s"$group-2", topic, "2").runSyncUnsafe()
}

object Producer extends OnePartitionMultipleConsumersEachInDifferentGroup {
  doProduce(Foo("some-thing"), topic).runSyncUnsafe()
}