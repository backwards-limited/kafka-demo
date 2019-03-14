package com.backwards.kafka.scenario

import io.circe.generic.auto._
import org.apache.kafka.clients.admin.NewTopic
import com.backwards.adt.Foo
import com.backwards.kafka.serialization.circe.Serializer._
import com.backwards.kafka.serialization.circe.Deserializer._

/**
  * Creating topics with `replicationFactor` > 1 requires a cluster of at least 2 brokers
  * e.g. use docker-compose-cluster.yml
  */
object SimplestOnePartitionOneConsumer extends App with ScenarioFixture {
  val topicName = "simplest-one-partition-one-consumer"
  val groupName = s"$topicName-group"

  val topic: NewTopic = createTopic("simplest-one-partition-one-consumer", numberOfPartitions = 1, replicationFactor = 1)

  val consume = doConsume[Foo](groupName, topic.name)
  val produce = doProduce(Foo("some-thing"), topic.name)

  runSyncUnsafe(consume, produce)
}