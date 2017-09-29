---
title: The Cassandra Publisher
permalink: /docs/cassandra-publisher/
---
Cassandra is a ‘flavor’ of NoSQL database that has a very tabular feel. In fact, the syntax for CQL (Cassandra Query Language) is very similar to SQL. Cassandra has become quite popular for a number of reasons:

* It has a peer-to-peer architecture rather than one based on master-slave configurations. Any number of server nodes can be added to the cluster in order to reliability as there is no single point of failure.
* It boasts elastic scalability by adding or removing nodes from the cluster.
* Cassandra’s architecture delivers high availability and fault tolerance.
* Cassandra delivers very high performance on large sets of data.
* Cassandra is column-oriented, giving a tabular feel to things. Cassandra rows can be extremely wide.
* It has a tunable consistency model ranging from “eventual consistency” to “strong consistency” which ensures that updates are written to all nodes.

The BDGlue Cassandra Publisher makes use of the Cassandra Java API published as Open Source by DataStax. Make sure that the version of Cassandra you are using is compatible with the DataStax Java API. Currently, DataStax claims compatibility with the latest stable release, Cassandra 3.0.x. It is not known at this time if the monthly development releases (currently version 3.5) are compatible or not. Feel free to experiment.

Each column in a Cassandra table will correspond to a column of the same name found in the relational source. Data types are mapped as closely as possible, and default to ‘text’ in situations where there is no direct mapping. Key columns are also mapped directly and in the order they are specified in the DDL (and in the order they are returned by JDBC if you use the SchemaDef utility to generate the DDL). The source schema name will correspond to the Cassandra “key space.”

### Connecting to Cassandra via the Cassandra Publisher

To deliver data to Cassandra, you need to configure the Cassandra Publisher as follows:

* Configure the NullEncoder
* Configure the CassandraPublisher

This is done by setting the values in the bdglue.properties file as follows:

```
bdglue.encoder.class = bdglue2.encoder.NullEncoder
bdglue.encoder.threads = 2
bdglue.encoder.tablename = false
bdglue.encoder.txid = false
bdglue.encoder.tx-optype = true
bdglue.encoder.tx-timestamp = false
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
bdglue.publisher.class = bdglue2.publisher.cassandra.CassandraPublisher
bdglue.publisher.threads = 2
bdglue.publisher.hash = rowkey
#
bdglue.cassandra.node = localhost
bdglue.cassandra.batch-size = 5 
bdglue.cassandra.flush-frequency = 500 
bdglue.cassandra.insert-only = false
```

### Basic Cassandra Administration

This section briefly explains how to start Cassandra from the command line, run the CQL shell, generate DDL that corresponds to the relational source tables, and apply that DDL into Cassandra.

#### Running Cassandra and the CQL Shell
First off, we have to make sure that Cassandra is running. To run Cassandra from the command line, run Cassandra using ‘sudo’.

```
#> cd ./apache-cassandra-3.0.5   # the Cassandra installation directory
#> sudo ./bin/cassandra –f       # runs in the console window. CTRL-C to end.
```

Running the Cassandra shell follows much the same process, but ‘sudo’ is not required.

```
#> cd ./apache-cassandra-3.0.5   # the Cassandra installation directory
#>./bin/cqlsh                    # runs in the console window. CTRL-C to end.
Connected to Test Cluster at 127.0.0.1:9042.
[cqlsh 5.0.1 | Cassandra 3.0.5 | CQL spec 3.4.0 | Native protocol v4]
Use HELP for help.
cqlsh>
```

Note that ‘cqlsh’ is implemented using Python version 2.7. Most Linux systems currently have Python 2.6 installed as the default. Various utilities such as ‘yum’ seem to rely on this version of Python. Note that these versions of Python are not compatible. If you don’t have version 2.7, you will have to install it. The easiest way is to download the Python 2.7 source, build, and then install it into /usr/local/bin. Be careful not to overwrite the default Python 2.6 (likely installed in /usr/bin/…).

#### Creating Tables in Cassandra

As with any “tabular” database, you need to create tables in Cassandra. Cassandra DDL looks much like DDL for a relational database and we have simplified the process of creating table definitions that correspond to the source tables that we will be capturing. This is done using the SchemaDef utility discussed later in this document. See The “SchemaDef” Utility for more information on how to configure and run SchemaDef for Cassandra and other targets.

Here is an example of generated Cassandra DDL:

```
CREATE KEYSPACE IF NOT EXISTS "bdglue" 
    WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
--
DROP TABLE IF EXISTS bdglue.CUST_INFO;
--
CREATE TABLE bdglue.CUST_INFO 
 ( 
   txoptype text,
   ID int,
   NAME text,
   GENDER text,
   CITY text,
   PHONE text,
   OLD_ID int,
   ZIP text,
   CUST_DATE text,
   PRIMARY KEY (ID) 
 );
```

To define the target tables in Cassandra to the following for each generated table definition:

```
#> cd <Cassandra installation directory>
#> bin/cqlsh < ~/ddl/bdglue.CUST_INFO.cql
#>
```

#### Validating Your Schema and Data
To check your schema in Cassandra, you simply do a “describe” as you would against a relational database:

```
cqlsh> describe bdglue.CUST_INFO;
CREATE TABLE bdglue.cust_info (
    id int PRIMARY KEY,
    city text,
    cust_date text,
    gender text,
    name text,
    old_id int,
    phone text,
    txoptype text,
    zip text
) WITH bloom_filter_fp_chance = 0.01
    AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
    AND comment = ''
    AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
    AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
    AND crc_check_chance = 1.0
    AND dclocal_read_repair_chance = 0.1
    AND default_time_to_live = 0
    AND gc_grace_seconds = 864000
    AND max_index_interval = 2048
    AND memtable_flush_period_in_ms = 0
    AND min_index_interval = 128
    AND read_repair_chance = 0.0
    AND speculative_retry = '99PERCENTILE';
cqlsh>
```

And to view some data:

```
cqlsh> select * from bdglue.CUST_INFO limit 2;
 id   | city              | cust_date  | gender | name         | old_id | phone          | txoptype | zip
------+-------------------+------------+--------+--------------+--------+----------------+----------+-----------
 4460 | Le Grand-Quevilly | 2014/04/13 |   Male |    Dane Nash |      1 | (874) 373-6196 |   INSERT | 81558-771
 4462 | Fontaine-l'Evique | 2015/02/06 |   Male | Amos Fischer |      3 | (141) 398-6160 |   INSERT |      9188
(2 rows)
cqlsh>
```



