package com.backwards.kafka.scenario

import java.io.InputStream
import scala.language.higherKinds
import better.files._
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient
import org.apache.avro.Schema
import com.backwards.console.Console

// TODO - Remove hardcoded URIs
object Schemas extends App with SchemaApi {
  out("Registered schemas", schemaRegistry.getAllSubjects)

  register("employee", Resource getAsStream "employee.avsc")

  register("foo", Resource getAsStream "foo.avsc")
}

trait SchemaApi extends Console {
  type SubjectId = Int

  val schemaRegistry = new CachedSchemaRegistryClient("http://localhost:8081", Int.MaxValue)

  val register: (String, InputStream) => SubjectId = { (subject, input) =>
    val subjectId = schemaRegistry.register(subject, new Schema.Parser().parse(input))
    out(s"Registered schema with ID $subjectId", schemaRegistry getById subjectId)
    subjectId
  }
}