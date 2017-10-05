---
title: Using Big Data Glue

permalink: /docs/home/
redirect_from: /docs/index.html
---

“Big Data Glue” (a.k.a. BDGlue) was developed as a general purpose library 
for delivering data from Java applications into various Big Data targets in a 
number of different data formats. The idea was to create a “one stop shop” 
of sorts to facilitate easy exploration of different technologies to help users 
identify what might be the most appropriate approach in any particular case. 
The overarching goal was to allow this experimentation to occur without the 
user having to write any Big Data-specific code. Big Data targets include 
Flume, Kafka, HDFS, Hive, HBase, Oracle NoSQL, Cassandra, and others.

Hadoop and other Big Data technologies are by their very natures constantly 
evolving and infinitely configurable. It is unlikely that BDGlue will exactly 
meet the requirements of a user’s intended production architecture, but it 
will hopefully provide a good starting point for many and at a minimum should 
prove sufficient for early point proving exercises.

The code was developed using a single node virtual machine based on a
Hadoop distribution from Cloudera. However, BDGlue does not leverage any 
capabilities specific to Cloudera's distribution and will work equally 
well with any standard Hadoop distribution. BDGlue has been used in 
production delivering to clusters based on distributions from Cloudera, 
Hortonworks, and MapR.

## Source Code

The source code for BDGlue is freely available and may be found on GitHub at: 

<http://github.com/johnneal3/bdglue2>

## Licensing

BDGlue is developed as open source and released under the Apache License, 
Version 2.0. Most external components that it interfaces with are licensed in 
this fashion as well, with a few exceptions which are called out explicitly in 
the LICENSE and NOTICE files that accompany the source code. In those 
situations, their corresponding licenses have been deemed compatible with 
Apache 2.0.

While BDGlue is open source, it is up to the user to 
determine if source and/or target environments are subject to license fees from 
their vendors.  For example, the GoldenGate Adapter for Java must be licensed 
if you make use of the GoldenGate source as described at the end of this 
document, as well as the GoldenGate CDC capabilities in the source environment. 
In short, just because BDGlue provides an interface to a technology, it 
doesn’t imply that access is inherently free.

## Disclaimer	

While it is intended to be useful out of the box, the source code is provided 
as is. While quite functional and has been deployed in production environments
on many occasions, it is not warranted as “production ready” 
and the onus for ensuring it is so remains the user’s responsibility. 
Source code is available so that users may alter it as needed to meet their 
specific needs.

