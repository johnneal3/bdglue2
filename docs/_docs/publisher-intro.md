---
title: BDGlue Publishers
permalink: /docs/publisher-intro/
---
A publisher is responsible for understanding how to interface with an external 
“target”. Another way of saying that is that a publisher is specific to its 
intended target. A publisher takes the data and associated meta-data handed off 
from the encoder and delivers it to the target. As mentioned previously, 
publishers are part of a “pool”, with each publisher having its own 
independent connection to the target, most typically via an RPC. 

In some cases, the publisher will deliver the data received from the encoder 
“as is” to the target. It will hand off these encoded records without 
really understanding their contents, just knowing that it needs to pass them 
along. Examples of publishers where encoded data would likely be passed along 
as provided by the encoder without further interpretation include Flume, Kafka, 
the Oracle NoSQL KV API, etc.

The primary exception to this would be data passed along via the “null 
encoder”. In this particular case, it is intended that the publisher process 
the data field-by-field as it writes to the target. Examples of publishers that 
would leverage data passed along from the “null encoder” include HBase, the 
Oracle NoSQL Table API, Cassandra, etc. In each of these cases, data is added 
to stored records on a field-by-field basis, so a pre-formatted record based on 
JSON, Avro, etc. are likely not appropriate.

Finally, just as it was designed to support development of new “encoders”, 
BDGlue was designed to be extended to support new publishers as well. Just as 
with Encoders, this is done by implementing a Java interface, and just as with 
new encoders this can be done without the need to make changes elsewhere in the 
code. More information pertaining to creating new publishers can be found in 
[Building a Custom Publisher](../custom-publisher) in the 
[BDGlue Developers Guide](../dev-overview) section found later in this document.

