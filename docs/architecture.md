# Architecture

## Topic

A **topic** is a **distributed log file**, where distribution is achieved via **partitions**.

## Partition

Each **partition** is a **file directory** named with the **topic** name and **partiton index**.

## Replication Factor

In a multinode cluster a **replication factor** is set to have multiple copies of each **partition** for **fault tolerance**.

As there will be multiple brokers associated with each **partition** of a given **topic**, one of the brokers will be designated as the **leader**, and the rest are **followers**. Each **follower** can either be an **ISR (In Sync Replica)** or not.