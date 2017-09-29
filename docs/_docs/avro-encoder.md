---
title: The Avro Encoder
permalink: /docs/avro-encoder/
---
This data encoding is a bit more advanced than the others. Avro is a data 
serialization format that supports rich data structures in a compact binary 
data format. It has proven to be quite useful, and is understood directly by 
Hive, Oracle NoSQL, and other targets. Avro also supports the notion of 
“schema evolution”, albeit in a more limited sense than might be supported 
by a relational database.

Unlike JSON, which is text-based and self-describing, Avro data is actually 
transmitted downstream to recipients in a more compact binary format based on 
an “Avro schema” that describes the contents. Like JSON-formatted data, 
this data also has a clearly defined structure, but it is different in that the 
“schema” that describes the data must be made available to the recipient so 
that the data can be understood. Avro schemas are actually defined using JSON.

Here is an example of what an Avro schema file looks like. As mentioned, it is 
a JSON format that describes the columns and their data types. Notice the 
“union” entries that contain “null” and a data type. These indicate 
that those columns may be null. Note also the specification of default values: 
“null” for columns that may be null; -1 for the OLD_ID column which in this 
case may not be null; etc. Inclusion of the null column information and default 
values is optional and specified in the properties file. It is recommended that 
these always be enabled as the information assists the target repository (HDFS, 
Hive, NoSQL, etc.) in the schema evolution process.

```{
  "type" : "record",
  "name" : "CUST_INFO",
  "namespace" : "bdglue",
  "doc" : "SchemaDef",
  "fields" : [ {
    "name" : "ID",
    "type" : "int",
    "doc" : "keycol"
  }, {
    "name" : "NAME",
    "type" : [ "null", "string" ],
    "default" : null
  }, {
    "name" : "GENDER",
    "type" : [ "null", "string" ],
    "default" : null
  }, {
    "name" : "CITY",
    "type" : [ "null", "string" ],
    "default" : null
  }, {
    "name" : "PHONE",
    "type" : [ "null", "string" ],
    "default" : null
  }, {
    "name" : "OLD_ID",
    "type" : "int",
    "default" : -1
  }, {
    "name" : "ZIP",
    "type" : [ "null", "string" ],
    "default" : null
  }, {
    "name" : "CUST_DATE",
    "type" : [ "null", "string" ],
    "default" : null
  } ]
}
```

For relational database sources, a utility _“SchemaDef”_ is provided with 
BDGlue that will generate the Avro schema files that would correspond to a 
table from the table’s metadata. _SchemaDef_ will also generate 
meta-information in other formats as well. _SchemaDef_ is described later in this 
document.

To tell BDGlue to make use of the Avro Encoder, simply specify the encoder in 
the _bdglue.properties_ file as follows:

    bdglue.encoder.class = bdglue2.encoder.AvroEncoder


