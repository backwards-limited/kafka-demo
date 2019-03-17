# Schemas

The Schema Registry in conjunction with Kafka is a powerful concept to keep decoupled services working in sync. An Avro schema acts as a contract between services, and said contract is fully controlled via constraints and versioning.

With an available Schema Registry, from the command line, we can post new schemas and list schemas. The Schema Registry will inform us of issues with schemas.

## Post New Schema

```bash
$ curl -X POST -H "Content-Type:
application/vnd.schemaregistry.v1+json" \
    --data '{"schema": "{\"type\": …}’ \
    http://localhost:8081/subjects/Employee/versions
```

## View Schemas

```bash
$ curl http://localhost:8081/subjects
```

## Scala

Take a look at [Schemas.scala](../src/it/scala/com/backwards/kafka/Schemas.scala) which is an application to register the Avro schema [employee.avsc](../src/it/resources/employee.avsc).