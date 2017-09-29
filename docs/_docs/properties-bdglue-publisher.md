---
title: Publisher-related Properties
permalink: /docs/properties-bdglue-publisher/
---
The following table lists the properties related to publishers. They specify how data is to be processed as it is written to the target. These are specified in the _bdglue.properties_ file. 

| Property | Required | Type | Default | Notes |
| -------- | -------- | ---- | ------- | ----- |
bdglue.publisher.class|Yes|String|bdglue2.publisher.console. ConsolePublisher|"This is the fully qualified class name (FQCN) of the class that will be called to Publish the data. These Encoders, and any that are custom built, implement the interface bdglue2.publisher.BDGluePublisher. Built-in options are:"
|||||* bdglue2.publisher.console.ConsolePublisher (writes the encoded data to the console. Useful for smoke testing upstream configurations before worrying about actually delivering data to a target. Json encoding is perhaps most useful for this.
|||||* bdglue2.publisher.flume.FlumePublisher (delivers encoded data to Flume).
|||||* bdglue2.publisher.hbase.HBasePublisher (delivers data to HBase. The NullEncoder should be used for this publisher).
|||||* bdglue2.publisher.nosql.NoSQLPublisher (delivers to OracleNoSQL. Use the AvroEncoder for the KV API, and NullEncoder for the Table API).
|||||* bdglue2.publisher.kafka.KafkaPublisher (delivers to Kafka. The AvroEncoder and JsonEncoder are perhaps most useful for this publisher). Note: this publisher uses an older Kafka API and is included for reasons of compatibility.
|||||* bdglue2.publisher.kafka.KafkaRegistryPublisher (delivers to Kafka using the newer Kafka API. This publisher is also compatible with the Confluent “schema registry”, although interfacing with the registry is not strictly required to use this publisher.)
|||||* bdglue2.publisher.cassandra.CassandraPublisher (delivers data to Cassandra. The NullEncoder should be used for this publisher).
|||||* bdglue2.publisher.bigquery.BigQueryPublisher (delivers data to Google's BigQuery. The NullEncoder should be used for this publisher).
bdglue.publisher.threads|No|Integer|2|The number of publishers to run in parallel.
bdglue.publisher.hash|No|String|rowkey|Select the publisher thread to pass an encoded event to based on a hash of either the table name (“table”) or row key (“rowkey”). This is to ensure that changes made to the same row are always handled by the same publisher to avoid any sort of race condition.
bdglue.nosql.host|No|String|localhost|The hostname that we will connect to for NoSQL
bdglue.nosql.port|No|String|5000|The port number where the NoSQL KVStore is listening.
bdglue.nosql.kvstore|No|String|kvstore|The name of the NoSQL KVStore to connect to.
bdglue.nosql.durability|No|String|WRITE_NO_SYNC|The NoSQL durability model for these transactions.  Options are: SYNC, WRITE_NO_SYNC, NO_SYNC.
bdglue.nosql.api|No|String|kv_api|Specify whether to use the “kv_api” or “table_api” when writing to Oracle NoSQL.
bdglue.kafka.topic|No|String|goldengate|The name of the Kafka topic that GoldenGate will publish to.
bdglue.kafka.batchSize|No|Integer|100|"The number of Kafka events to queue before publishing. The default value should be reasonable for most scenarios, but should be decreased to a smaller value for low volume situations, and perhaps made larger in extremely high volume situations. This property only applies to the KafkaPublisher as batching is handled by that publisher directly. Use bdglue.kafka.producer.batch.size for the KafkaRegistryPublisher as batching is handled by the actual Kafka producer logic in that case."
bdglue.kafka.flushFreq|No|Integer|500|The number of milliseconds to allow events to queue before forcing them to be written to Kafka in the event that ‘batchSize’ has not been reached.
bdglue.kafka.serializer.class|No|String|kafka.serializer.DefaultEncoder|The serializer to use when writing the event to Kafka. The DefaultEncoder passes the encoded data received verbatim to Kafka in a byte-for-byte fashion. It is not likely that there will be need to override the default value.
bdglue.kafka.key.serializer.class|No|String|kafka.serializer.StringEncoder|The serializer to use when encoding the Topic “key”. It is not likely that the default value will need to be overridden.
bdglue.kafka.metadata.broker.list|Yes|String|localhost:9092|"A comma-separated list of host:port pairs of Kafka brokers that may be published to. Note that this is for the Kafka broker, not for Zookeeper."
bdglue.kafka.metadata.helper.class|No|String|bdglue2.publisher.kafka. KafkaMessageDefaultMeta|A simple class that implements the KafkaMessageHelper interface. Its purpose is to allow customization of message “topic” and message “key” behavior. Current built-in options are:
|||||* bdglue2.publisher.kafka. KafkaMessageDefaultMeta – writes all messages to a single topic specified in the properties file, and the key is the table name.
|||||* bdglue2.publisher.kafka.KafkaMessageTableKey – publishes each table to a separate topic, where the topic name is the table name, and the message key is a concatenated version of the key columns from the table in this format: /key1/key2/…
bdglue.kafka.request.required.acks|No|Integer|1|0 – write and assume delivery. Don’t wait for response (potentially unsafe); 1 – write and wait for the event to be accepted by at least one broker before continuing; -1 – write and wait for the event to be accepted by all brokers before continuing.
bdglue.cassandra.node|No|String|localhost|The Cassandra node to connect to.
bdglue.cassandra.batch-size|No|Integer|5|The number of operations to group together with each call to Cassandra.
bdglue.cassandra.flush-frequency|No|Integer|500|Force writing of any queued operations that haven’t been flushed due to batch-size after this many milliseconds
bdglue.cassandra.insert-only|No|Boolean|FALSE|Convert update and delete operations to an insert. Note that the default key generated by SchemaDef may need to be changed to include operation type and timestamp if this is set to ‘true’.
bdglue.flume.host|Yes|String|localhost|The name of the target host that we will connect to.
bdglue.flume.port|Yes|Integer|5000|The port number on the host where the target is listening.
bdglue.flume.rpc.retries|No|Integer|5|The number of times to retry a connection after encountering an issue before aborting.
bdglue.flume.rpc.retry-delay|No|Integer|10|The number of seconds to delay after each attempt to connect before trying again.
bdglue.flume.rpc.type|No|String|avro-rpc|Currently only pertinent for Flume. Defines the type of event RPC protocol being used for communication. Options are avro-rpc and thrift-rpc. Avro is most common. Do not confuse avro RPC communication with avro encoding of data. Same name, different things entirely. One does not require the other.
bdglue.bigquery.dataset|Yes|String|default_dataset|The BigQuery dataset name to connect to.
bdglue.bigquery.batch-size|No|Integer|5|The size of the batch to commit. The default value is for testing. BigQuery wants a much larger number for production loads. Try 500 to start.
bdglue.bigquery.flush-frequency|No|Integer|500|The number of milliseconds to wait before forcing a write even if the specified batch size has not been reached.
bdglue.bigquery.insert-only|No|Boolean|true|True if we want to convert deletes and updates into inserts. Assumes that inclusion of operation type and timestamp has been specified in the properties. Note that the data streaming API used by BDGlue doesn't currently support updates or deletes, so at present this value should always be set to 'true'. Reconciliation of these opertaions should be done periodically downstream via an ETL job.







