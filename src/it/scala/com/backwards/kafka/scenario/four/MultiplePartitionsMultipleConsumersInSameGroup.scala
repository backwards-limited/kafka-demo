package com.backwards.kafka.scenario.four

import scala.language.postfixOps
import io.circe.generic.auto._
import com.backwards.adt.Foo
import com.backwards.kafka.scenario.Scenario
import com.backwards.kafka.serialization.circe.Deserializer._
import com.backwards.kafka.serialization.circe.Serializer._

/**
  * Run each App (object) in order
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