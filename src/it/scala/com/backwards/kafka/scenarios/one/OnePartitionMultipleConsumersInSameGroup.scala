package com.backwards.kafka.scenarios.one

import scala.language.postfixOps
import io.circe.generic.auto._
import com.backwards.adt.Foo
import com.backwards.kafka.scenarios.ScenarioFixture
import com.backwards.kafka.serialization.circe.Deserializer._
import com.backwards.kafka.serialization.circe.Serializer._

/**
  * Run each App (object) in order
  */
trait OnePartitionMultipleConsumersInSameGroup extends App with ScenarioFixture {
  out("Boot", getClass.getName.replaceAll("\\$$", ""))

  val topic = "one-partition-multiple-consumers-in-same-group"

  val group = topic
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