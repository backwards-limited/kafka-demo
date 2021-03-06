package com.backwards

import java.io.File
import java.util.{Properties, UUID}
import monix.kafka.{KafkaConsumerConfig, KafkaProducerConfig}
import monocle.macros.syntax.lens._
import org.apache.kafka.streams.StreamsConfig
import com.backwards.adt.Merge.ops._
import com.typesafe.scalalogging.LazyLogging

// TODO - Rewrite
package object kafka extends LazyLogging {
  val randomWhenEmpty: String => String = { s =>
    if (s == null || s.isEmpty) UUID.randomUUID().toString else s
  }

  def kafkaProducerConfig: KafkaProducerConfig = {
    import monix.kafka.KafkaProducerConfig.{default, loadFile, loadResource}

    val configure: KafkaProducerConfig => String => KafkaProducerConfig =
      config => message => {
        val configWithOverrides = (default merge config).lens(_.clientId).modify(randomWhenEmpty)
        logger info s"$message: ${pprint.apply(configWithOverrides).render}"
        configWithOverrides
      }

    Option(System.getProperty("config.file")).map(new File(_)) match {
      case Some(file) if file.exists() =>
        configure(loadFile(file, "kafka"))(s"Loaded Kafka producer configuration from file ${file.getAbsolutePath}")

      case None =>
        Option(System.getProperty("config.resource")) match {
          case Some(resource) =>
            configure(loadResource(resource, "kafka"))(s"Loaded Kafka producer configuration from resource $resource")

          case None =>
            configure(default)("Loaded default Kafka producer configuration")
        }
    }
  }

  def kafkaConsumerConfig: KafkaConsumerConfig = {
    import monix.kafka.KafkaConsumerConfig.{default, loadFile, loadResource}

    val configure: KafkaConsumerConfig => String => KafkaConsumerConfig =
      config => message => {
        val configWithOverrides = (default merge config).lens(_.clientId).modify(randomWhenEmpty)
        logger info s"$message: ${pprint.apply(configWithOverrides).render}"
        configWithOverrides
      }

    Option(System.getProperty("config.file")).map(new File(_)) match {
      case Some(file) if file.exists() =>
        configure(loadFile(file, "kafka"))(s"Loaded Kafka consumer configuration from file ${file.getAbsolutePath}")

      case None =>
        Option(System.getProperty("config.resource")) match {
          case Some(resource) =>
            configure(loadResource(resource, "kafka"))(s"Loaded Kafka consumer configuration from resource $resource")

          case None =>
            configure(default)("Loaded default Kafka consumer configuration")
        }
    }
  }

  def kafkaStreamsConfig(applicationId: String = UUID.randomUUID().toString): Properties = {
    val properties = new Properties()
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId)
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerConfig.bootstrapServers.mkString(","))
    logger info s"Loaded Kafka streams configuration: $properties"
    properties
  }
}