---
title: SchemaDef Properties
permalink: /docs/properties-schemadef/
---
The following table lists the properties that can be specified in the _schemadef.properies_ file.

|Property|Required|Type|Default|Notes|
|--------|--------|----|-------|----|
schemadef.jdbc.driver|Yes|String|com.mysql.jdbc.Driver|The fully qualified class name of the jdbc driver.
schemadef.jdbc.url|Yes|String|jdbc:mysql://localhost/bdglue|The connection URL for JDBC
schemadef.jdbc.username|Yes|String|root|The database user that we will connect as.
schemadef.jdbc.password|Yes|String| prompt|The database user’s password. If this property is set to the value “prompt”, SchemaDef will prompt the user to enter the password from the command line.
schemadef.jdbc.tables|Yes|String|N/A|A whitespace-delimited list of schema.table pairs that we should generate schema/ddl information for. More than one table may be specified per line, and a line may be continued by placing a backslash (‘\’) as the last character of the current line in the file.
schemadef.output.format|No|String|avro|The type of metadata / ddl to generate. Options are: avro, hive_avro, and nosql.
schemadef.output.path|No|String| ./output|The directory where we should store the generated files.
schemadef.numeric-encoding|No|String|double|How to encode numeric, non-integer fields (decimal, numeric types) in the schema: string, double, float.
schemadef.set-defaults|No|Boolean|TRUE|Whether or not to set default values in the generated Avro schema.
schemadef.tx-optype|No|Boolean|TRUE|Include the transaction operation type in a column in the encoded data. Note that this configuration must match the corresponding property in the bdglue.properties file.
schemadef.tx-optype-name|No|String|txoptype|The name of the column to populate the operation type value in. Note that this configuration must match the corresponding property in the bdglue.properties file.
schemadef.tx-timestamp|No|Boolean|TRUE|Include the transaction operation type in a column in the encoded data. Note that this configuration must match the corresponding property in the bdglue.properties file.
schemadef.tx-timestamp-name|No|String|txtimestamp|The name of the column to populate the transaction timestamp value in. Note that this configuration must match the corresponding property in the bdglue.properties file.
schemadef.tx-position|No|Boolean|TRUE|Include details of the operation’s position in the replication flow in a column in the encoded data to allow sorting when transactions are occurring more rapidly than the granularity of the transaction timestamp can support. Note that this configuration must match the corresponding property in the bdglue.properties file.
schemadef.tx-position-name|No|String|txposition|The name of the column to populate the transaction position information in. Note that this configuration must match the corresponding property in the bdglue.properties file.
schemadef.user-token|No|Boolean|TRUE|Populate a field that will contain a comma delimited list of any user tokens that accompany the record in the form of “token1=value, token2=value, …”. Note that this configuration must match the corresponding property in the bdglue.properties file.
schemadef.user-token-name|No|String|usertokens|The name of the field that will contain the list of user-defined tokens. Note that this configuration must match the corresponding property in the bdglue.properties file.
schemadef.tablename|No|Boolean|FALSE|Populate a field that will contain the long version of the table name (schema.table format).
schemadef.tablename-col|No|String|tablename|The name of the field that will contain the table name.
schemadef.txid|No|Boolean|FALSE|Populate a field that will contain a transaction identifier.
schemadef.txid-col|No|String|txid|The name of the field that will contain the transaction identifier.
schemadef.avro-url|No|String|/path/to/avro/schema|Tells the Hive Avro SerDe where to find the avro schema for this table. Required for avro_hive schema generation
schemadef.data-location|No|String|/path/to/avro/data|Tells the Hive Avro SerDe where to find the avro-encoded data files for this table. Required for avro_hive schema generation.
schemadef.cassandra.replication-strategy|No|String|"{ 'class' : 'SimpleStrategy', 'replication_factor' : 1 }"|The replication strategy for the table. Note that this string is passed into SchemaDef and the corresponding CQL that is generated verbatim … it must be syntactically correct.
schemadef.replace.invalid_char|No|String|_ (underscore)|Replace non-alphanumeric 'special' characters that are supported in table and column names in some databases with the specified character or characters. This is needed because most of the big data targets are much more limited in terms of the characters that are supported. This value must be the same as the value specified for the equivalent property in bdglue.properties.
schemadef.replace.invalid_first_char|No|String|x|Prepend this string to table and column names that begin with anything other than an alpha character. This is needed because of limitations on the big data side of things. Set to a null value to avoid this functionality. This value must be the same as the value specified for the equivalent property in bdglue.properties.
schemadef.replace.regex|No|String|[^a-zA-Z0-9_\\.]|This is a regular expression that contains the characters that *are* supported in the target. (Note: the ^ is required just as in the default). All characters not in this list will be replaced by the character or characters specified in schemadef.replace.invalid_char. This value must be the same as the value specified for the equivalent property in bdglue.properties.

