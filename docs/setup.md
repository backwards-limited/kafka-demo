# Setup

The following setup is for a **local** environment. I'll eventually add sections for AWS and GCP.

We can have Kafka up and running in two different ways.

- Install Kafka and run
- Use Docker

(I'll eventually include a third way with Kubernetes by using Minikube).

## Install Kafka

The following installations are really just to have Kafka CLI tools available to interact with Kafka from the command line. Once installed we can also run Kafka, but throughout the demo we shall actually run Kafka with the help of Docker.

If you don't have [Homebrew](https://brew.sh) on your Mac then first run the following:

```bash
$ /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

First install Java and Scala, along with the Scala build tool sbt:

```bash
$ brew cask install java

$ brew install scala

$ brew install sbt
```

And now install Kafka, which will include Zookeeper:

```bash
$ brew install kafka
```

Zookeeper and Kafka can be booted with the following commands:

```bash
$ zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties

$ kafka-server-start /usr/local/etc/kafka/server.properties
```

## Docker

To run Kafka using Docker… we'll need Docker installed! And Docker Compose.

```bash
$ brew cask install virtualbox

$ brew cask install docker

$ brew install docker-compose
```

Note that since Docker only runs natively on a Linux environment, we need an extra piece of software in our Mac environment named [VirtualBox](https://www.virtualbox.org/).

There is also a Mac Desktop version of Docker which can be installed from:

[https://docs.docker.com/docker-for-mac/install/](https://docs.docker.com/docker-for-mac/install/).

As of writing, there are two **Docker Compose** files which can be given to Docker to run Kafka for us. These files reside under the directory [src/it/resources](../src/it/resources):

- [docker-compose-cluster.yml](../src/it/resources/docker-compose-cluster.yml) - Runs a Kafka cluster with a simple UI from [Landoop](https://www.landoop.com/).
- [docker-compose-lenses.yml](../src/it/resources/docker-compose-lenses.yml) - Runs a single instance of Kafka with a powerful UI from [Landoop](https://www.landoop.com/).

Using Docker Compose we can have Kafka up and running with either of the following commands:

```bash
$ docker-compose -f docker-compose-cluster.yml up
```

```bash
$ docker-compose -f docker-compose-lenses.yml up
```

More about this in the section on [Commands](commands.md).