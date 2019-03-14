package com.backwards.kafka.scenario.three

import scala.language.postfixOps
import io.circe.generic.auto._
import com.backwards.adt.Foo
import com.backwards.kafka.scenario.Scenario
import com.backwards.kafka.serialization.circe.Deserializer._
import com.backwards.kafka.serialization.circe.Serializer._

/**
  * Run each App (object) in order
  */
trait MultiplePartitionSingleConsumer extends App with Scenario {
  val topic: String = topicOf[MultiplePartitionSingleConsumer]

  val group: String = topic
}

/**
  * Creating a topic with `replicationFactor` > 1 requires a cluster of at least 2 brokers
  * e.g. use docker-compose-cluster.yml
  */
object CreateTopic extends MultiplePartitionSingleConsumer {
  createTopic(topic, numberOfPartitions = 4, replicationFactor = 1)
}

object Consumer extends MultiplePartitionSingleConsumer {
  doConsume[Foo](group, topic).runSyncUnsafe()
}

object Producer extends MultiplePartitionSingleConsumer {
  doProduce(Foo("some-thing"), topic).runSyncUnsafe()
}