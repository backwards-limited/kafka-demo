package com.backwards.kafka

import java.util.Properties
import scala.collection.JavaConverters._
import org.apache.kafka.clients.admin.{AdminClient, AdminClientConfig, NewTopic}
import com.backwards.collection.JavaOps
import com.backwards.console.Console
import com.backwards.kafka.KafkaAdmin._

trait KafkaAdmin extends JavaOps with Console {
  def adminClient(properties: Properties = default): AdminClient =
    AdminClient create properties

  def createTopic(name: String, numberOfPartitions: Int, replicationFactor: Int, configs: (String, String)*)(implicit adminClient: AdminClient): NewTopic = {
    val topic: NewTopic = newTopic(name, numberOfPartitions, replicationFactor, configs: _*)
    adminClient createTopics topic
    out(s"Topic ${topic.name} configuration", adminClient.describeTopics(topic.name).all.get.asScala.mkString(", "))
    topic
  }

  def newTopic(name: String, numberOfPartitions: Int, replicationFactor: Int, configs: (String, String)*): NewTopic =
    new NewTopic(name, numberOfPartitions, replicationFactor.toShort).configs(configs.toMap[String, String])
}

object KafkaAdmin {
  lazy val default: Properties = {
    val properties = new Properties()
    properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    properties
  }
}