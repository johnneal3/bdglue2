---
title: The "Null" Encoder
permalink: /docs/null-encoder/
---
The “null” encoder is just what it sounds like … it actually does no 
encoding at all. It is designed to simply take the data that was provided by 
the source, encapsulate it with a little meta-data related to the work that 
needs to be done downstream, and then pass the data along to the publisher. 
Consequently, the null encoder is the most lightweight of the encoders and is 
intended for use against those targets that

* BDGlue will connect to directly (i.e. not via Flume, Kafka, etc.); and

* Require data to be applied via API at the field (or column) level rather than 
at the record level.

Targets for which the null encoder is appropriate include HBase, the Oracle 
NoSQL “table” API, Cassandra, etc.

To tell BDGlue to make use of the Null Encoder, simply specify the encoder in 
the bdglue.properties file as follows:


    bdglue.encoder.class = bdglue2.encoder.NullEncoder


