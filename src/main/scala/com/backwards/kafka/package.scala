package com.backwards

import java.io.File
import java.util.UUID
import monix.kafka.{KafkaConsumerConfig, KafkaProducerConfig}
import monocle.macros.syntax.lens._
import com.backwards.adt.Merge.ops._
import com.typesafe.scalalogging.LazyLogging

package object kafka extends LazyLogging {
  def kafkaProducerConfig: KafkaProducerConfig = {
    import monix.kafka.KafkaProducerConfig.{default, loadFile, loadResource}

    val configure: KafkaProducerConfig => String => KafkaProducerConfig =
      config => message => {
        val configWithOverrides = (default merge config).lens(_.clientId).modify(id => if (id.isEmpty) UUID.randomUUID().toString else id)
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
        val configWithOverrides = (default merge config).lens(_.clientId).modify(id => if (id.isEmpty) UUID.randomUUID().toString else id)
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
}