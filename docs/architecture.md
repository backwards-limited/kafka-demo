# Architecture

## Topic

A **topic** is a **distributed log file**, where distribution is achieved via **partitions**.

## Partition

Each **partition** is a **file directory** named with the **topic** name and **partiton index**.

## Replication Factor

In a multinode cluster a **replication factor** is set to have multiple copies of each **partition** for **fault tolerance**.

As there will be multiple brokers associated with each **partition** of a given **topic**, one of the brokers will be designated as the **leader**, and the rest are **followers**. Each **follower** can either be an **ISR (In Sync Replica)** or not.



A producer, whether a CLI or programmatic client, connects to a Kafka cluster by providing a list of Kafka brokers.

Producers connect to one or more brokers and push messages to topics via the leader.

Upon publishing a message to a particular topic, a client is first given the necessary meta data by the cluster. This meta data shows where the partitions of the topic are and which is the leader, so that the client can send the message to the leader.

Consumers pull messages from a topic by polling at regular intervals. Each time a consumer reads messages it needs to keep track of the (log) offset.



## Zookeeper

Create and manage topics.

Assigns the leader for each partition.