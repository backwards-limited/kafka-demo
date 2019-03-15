package com.backwards.kafka.scenario.one

import scala.language.postfixOps
import io.circe.generic.auto._
import org.apache.kafka.clients.admin.NewTopic
import com.backwards.adt.Foo
import com.backwards.kafka.scenario.ScenarioFixture
import com.backwards.kafka.serialization.circe.Deserializer._
import com.backwards.kafka.serialization.circe.Serializer._

/**
  * Creating topics with `replicationFactor` > 1 requires a cluster of at least 2 brokers
  * e.g. use docker-compose-cluster.yml
  */
object OnePartitionOneConsumer extends App with ScenarioFixture {
  val topicName = "one-partition-one-consumer"
  val groupName = s"$topicName-group"

  val topic: NewTopic = createTopic("one-partition-one-consumer", numberOfPartitions = 1, replicationFactor = 1)

  val consume = doConsume[Foo](groupName, topic.name)
  val produce = doProduce(Foo("some-thing"), topic.name)

  runSyncUnsafe(consume, produce)
}