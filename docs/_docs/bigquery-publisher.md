---
title: The BigQuery Publisher
permalink: /docs/bigquery-publisher/
---
Google's BigQuery is a serverless, petabyte scale data store primarly intended for analytics. It is columnar in nature and supports inserts at high volume. While supported via SQL, update and delete operations involve expensive scans to identify matching rows. 

BDGlue supports delivery of data to Google's BigQuery using the BigQuery data streaming API. At present, the streaming API only supports the idea of "inserts". Updates and deletes are not supported. Recommended best pracitce is to include these operations in "insert only" fashion: include a timestamp and an operaton type, and the perform periodic post prcessing via an ETL job to obtain a current snapshot of the data. Data delievered via the streaming API is initially stored in buffers and is *immeidately* available for query. Flushing of data to physical storage occurs in a "lazy" fashion and timing is non-deterministic. We have seen delays on the order of hours, but again this does not impact the ability to query the data that has been delivered.

Properties for configuring this can be found in the [Publisher Properties](../properties-bdglue-publisher) section of this document.

Here is how you might configure the _bdglue.properties_ file to deliver to Google BigQuery:

```
# 
bdglue.encoder.class = bdglue2.encoder.NullEncoder
bdglue.encoder.threads = 2
bdglue.encoder.tablename = false
bdglue.encoder.txid = false
bdglue.encoder.tx-optype = true
bdglue.encoder.tx-timestamp = true
bdglue.encoder.tx-position = false
bdglue.encoder.user-token = false
bdglue.encoder.include-befores = false
bdglue.encoder.ignore-unchanged = false
#
bdglue.event.generate-avro-schema = false
bdglue.event.header-optype = false
bdglue.event.header-timestamp = false
bdglue.event.header-rowkey = true
bdglue.event.header-avropath = false
bdglue.event.header-columnfamily = true
bdglue.event.header-longname = true
#
bdglue.publisher.class = bdglue2.publisher.bigquery.BigQueryPublisher
bdglue.publisher.threads = 2
bdglue.publisher.hash = rowkey
#
bdglue.bigquery.dataset = mytestdb
bdglue.bigquery.batch-size = 1000
bdglue.bigquery.flush-frequency = 500
bdglue.bigquery.insert-only = true
```
