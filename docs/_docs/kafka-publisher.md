---
title: The Kafka Publisher
permalink: /docs/kafka-publisher/
---
Kafka is a fast, scalable, and fault-tolerant publish-subscribe messaging system that is frequently used in place of more traditional message brokers in “Big Data” environments. As with traditional message brokers, Kafka has the notion of a “topic” to which events are published. Data is published by a Kafka “producer”. 

Data written to a topic by a producer can further be partitioned by the notion of a “key”. The key serves two purposes: to aid in partitioning data that has been written to a topic for reasons of scalability, and in our case to aid downstream “consumers” in determining exactly what data they are looking at.

In the case of BDGlue, by default all data is written to a single topic, and the data is further partitioned by use of a key. The key in this case is the table name, which can be used to route data to particular consumers, and additional tell those consumer what exactly they are looking at.

Finally, Kafka supports the notion of “batch” or “bulk” writes using an asynchronous API that accepts many messages at once to aid in scalability. BDGlue takes advantage of this capability by writing batches of messages at once. The batch size is configurable, as is a timeout specified in milliseconds that will force a “flush” in the event that too much time passes before a batch is completed and written.

When publishing events, Kafka is expecting three bits of information: 

* Topic – which will be the same for all events published by an instance of the Kafka Publisher
* Key – which will correspond to the table name that relates to the encoded data
* Body – the actual body of the message that is to be delivered. The format of this data may be anything. In the case of the Kafka publisher, any of the encoded types are supported: Delimited Text, JSON, and Avro.

Note that there are some additional java dependencies required to execute a Kafka publisher beyond those required to actually compile BDGlue and must be added to the classpath in the Java Adapter properties file. In this case, the specific order of the dependencies listed is very important. If you make a mistake here you will like find the wrong entry point into Kafka and results will be indeterminate.

```
#Adapter Logging parameters. 
#log.logname=ggjavaue
#log.tofile=true
log.level=INFO
#
#Adapter Check pointing  parameters
goldengate.userexit.chkptprefix=GGHCHKP_
goldengate.userexit.nochkpt=true
# Java User Exit Property
goldengate.userexit.writers=javawriter
#
# this is one continuous line
javawriter.bootoptions= -Xms64m -Xmx512M 
  -Dlog4j.configuration=ggjavaue-log4j.properties 
  -Dbdglue.properties=bdglue.properties 
  -Djava.class.path=./gghadoop:./ggjava/ggjava.jar
#
#
#Properties for reporting statistics
# Minimum number of {records, seconds} before generating a report
javawriter.stats.time=3600
javawriter.stats.numrecs=5000
javawriter.stats.display=TRUE
javawriter.stats.full=TRUE
#
#Hadoop Handler.  
gg.handlerlist=gghadoop
gg.handler.gghadoop.type=bdglue2.source.gghadoop.GG12Handler
gg.handler.gghadoop.mode=op
gg.classpath=./gghadoop/lib/*:/kafka/kafka_2.10-0.8.2.1/libs/kafka-clients-0.8.2.1.jar:/kafka/kafka_2.10-0.8.2.1/libs/*
```

### Configuring the Kafka Publisher
Configuring the Kafka Publisher is actually very straight-forward:

* Configure an encoder (note that the “NullEncoder” is not supported by this publisher). The encoder must be for one of the actual supported data formats: Avro, JSON, or Delimited.
* Configure the KafkaPublisher.

```
# bdglue.properties file for delivery to Kafka
#
bdglue.encoder.class = bdglue2.encoder.JsonEncoder
bdglue.encoder.threads = 2
bdglue.encoder.tx-optype = false
bdglue.encoder.tx-timestamp = false
bdglue.encoder.tx-position = false
bdglue.encoder.user-token = false
#
bdglue.event.header-optype = false
bdglue.event.header-timestamp = false
bdglue.event.header-rowkey = true
bdglue.event.header-avropath = false
bdglue.event.header-columnfamily = true
bdglue.event.header-longname = false
#
bdglue.publisher.class = bdglue2.publisher.kafka.KafkaPublisher
bdglue.publisher.threads = 2
bdglue.publisher.hash = table
#
bdglue.kafka.topic = goldengate
bdglue.kafka.batchSize = 3
bdglue.kafka.flushFreq = 500
bdglue.kafka.metadata.broker.list = localhost:9092
```

Note that at there are several “bdglue.kafka” properties located toward the bottom of the example above. Only one of those is actually required, and that is the broker list. This is defined in the Kafka documentation and tells the KafkaPublisher which Kafka broker(s) to deliver events to. Information about these and a few other Kafka-related properties can be found in the appendix at the end of this document.

### Using Flume to Deliver Data to Kafka

While in most situations users will configure BDGlue to deliver data to Kafka directly, BDGlue also supports the delivery of data to Kafka by way of Flume. This approach might be useful if there more complicated flow of data required that neither BDGlue nor Kafka can provide on their own. Flume’s ability to fork and merge data flows, or augment the flow with additional processors (called ‘interceptors’) can prove to be extremely powerful when defining the architecture of a data flow.

#### Configuring BDGlue

First we must configure BDGlue to deliver the data to Flume. Just as with the KafkaPublisher, the data must be encoded in one of the supported formats: Delimited Text, Avro, or JSON. You’ll see that the bdglue.properties file is simpler than some as there isn’t much for BDGlue to do other than encode the data and hand it on.

```
# Configuring BDGlue to deliver data to Kafka 
# by way of Flume (bdglue.properties)
#
bdglue.encoder.class = bdglue2.encoder.JsonEncoder
bdglue.encoder.threads = 2
bdglue.encoder.tx-optype = false
bdglue.encoder.tx-timestamp = false
bdglue.encoder.user-token = false
#
bdglue.event.header-optype = false
bdglue.event.header-timestamp = false
bdglue.event.header-rowkey = true
bdglue.publisher.class = bdglue2.publisher.flume.FlumePublisher
bdglue.publisher.threads = 2
bdglue.publisher.hash = rowkey
#
bdglue.flume.host = localhost
bdglue.flume.port = 41414
bdglue.flume.rpc.type = avro-rpc
```

### Validating Delivery to Kafka
Note that for data to be delivered successfully, the Kafka broker must be running when BDGlue attempts to write to it. The broker may be installed and running as a service, or if not, will need to be started by hand. There is a script to do this that can be found in the “bin” directory of the Kafka installation, and a default set of properties can be found in the “config” directory:

```
./bin/kafka-server-start.sh config/server.properties
```

In the Kafka architecture, both the BDGlue KafkaPublisher and the Flume Kafka “sink” serve the role of “Kafka Producer”. In order to see what has been delivered to Kafka, there will need to be a consumer. Kafka has a sample consumer, called the “Console Consumer” which is great for smoke testing the environment. The Console Consumer basically reads messages that have been posted to a topic and writes them to the screen. 

```
./bin/kafka-console-consumer.sh --zookeeper localhost:2181  --topic goldengate --from-beginning --property print.key=true
```

The “print.key” property causes the consumer to print the topic “key” (in our case, the table name) to the console along with the message. Note that if you are going to use the Console Consumer, it would probably be best to configure the JsonEncoder during this time as the data that is output will be in a text-based format.  Data encoded by the AvroEncoder can contain binary data and will not be as legible on your screen.



