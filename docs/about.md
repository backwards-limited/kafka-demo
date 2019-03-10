# Kafka?

Kafka is a distributed, horizontally-scalable, fault-tolerant, commit log.

An open source stream processing platform featuring:

- High scalability via **clustered brokers** and **partitioning**
- Fault tolerant via **replication (factor)**
- Low latency, high throughput, where messages/events are appended to fast access files
- Persistent append only logs providing automated data retention

So, at its core, Kafka is a distributed fault tolerant log file. However, it is so much more, including:

- Producer and Consumer APIs for many languages (Kafka itself is written in Scala and Java)
- Kafka Streams and Kafka SQL to transform Kafka data with aggregations, filters etc.
- Kafka Connect to integrate with third party systems such as Databases
- Third party plugins to integrate with the likes of Spark Streaming, Storm, Flink etc.

![101](images/101.png)

Applications (**producers**) send messages (**records**) to a Kafka node (**broker**) and said messages are processed by other applications called **consumers**. Said messages get stored in a **topic** (commit log) and consumers subscribe to the topic to receive new messages.

## What are the Two Essentials of any System?

A completely subjective question...

- Low coupling
- High cohesion

## Kafka and Microservices

As microservices have evolved, Kafka has become popular to integrate data between different microservices:

- Asynchronous
  - Realtime
  - Batch

Microservice A ——————————> endpoint, Microservice B

coupled !!!

Microservice A ——— Kafka ———— Microservice B

decoupled

Pics of microservices with endpoints replaced with Kafka



## Advantages / Disadvantages

blah

## Alternatives

blah

