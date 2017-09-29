---
layout: default
sidebar: toc
---
<ul>
    {% for item in site.data.navigation[page.sidebar] %}
      <li><a href="{{ item.url }}">{{ item.title }}</a></li>
    {% endfor %}
</ul>

## Big Data Glue (Version 2)
BDGlue2 (like the original BDGlue) is intended to be a general purpose library
for delivering data from Java applications into various Big Data targets
in a number of different data formats. The idea was to create a “one stop
shop” of sorts to facilitate easy exploration of different technologies to
help users identify what might be the most appropriate approach in any
particular case. The overarching goal was to allow this experimentation to
occur without the user having to write any Big Data-specific code.
Big Data targets include Flume, Kafka, HDFS, Hive, HBase, Oracle NoSQL,
Cassandra, Google BigQuery, and others. Currently, BDGlue processes data
captured in real-time by Oracle GoldenGate from relational database sources.
The intent over time is to add interfaces to process non-GoldenGate sources as well.

_Author's Note: I wrote the original "Big Data Glue" (a.k.a. BDGlue) while I
was employed at Oracle (you can find the original version at
https://github.com/oracle/bdglue) and was required to post it in Oracle's GitHub repository. 
I have since left Oracle and no longer have access to commit changes. As it was 
"my" project, it is no longer being actively
maintained.  I have ideas for additional capabilities and have created
"Big Data Glue V2" (a.k.a. BDGlue2) to support these enhancements going forward._


