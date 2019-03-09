# Scala

## Run Integration Tests

```bash
$ sbt it:test
```

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