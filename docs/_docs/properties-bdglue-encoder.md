---
title: Encoder-related Properties
permalink: /docs/properties-bdglue-encoder/
---
The following table lists the properties related to encoders. They specify how data is to be formatted, what meta-information about a record should be included, etc. These are specified in the _bdglue.properties_ file. 

| Property | Required | Type | Default | Notes |
| -------- | -------- | ---- | ------- | ----- |
`bdglue.encoder.threads`|No|Integer|`2`|The number of encoder threads to run in parallel.
`bdglue.encoder.class`|Yes|String|`bdglue2.encoder.``JsonEncoder`|The fully qualified class name (FQCN) of the class that will be called to encode the data. These Encoders, and any that are custom built, implement the interface `bdglue2.encoder.BDGlueEncoder`. Built-in options are:
|||||* `bdglue2.encoder.AvroEncoder` : encode in an Avro formatted byte array
|||||* `bdglue2.encoder.AvroGenericRecordEncoder` :  encode an instance of an Avro GenericRecord
|||||* `bdglue2.encoder.DelimtedTextEncoder` : encode in delimited text format
|||||* `bdglue2.encoder.JsonEncoder` : encode in JSON format
|||||* `bdglue2.encoder.NullEncoder` : does not encode the data. This is used when the publisher will not pass along the data as encoded, and instead will apply the data to the target “column-by-column”. Example targets that approach things this way include HBase, Oracle NoSQL Table API, Cassandra, and others.
`bdglue.encoder.delimiter`|No|Integer|`1`|Default is ^A (001). Enter the numeric representation of the desired character (i.e. a semicolon is 073 in octal, 59 in decimal).
`bdglue.encoder.tx-optype`|No|Boolean|`true`|Include the transaction operation type in a column in the encoded data. Note that this configuration must match the corresponding `schemadef.tx-optype` property in the schemadef.properties file. 
`bdglue.encoder.tx-optype-name`|No |String |`txoptype`|The name of the column to populate the operation type value in. Note that this configuration must match the corresponding `schemadef.tx-optype-name` property in the schemadef.properties file. 
`bdglue.encoder.tx-timestamp`|No |Boolean |`true`|Include the transaction operation type in a column in the encoded data. Note that this configuration must match the corresponding `schemadef.tx-timestamp` property in the schemadef.properties file. 
`bdglue.encoder.tx-timestamp-name`|No |String |`txtimestamp` |The name of the column to populate the transaction timestamp value in. Note that this configuration must match the corresponding `schemadef.tx-timstamp-name` property in the schemadef.properties file. 
`bdglue.encoder.tx-position`|No|Boolean|`true`|Include information pertaining to the position of this operation in the transaction flow. This is used to allow sorting of operations when they are occurring more frequently than the granularity of the tx-timestamp. Note that this configuration must match the corresponding `schemadef.tx-position` property in the schemadef.properties file.
`bdglue.encoder.tx-position-name`|No|String|`txposition`|The name of the column to populate the transaction position value in. Note that this configuration must match the corresponding `schemadef.tx-position-name` property in the schemadef.properties file.
`bdglue.encoder.user-token`|No |Boolean |`true`|Populate a field that will contain a comma delimited list of any user tokens that accompany the record in the form of “token1=value, token2=value, …”. This property must be the same as the corresponding `schemadef.user-token` property found for schemadef.
`bdglue.encoder.user-token-name`|No |String |`usertokens`|The name of the field that will contain the list of user-defined tokens. This property must be the same as the corresponding `schemadef.user-token-name` property found for schemadef. 
`bdglue.encoder.tablename`|No|Boolean|`false`|Populate a field with the name of the source table. This will be the “long” table name in schema.table format.
`bdglue.encoder.tablename-col`|No|String|`tablename`|The name of the field to populate with the name of the source table.
`bdglue.encoder.txid`|No|Boolean|`false`|Populate a field with a transaction identifier.
`bdglue.encoder.txid-col`|No|String|`txid`|The name of the field to populate with the transaction identifier.
`bdglue.encoder.replace-newline`|No|Boolean|`false`|Replace newline characters found in string fields with another character. This is needed because newlines can cause problems in some downstream targets.
`bdglue.encoder.newline-char`|No|String|`<space>`|The character to substitute for newlines in string fields. The default is “ “ (a space). Override with another character if needed.
`bdglue.encoder.json.text-only`|No|Boolean|`true`|Whether or not to represent all column values as quoted text strings. When ‘true’, a numeric field would be represented as “ID”:”789”. When false, that same field would be represented as “ID”:789, (no quotes around the value), which allows the downstream JSON parser to know to parse this as a number.
`bdglue.encoder.include-befores`|No|Boolean|`false`|Include the before images representation of all columns when encoding an operation. This option is only supported for JSON encoding at this time and will be ignored by other encoders.
`bdglue.event.header-optype`|No |Boolean |`true` |Include the operation type in the Flume event header 
`bdglue.event.header-timestamp`|No |Boolean |`true` |Include the transaction timestamp in the Flume event header. 
`bdglue.event.header-rowkey`|No|Boolean|`true`|Boolean as to whether or not to include a value for the row's key as a concatenation of the key columns in the event header information. HBase and NoSQL KV API need this. It is also needed if the publisher hash is based on key rather than table name.
`bdglue.event.header-longname`|No|Boolean|`true`|Boolean as to whether or not to include the "long" table name in the header. The long name is normally in the form of “schema.tablename”. FALSE will cause the "short" name (table name only) to be included. Most prefer the long name. HBase and NoSQL prefer the short name.
`bdglue.event.header-columnfamily`|No|Boolean|`true`|Boolean as to whether or not to include a "columnFamily" value in the header. This is needed for Hbase.
`bdglue.event.header-avropath`|No|Boolean|`false`|Boolean as to whether or not to include the path to the Avro schema file in the header. This is needed for Avro encoding where Avro-formatted files are created in HDFS, including those that will be leveraged by Hive.
`bdglue.event.avro-hdfs-schema-path`|No|String|`hdfs:///user/flume/``gg-data/avro-schema/`|The URI in HDFS where Avro schemas can be found. This information is passed along as the header-avropath and is required by Flume when writing Avro-formatted files to HDFS.
`bdglue.event.generate-avro-schema`|No|Boolean|`false`|Boolean on whether or not to generate the avro schema on the fly. This is really intended for testing and should likely always be false. It might be useful at some point in the future to use to support Avro schema evolution. Note that current built-in schema generation capabilities are not on par with those in schemadef.
`bdglue.event.avro-namespace`|No|String|`default`|The namespace to use in avro schemas if the actual table schema name is not present. The table schema name will override.
`bdglue.event.avro-schema-path`|No|String|`./gghadoop/avro`|The path on local disk where we can find the avro schemas and/or where they will be written if we were to generate them on the fly.
