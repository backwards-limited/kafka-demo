package com.backwards.kafka.scenarios

import scala.language.postfixOps
import io.circe.generic.auto._
import org.apache.kafka.clients.admin.NewTopic
import com.backwards.adt.Foo
import com.backwards.kafka.serialization.circe.Serializer._
import com.backwards.kafka.serialization.circe.Deserializer._

/**
  * Creating topics with `replicationFactor` > 1 requires a cluster of at least 2 brokers
  * e.g. use docker-compose-cluster.yml
  */
object OnePartitionMultipleConsumersInSameGroup extends App with ScenarioFixture {
  val topic: NewTopic = createTopic("one-partition-multiple-consumers-in-same-group", numberOfPartitions = 1, replicationFactor = 1)

  val consume = doConsume[Foo]("adt-group")
  val produce = doProduce(Foo("some-thing"))

  runSyncUnsafe(consume("(A)", topic.name), consume("(B)", topic.name), produce(topic.name))
}