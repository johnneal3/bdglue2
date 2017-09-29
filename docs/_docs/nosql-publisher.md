---
title: The Oracle NoSQL Publisher
permalink: /docs/nosql-publisher/
---
The Oracle NoSQL database  is a leading player in the NoSQL space. Oracle NoSQL Database provides a powerful and flexible transaction model that greatly simplifies the process of developing a NoSQL-based application. It scales horizontally with high availability and transparent load balancing even when dynamically adding new capacity, bringing industrial strength into an arena where it is often found to be lacking.

Some key benefits that the product brings to the Big Data “table” are:

* Simple data model using key-value pairs with secondary indexes
* Simple programming model with ACID transactions, tabular data models, and JSON support
* Application security with authentication and session-level SSL encryption
* Integrated with Oracle Database, Oracle Wallet, and Hadoop
* Geo-distributed data with support for multiple data centers
* High availability with local and remote failover and synchronization
* Scalable throughput and bounded latency

Oracle NoSQL can be used with or without Hadoop. It supports two APIs for storing and retrieving data: the KV (key-value) API, and the Table API. Each API has its own strengths. The KV API is more “traditional”, but the Table API is gaining a lot of momentum in the market. This adapter supports interfacing with Oracle NoSQL with both APIs.

### KV API Support

The KV API writes data to Oracle NoSQL in key-value pairs, where the key is a text string that looks much like a file system path name, with each “node” of the key preceded by a slash (‘/’). For example, keys based on customer names might look like:
,,,
/smith/john
/smith/patty
/hutchison/don
,,,

BDGlue creates the key for each row by concatenating a string representation of each relational column that makes up the primary key in the order that the columns are listed in the relational table’s metadata.

A “value” is data of some sort. It may be text-based, or binary. The structure obviously must be understood by the application. Oracle NoSQL itself is very powerful, however, and there is much database “work” it is able to do if it can understand the data. As it turns out, Oracle NoSQL supports Avro schemas, something that we have already discussed in the context of other Big Data targets. BDGlue makes good use of this Avro encoding.

### Table API Support

The Oracle NoSQL Table API is a different way of storing and accessing data. In theory, you can leverage the same data via the KV and Table APIs, but we are not approaching things in that fashion. The Table API maps data in a “row” on a column-by-column basis.

BDGlue maps the source tables and their columns directly to tables in Oracle NoSQL of essentially the same structure. Key columns are also mapped one-for-one.

### NoSQL Transactional Durability

Before we get to specific configurations, we should also mention at this point the “durability” property, which is applicable to all aspects of this adapter: direct to NoSQL, or via Flume; and for both the Table and KV APIs. Durability effectively addresses “guarantee” that data is safe and sound in the event of a badly timed failure. Oracle NoSQL supports different approaches to syncing transactions once they are committed (i.e. durability). BDGlue supports three sync models:

* **SYNC** : Commit onto disk at master and replicate to simple majority of replicas. This is the most durable. When the commit returns to the caller, you can be absolutely certain that the data will still be there no matter what the failure situation. It is also the slowest.
* **WRITE_NO_SYNC**: Commit onto disk at master but do not wait for data to replicate to other nodes. This is of medium performance as it writes to the master, but doesn’t wait for the data to be replicated before returning to the caller after a commit.
* **NO_SYNC**: Commit only into master memory and do not wait for the data to replicate to other nodes. This is the fastest mode as it returns to the caller immediately upon handing the data to the NoSQL master. At that point, the data has not been synced to disk and could be lost in the event of a failure at the master.

### Connecting Directly to Oracle NoSQL via the NoSQL Publisher

Connecting and delivering data to Oracle NoSQL is not particularly complicated.

#### Configuring for Delivery to the KV API

Delivery to the KV API is straight forward … the key is a concatenated string based on the columns from the source table that comprise the primary key, and the value is an Avro-encoded record containing all of the columns that have been captured, including the key columns.

The first step, obviously, is to configure the _bdglue.properties_ file.

```
# bdglue.properties file for direct connection to 
# Oracle NoSQL via the KV API.
#
bdglue.encoder.class = bdglue2.encoder.AvroEncoder
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
bdglue.event.header-longname = true
bdglue.event.avro-schema-path = ./gghadoop/avro
#
bdglue.publisher.class = bdglue2.publisher.nosql.NoSQLPublisher
bdglue.publisher.threads = 2
bdglue.publisher.hash = rowkey
#
bdglue.nosql.host = localhost
bdglue.nosql.port = 5000
bdglue.nosql.kvstore = kvstore
bdglue.nosql.durability = WRITE_NO_SYNC
bdglue.nosql.api = kv_api
```

The above properties are all that is required. See the admin section Basic Oracle NoSQL Administration for some basic information regarding how to define tables in Oracle NoSQL, etc.

#### Configuring for Delivery via the Table API
BDGlue maps the source tables and their columns directly to tables in Oracle NoSQL of essentially the same structure. Key columns are also mapped one-for-one.

Just as always, the first, and in this case the only thing we need to do is configure the adapter to format and process the data as we expect via the _bdglue.properties_ file. For the NoSQL Table API, we configure the NullEncoder because BDGlue writes the data to NoSQL on a column-by-column basis.

```
# bdglue.properties for delivering directly to 
# the Oracle NoSQL Table API.
#
bdglue.encoder.class = bdglue2.encoder.NullEncoder
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
bdglue.publisher.class = bdglue2.publisher.nosql.NoSQLPublisher
bdglue.publisher.threads = 2	
bdglue.publisher.hash = rowkey
#
bdglue.nosql.host = localhost
bdglue.nosql.port = 5000
bdglue.nosql.kvstore = kvstore
bdglue.nosql.durability = WRITE_NO_SYNC
bdglue.nosql.api = table_api
```

### Basic Oracle NoSQL Administration

Oracle NoSQL comes in two flavors, a “lite” version for basic testing, and an Enterprise version that contains more robust capabilities that enterprise deployments might require. Administratively, they are virtually identical as far as the features we are leveraging in BDGlue … so we keep it simple and test with the “lite” version, called KVLite.

#### Starting Oracle NoSQL from the Command Line
Oracle NoSQL runs as a Java process.

```
#
# starts the kvlite NoSQL instance, listening on default port of 5000
#
[nosqlhome]$ KVHOME="/u01/nosql/kv-ee"
[nosqlhome]$ java -Xmx256m -Xms256m -jar $KVHOME/lib/kvstore.jar kvlite
Opened existing kvlite store with config:
-root ./kvroot -store kvstore -host bigdatalite.localdomain -port 5000 -admin 5001
```

#### Running the KVLite Administration Command Line Interface

The command line utility is where you will define tables, review data stored in Oracle NoSQL, etc.
```
#
# run the kvlite command line interface
#
[nosqlhome]$ KVHOME="/u01/nosql/kv-ee"
[nosqlhome]$ java -Xmx256m -Xms256m -jar $KVHOME/lib/kvstore.jar runadmin -port 5000 -host localhost
kv->
kv-> connect store -name kvstore
Connected to kvstore
Connected to kvstore at localhost:5000.
kv->
```

#### KV API: Creating Tables in Oracle NoSQL

The KV API relies on delivery of key-value pairs. In the case of BDGlue, the key is a concatenation of the columns that make up the key in the relational database source. The “value” is an array of bytes that contain the data we are storing. In our case, we are choosing to encode the “value” in Avro format both because it is a compact representation of the data, and because it self-describes to Oracle NoSQL.

#### KV API: Preparing the Avro Schemas

Preparing the Avro schemas is a two step process:

* Generate the schemas from the source table meta data
* Load the schemas into Oracle NoSQL.

The steps to generate the schemas are exactly as described in _Generating Avro Schemas_. Refer to that section for more information.

Loading the generated schemas into NoSQL is a relatively straight forward process. First you must log into NoSQL with the admin utility. Once you are logged in and have the command prompt, do the following:

```
kv-> ddl add-schema -file ./bdglue.CUST_INFO.avsc -force
Added schema: bdglue.CUST_INFO.1
8 warnings were ignored.    << Ignore if you see this: the result of not setting default values
kv-> ddl add-schema -file ./bdglue.MYCUSTOMER.avsc -force
Added schema: bdglue.MYCUSTOMER.2
22 warnings were ignored. << Ignore if you see this: the result of not setting default values
kv-> show schemas
bdglue.CUST_INFO
  ID: 1  Modified: 2014-10-21 19:19:22 UTC, From: bigdatalite.localdomain
bdglue.MYCUSTOMER
  ID: 2  Modified: 2014-10-21 19:19:53 UTC, From: bigdatalite.localdomain
kv->
```

Once complete, you are ready to capture data from a source database and deliver into the Oracle NoSQL data store.

#### KV API: Validating Your Data

Oracle NoSQL doesn’t provide an easy way to query data stored in KV pairs from the command line. To do this, you need to have the key to the row you want to see.

```
kv-> get kv -key /2978
{
  "ID" : 2978,
  "NAME" : "Basia Foley",
  "GENDER" : "Female",
  "CITY" : "Ichtegem",
  "PHONE" : "(943) 730-2640",
  "OLD_ID" : 8,
  "ZIP" : "T1X 1M5",
  "CUST_DATE" : "2015/01/16"
}
kv->
```

In the example above, the key was “/2798” (note the preceding slash). Also note that Oracle NoSQL understood the structure of the stored value object. This is because we generated and used the Avro schema when writing the key-value pair to the database.

#### Table API: Creating Tables in Oracle NoSQL

Just as with a relational database, you have to create tables in Oracle NoSQL in order to use the Table API. Table creation commands can actually be quite cumbersome, but we have actually simplified the process somewhat by configuring the SchemaDef utility discussed later in this document in _Generating Avro Schemas with SchemaDef_ to generate the NoSQL DDL for us. Just as for Avro, the utility connects to the source database via JDBC. Everything is essentially the same as before except for the output format. 

Here is what the schemadef.properties file might look like:

```
# jdbc connection information
schemadef.jdbc.driver = com.mysql.jdbc.Driver
schemadef.jdbc.url = jdbc:mysql://localhost/bdglue
#
# Oracle JDBC connection info 
#schemadef.jdbc.driver = oracle.jdbc.OracleDriver
#schemadef.jdbc.url = jdbc:oracle:thin:@//<host>:<port>/<service_name>
#
schemadef.jdbc.username = root
schemadef.jdbc.password = welcome1
#
# output format: avro, nosql
schemadef.output.format = nosql
schemadef.output.path = ./output
#
# encode numeric/decimal types as string, double, float
schemadef.numeric-encoding = double
#
schemadef.set-defaults = true
schemadef.tx-optype = false
schemadef.tx-timestamp = false
#
# whitespace delimited list of schema.table pairs
schemadef.jdbc.tables = bdglue.MYCUSTOMER bdglue.CUST_INFO \
                     bdglue.TCUSTORD
```

And the utility is executed just as before:

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
                bdglue2.utility.schemadef.SchemaDef
```

Here is a sample of a generated output file. Each output file contains the script needed to create a table in Oracle NoSQL that corresponds to the source table.

```
## enter into table creation mode 
table create -name CUST_INFO
add-field -type INTEGER -name ID
primary-key -field ID
add-field -type STRING -name NAME
add-field -type STRING -name GENDER
add-field -type STRING -name CITY
add-field -type STRING -name PHONE
add-field -type INTEGER -name OLD_ID
add-field -type STRING -name ZIP
add-field -type STRING -name CUST_DATE
## exit table creation mode 
exit
## add the table to the store and wait for completion 
plan add-table -name CUST_INFO –wait
```

And then we have to add the tables into Oracle NoSQL. We do this from the command prompt in the Oracle NoSQL admin utility.

```
kv-> 
kv-> load -file ./output/CUST_INFO.nosql
Table CUST_INFO built.
Executed plan 5, waiting for completion...
Plan 5 ended successfully
kv->
kv-> load -file ./output/MYCUSTOMER.nosql
Table MYCUSTOMER built.
Executed plan 6, waiting for completion...
Plan 6 ended successfully
kv->
```

And now we are ready to capture data and deliver it into Oracle NoSQL.

#### Table API: Validating Your Data
Looking at data stored with the Table API is a little easier than with data stored with the KV API, but don’t expect the power you might have with a SQL query.

Here is sample output from a single row stored in the CUST_INFO table.

```
kv-> get table -name CUST_INFO  -field ID -value 3204 -pretty
{
  "ID" : "3204",
  "NAME" : "Adria Bray",
  "GENDER" : "Female",
  "CITY" : "Anklam",
  "PHONE" : "(131) 670-1907",
  "OLD_ID" : "94",
  "ZIP" : "27665",
  "CUST_DATE" : "2014/06/01"
}
kv->
```

In this case, we knew the ID column’s value was “3204”.  If you leave off the –field and –value options, you can get all rows in the table, by the way.

Just to further prove the point, here is some example output from the MYCUSTOMER table.

```
kv-> get table -name MYCUSTOMER  -field id -value 2864 -pretty
{
  "id" : "2864",
  "LAST_NAME" : "Barnes",
  "FIRST_NAME" : "Steel",
  "STREET_ADDRESS" : "Ap #325-5990 A Av.",
  "POSTAL_CODE" : "V0S 7A8",
  "CITY_ID" : "14819",
  "CITY" : "Reus",
  "STATE_PROVINCE_ID" : "316",
  "STATE_PROVINCE" : "CA",
  "COUNTRY_ID" : "137",
  "COUNTRY" : "Iran",
  "CONTINENT_ID" : "1",
  "CONTINENT" : "indigo",
  "AGE" : "25",
  "COMMUTE_DISTANCE" : "14",
  "CREDIT_BALANCE" : "2934",
  "EDUCATION" : "Zolpidem Tartrate",
  "EMAIL" : "feugiat.nec@ante.com",
  "FULL_TIME" : "YES",
  "GENDER" : "MALE",
  "HOUSEHOLD_SIZE" : "3",
  "INCOME" : "116452"
}
kv->
```

And there you have it. We have validated that we successfully delivered data into the Oracle NoSQL database via the Table API.


