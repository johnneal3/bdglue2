---
title: The Delimited Text Encoder
permalink: /docs/delimited-encoder/
---
The “delimited text encoder” is also just what it sounds like … it is 
designed to take the data that is passed in from the source and encode it into 
a data “delimited text” fashion. 

Delimited text is the simplest and most straight forward way to transmit the 
data from BDGlue to a target. It is also likely the least useful. The column 
values are added to a buffer that will become the “body” of the data that 
is sent downstream by a publisher. Columns are added to the buffer in the order 
they are represented in the table metadata, with each column separated from the 
one preceding it in the buffer by a delimiter. By default, the delimiter is the 
default delimiter recognized by Hive, which is \001 (^A). That value can be 
overridden in the _bdglue.properties_ file by specifying the 
_bdglue.encoder.delimiter_ property.

Delimited text is fast and somewhat compact, but it contains no metadata 
regarding the structure of the data (i.e. the names of the columns). This 
requires downstream consumers of the data to know the structure when it comes 
time to make use of the data later. This could in theory be a challenge; 
particularly if the schema has evolved over time.

To tell BDGlue to make use of the Delimited Text Encoder, simply specify the 
encoder in the _bdglue.properties_ file as follows:

    bdglue.encoder.class = bdglue2.encoder.DelimitedTextEncoder



