---
title: BDGlue Encoders
permalink: /docs/encoder-intro/
---
There are a number of encoders that are inherently part of BDGlue: null, 
delimited text, Avro, and JSON. For the most part, these should prove to be 
sufficient for just about any use case, but BDGlue was designed to be extended 
with additional encoders if needed. This can be accomplished simply by 
implementing a Java interface. New Encoders can be developed and deployed 
without requiring changes to BDGlue itself. More information pertaining to 
creating new encoders can be found in [Building a Custom Encoder](../custom-encoder) 
in the [BDGlue Developers Guide](../dev-overview) section later in this document.

