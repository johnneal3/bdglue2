---
title: The Asynchronous Hbase Publisher
permalink: /docs/async-hbase-publisher/
---
Apache HBase is a column-oriented key/value data store built to run on top of the Hadoop Distributed File System (HDFS). An HBase system comprises a set of tables. Each table contains rows and columns, much like a traditional database, and it also has an element defined as a key.

All access to HBase tables must use the defined key.  While similar in nature to a primary key in a relational database, a key in HBase might be used a little differently … defined and based specifically on how the data will be accessed after it has been written. 

An HBase column represents an attribute of an object and in our case likely a direct mapping of a column from a relational database. HBase allows for many columns to be grouped together into what are known as column families, such that the elements of a column family are all stored together. This is different from a row-oriented relational database, where all the columns of a given row are stored together. 

With HBase you must predefine the table schema and specify the column families. However, it is very flexible in that new columns can be added to families at any time, making the schema flexible and therefore able to adapt to changing application requirements.

Currently in BDGlue, we map each source table into a single column family of a corresponding table in HBase, creating a key from the key on the relational side. This may not be the best approach in some circumstances, however. The very nature of HBase cries out for keys that are geared toward that actual way you are likely to access the data via map reduce (which is likely quite different than how you would access a relational table). In some cases, it would be most optimal to combine relational tables that share a common key on the relational side into a single table in HBase, having a separate column family for each mapped table. This is all possible in theory, and a _future version_ of this code may support a JSON-based specification file to define the desired mappings.

### Connecting to HBase via the Asynchronous HBase Publisher

Just as with Kafka, configuring the Asynchronous HBase Publisher very straight-forward:

* Configure the NullEncoder. 
* Configure the AsyncHbasePublisher.

This is done by setting _bdglue.properties_ as follows:

```
# 
# bdglue.properties file for the AsyncHbasePublisher
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
bdglue.publisher.class = bdglue2.publisher.asynchbase.AsyncHbasePublisher
bdglue.publisher.threads = 2
bdglue.publisher.hash = rowkey
#
bdglue.async-hbase.batchSize = 5
bdglue.async-hbase.timeout = 5000
```

### Basic HBase Administration

There are a couple of things we need to do to make sure that HBase is ready to receive data. 
First off, we need to make sure that HBase is running. This requires ‘sudo’ access on Linux/Unix.

```
#>  sudo service hbase-master start
Starting HBase master daemon (hbase-master):               [  OK  ]
HBase master daemon is running 
#>                            [  OK  ]
#>  sudo service hbase-regionserver start
Starting Hadoop HBase regionserver daemon: starting regionserver, logging to /var/log/hbase/hbase-hbase-regionserver-bigdatalite.localdomain.out
hbase-regionserver.
#>
```

And we also need to create the tables in HBase to receive the information we want to write there. If you are not aware, HBase has something called “column families”. All columns reside within a column family, and each table can have multiple column families if desired. For the purpose of this adapter, we are assuming a default name of ‘data’ for the column family, and are putting all columns from the relational source in there.
The example below creates table CUST_INFO having a single column family called ‘data’. Before doing that, we check the status to be sure that we have a region server up and running.

```
[ubuntudev ~]$ 
[ubuntudev ~]$ hbase shell
2014-10-03 17:40:40,439 INFO  [main] Configuration.deprecation: Hadoop.native.lib is deprecated. Instead, use io.native.lib.available
HBase Shell; enter 'help<RETURN>' for list of supported commands.
Type "exit<RETURN>" to leave the HBase Shell
Version 0.96.1.1-cdh5.0.3, rUnknown, Tue Jul  8 15:35:51 PDT 2014
hbase(main):001:0> status
1 servers, 0 dead, 3.0000 average load
hbase(main):007:0> create 'CUST_INFO', 'data'
0 row(s) in 0.8300 seconds
[ubuntudev ~]$
<add some test data ... >
[ubuntudev ~]$
hbase(main):006:0> scan 'CUST_INFO'
ROW                   COLUMN+CELL
 /7021                column=data:CITY, timestamp=1434739949881, value=Le Grand-
                      Quevilly                                                  
 /7021                column=data:CUST_DATE, timestamp=1434739949901, value=2014
                      /04/13                                                    
 /7021                column=data:GENDER, timestamp=1434739949899, value=Male
 /7021                column=data:ID, timestamp=1434739949843, value=7021
 /7021                column=data:NAME, timestamp=1434739949844, value=Dane Nash
 /7021                column=data:OLD_ID, timestamp=1434739949847, value=1
 /7021                column=data:PHONE, timestamp=1434739949846, value=(874) 37
                      3-6196                                                    
 /7021                column=data:ZIP, timestamp=1434739949847, value=81558-771 
 /7022                column=data:CITY, timestamp=1434739949834, value=Carlton
 /7022                column=data:CUST_DATE, timestamp=1434739949838, value=2014
                      /03/14                                                    
 /7022                column=data:GENDER, timestamp=1434739949826, value=Male
 /7022                column=data:ID, timestamp=1434739949892, value=7022
 /7022                column=data:NAME, timestamp=1434739949825, value=Serina Ja
                      rvis                                                      
 /7022                column=data:OLD_ID, timestamp=1434739949835, value=2
 /7022                column=data:PHONE, timestamp=1434739949845, value=(828) 76
                      4-7840                                                    
 /7022                column=data:ZIP, timestamp=1434739949836, value=70179
totalEvents          column=data:eventCount, timestamp=1434739949985, value=\x0
                      0\x00\x00\x00\x00\x00\x00\x02                             
2 row(s) in 0.1020 seconds
hbase(main):009:0>hbase(main):009:0> exit
[ubuntudev ~]$
```



