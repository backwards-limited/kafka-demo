# Scala

## Unit Tests

```bash
$ sbt test
```

## Integration Tests

```bash
$ sbt it:test
```

Note that all tests have **no assertions** - for a demo this is for illustration purposes only (assertions are boring in a demo).

---

Kafka really only know about **bytes**.

## ADT

In our application / services, we don't deal in bytes. Ideally with deal with **Algebraic Data Types** (ADT).

When our ADT meets Kafka, we need to **serialize** our ADT to bytes to be able to commit to a Kafka topic. Then upon consuming the bytes we wish to reverse that process and **deserialize** said bytes back to our ADT. However, since services are decoupled, an ADT in one system is probably different from that of another. In fact different systems may well be coded in different languages. So we need our systems to communicate via some form of protocol that is agnostic to the chosen technologies. Examples are **JSON**, **Avro** and **Protobuf**.

## JSON



## Avro

CLI

```bash
$ brew install avro-tools
```

JSON to binary Avro

Without compression
avro-tools fromjson --schema-file twitter.avsc twitter.json > twitter.avro

With Snappy compression
avro-tools fromjson --codec snappy --schema-file twitter.avsc twitter.json > twitter.snappy.avro

Binary Avro to JSON
avro-tools tojson twitter.avro > twitter.json
avro-tools tojson twitter.snappy.avro > twitter.json

Retrieve Avro schema from binary Avro
avro-tools getschema twitter.avro > twitter.avsc
avro-tools getschema twitter.snappy.avro > twitter.avsc