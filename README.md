Big Data Glue V2
=====
Author's Note: I wrote the original "Big Data Glue" (a.k.a. BDGlue) while I 
was employed at Oracle (you can find the original version at 
https://github.com/oracle/bdglue).  I have since left Oracle and no longer 
have access to that repsoitory to commit changes. I have ideas for additional 
capabilities and have created "Big Data Glue V2" (a.k.a. BDGlue2) to 
support these enhancements going forward.

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
My goal over time is to add interfaces to process non-GoldenGate sources as well.


## Building this project
First, get this repository into your local environment:

        git clone https://github.com/johnneal3/bdglue2

Simply type ``make`` from the command line. Under the covers, ``make``
will be calling Maven, but everyone understands ``make``.

In order to build this, you will need to install GoldenGate and then
configure two environment variables:

* ``GGBD_HOME`` is the directory where GG for Big Data in installed. For 
  example, if GG for Big Data is installed at ``/u01/ggbd12_2``, then you 
  would set ``GGBD_HOME=/u01/ggbd12_2``.
* ``GGBD_VERSION`` is an environment variable set to the version of the 
  ggdbutil-VERSION.jar file found in the ``GGBD_HOME/ggjava/resources/lib`` 
  directory.  For example, if the file is named ``ggdbutil-12.2.0.1.0.012.jar``, 
  then you would set ``GGBD_VERSION=12.2.0.1.0.012``.


Assumptions: gmake, Maven, and Java SE 8 are all installed and 
configured.
