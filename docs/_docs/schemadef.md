---
title: The SchemaDef Utility
permalink: /docs/schemadef/
---
SchemaDef is a java-based utility that connects to a source database via JDBC and generates metadata relevant to the BDGlue encoding process, the target repository, or both.

### Running SchemaDef

The SchemaDef utility can be found in the jar file for BDGlue. Here is how you would run it:

```
DIR=/path/to/jars
#
CLASSPATH="$DIR/bdglue.jar"
CLASSPATH="$CLASSPATH:$DIR/slf4j-api-1.6.1.jar"
CLASSPATH="$CLASSPATH:$DIR/slf4j-simple-1.7.7.jar"
CLASSPATH="$CLASSPATH:$DIR/commons-io-2.4.jar"
CLASSPATH="$CLASSPATH:$DIR/jackson-core-asl-1.9.13.jar"
CLASSPATH="$CLASSPATH:$DIR/mysql-connector-java-5.1.34-bin.jar"
#
java –Dschemadef.properties=schemadef.properties -cp $CLASSPATH
                com.oracle.bdglue.utility.schemadef.SchemaDef
```

Note that the last jar file listed is specific to the database you will be connecting to. In this case, it is MySQL. Replace this jar file with the jar file that is appropriate for your database type and version. **NOTE: It is up to you to obtain the appropriate JDBC driver for your database platform and version, and to identify the appropriate connection URL and login credentials.**

Details on the various properties that can be configured for SchemaDef can be found in [SchemaDef Properties](../properties-schemadef).

### Generating Avro Schemas with SchemaDef

The first step in the process of generating binary encoded Avro data is to create the Avro schemas for the tables we want to capture. (Note that it is possible to let the BDGlue generate these schemas on the fly, but be aware that new schema files must be copied into HDFS before we start writing the Avro data there that corresponds to that version of the schema. The Avro (de)serialization process requires that the path to the schema file be included in the Avro event header information, and in turn it puts a copy of the schema in the meta data of each generated *.avro file. This allows various Big Data technologies (HDFS, Hive, and more) to always know the structure of the data, and in turn also allows Avro to support schema evolution on a table.

So, the best way to approach this is to generate the Avro schema files (*.avsc) and then copy them into place in HDFS before we start passing data through Flume to HDFS.

To facilitate generation of the Avro Schema files, we created a simple Java utility, SchemaDef, that parses connects to the source database via JDBC and generates the Avro schema files directly from the table definitions of the tables you specify.

Like everything else “java”, the utility is configured via a properties file that looks like this:

```
# jdbc connection information
schemadef.jdbc.driver = com.mysql.jdbc.Driver
schemadef.jdbc.url = jdbc:mysql://localhost/bdgluedemo
#
# Oracle JDBC connection info 
#schemadef.jdbc.driver = oracle.jdbc.OracleDriver
#schemadef.jdbc.url = jdbc:oracle:thin:@//<host>:<port>/<service_name>
#
schemadef.jdbc.username = root
schemadef.jdbc.password = welcome1
#
# output format: avro, nosql, hive_avro
schemadef.output.format = avro
schemadef.output.path = ./avro
#
# encode numeric/decimal types as string, double, float
schemadef.numeric-encoding = double
#
schemadef.set-defaults = true
schemadef.tx-optype = false
schemadef.tx-timestamp = false
schemadef.user-token = false
#
# whitespace delimited list of schema.table pairs
schemadef.jdbc.tables = bdgluedemo.MYCUSTOMER bdgluedemo.CUST_INFO \
                     bdgluedemo.TCUSTORD
```

Details on the properties can be found in the Appendix.

Once the schema files have been created, you then need to copy them locally into HDFS. The following command will do that for you.

```
hdfs dfs -copyFromLocal -f ./output/*.avsc    /user/flume/gg-data/avro-schema
```

### Generating Hive Table Definitions for Use with Avro Schemas

We can generate the Hive Table definitions (DDL) to read *.avro files written to HDFS by simply changing the output format from “avro” to “hive_avro” in the schemadef.properties file and rerun the utility.

```
# jdbc connection information
schemadef.jdbc.driver = com.mysql.jdbc.Driver
schemadef.jdbc.url = jdbc:mysql://localhost/bdgluedemo
#
# Oracle JDBC connection info 
#schemadef.jdbc.driver = oracle.jdbc.OracleDriver
#schemadef.jdbc.url = jdbc:oracle:thin:@//<host>:<port>/<service_name>
#
schemadef.jdbc.username = root
schemadef.jdbc.password = welcome1
#
# output format: avro, nosql, hive_avro
schemadef.output.format = hive_avro
schemadef.output.path = ./avro
#
# encode numeric/decimal types as string, double, float
schemadef.numeric-encoding = double
#
schemadef.set-defaults = true
schemadef.tx-optype = false
schemadef.tx-timestamp = false
schemadef.user-token = false
#
schemadef.avro-url = hdfs:///user/flume/gg-data/avro-schema
schemadef.data-location = /user/flume/gg-data
#
# whitespace delimited list of schema.table pairs
schemadef.jdbc.tables = bdgluedemo.MYCUSTOMER bdgluedemo.CUST_INFO \
                     bdgluedemo.TCUSTORD
```

This will generate an hql file for each specified table that looks like this:

```
CREATE SCHEMA IF NOT EXISTS bdgluedemo;
USE bdgluedemo;
DROP TABLE IF EXISTS CUST_INFO;
CREATE EXTERNAL TABLE CUST_INFO
COMMENT "Table backed by Avro data with the Avro schema stored in HDFS"
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.avro.AvroSerDe'
STORED AS
INPUTFORMAT  'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'
LOCATION '/user/flume/gg-data/bdgluedemo.CUST_INFO/'
TBLPROPERTIES ( 'avro.schema.url'='hdfs:///user/flume/gg-data/avro-schema/bdgluedemo.CUST_INFO.avsc' );
```

### Generating Cassandra Table Definitions

SchemaDef is able to generate appropriate DDL that maps to the source tables for Cassandra just as it does for other targets. All you need to do is specify Cassandra as the target in the schemadef.properties file.

```
# jdbc connection information
# mysql
schemadef.jdbc.driver = com.mysql.jdbc.Driver
schemadef.jdbc.url = jdbc:mysql://localhost/bdglue
schemadef.jdbc.username = root
schemadef.jdbc.password = welcome1
#schemadef.jdbc.password = prompt
#
#schemadef.jdbc.driver = oracle.jdbc.OracleDriver
#schemadef.jdbc.url = jdbc:oracle:thin:@//<host>:<port>/<service_name>
#schemadef.jdbc.url = jdbc:oracle:thin:@//localhost:1521/orcl
#schemadef.jdbc.username = moviedemo
#schemadef.jdbc.password = welcome1
#
# output format: avro, nosql, hive_avro, cassandra
schemadef.output.format = cassandra
schemadef.output.path = ./ddl
#
schemadef.cassandra.replication-strategy = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }
#
schemadef.set-defaults = false
schemadef.tablename = false
schemadef.tx-optype = true
schemadef.tx-timestamp = false
schemadef.tx-position = false
schemadef.user-token = false
#
# whitespace delimited list of schema.table pairs
schemadef.jdbc.tables = bdglue.CUST_INFO     bdglue.MYCUSTOMER  \
                     bdglue.TCUSTORD bdglue.my$Table
```

This will generate a cql file for each table specified. A generated cql file will look like this:

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


