---
title: Installing BDGlue
permalink: /docs/installation/
---


For convenience, BDGlue can be obtained from GitHub in source form and can 
easily be compiled from there. The net result of the compilation process will 
be a bdglue-specific *.jar file, jar file dependencies needed to compile and 
execute, as well as documentation, example properties files, etc.

Note that BDGlue is configured to build with Maven, and a suitable pom.xml file 
is included for this purpose. For those unfamiliar with Maven, there is a 
traditional Makefile provided which invokes Maven under the covers. Being 
somewhat old school, while Maven is great for compiling everything and 
assembling the dependencies, we prefer calling Maven from make (gmake actually) 
as the install step is a bit more straightforward to comprehend as it copies 
all of the relevant build artifacts to a “deploy” directory.

Note in either case, you will need to set two environment variables:

    # GGBD_HOME is the directory where GG for Big Data in installed.
    # For example, if GG for Big Data is installed at /u01/ggbd12_3, 
    # then you would set
    
    export GGBD_HOME=/u01/ggbd12_3


And

    # GGBD_VERSION is an environment variable set to the version of the 
    # ggdbutil-VERSION.jar file found in the $GGBD_HOME/ggjava/resources/lib
    # directory. For example, if the file is named ggdbutil-12.3.0.1.0.012.jar, 
    # then you would set
    
    export GGBD_VERSION=12.3.0.1.0.012

Download.

    # create a directory where you want to install the files
    [ogg@bigdatalite ~]$ mkdir bdglue
    [ogg@bigdatalite ~]$ cd bdglue
    [ogg@bigdatalite ~]$ git clone https://github.com/johnneal3/bdglue2
    [ogg@bigdatalite ~]$ export GGBD_HOME=/path/to/gg4bigdata
    [ogg@bigdatalite ~]$ export GGBD_VERSION=12.3.0.1.0.12


Build with Make:

    [ogg@bigdatalite ~]$ make
    mvn package -Dggbd.VERSION=12.3.0.1.0.012 -Dggbd.HOME=/u01/ggbd12_3
    [INFO] Scanning for projects...
    [INFO]                                                                         
    [INFO] ------------------------------------------------------------------------
    [INFO] Building bdglue 1.2.0.0
    [INFO] ------------------------------------------------------------------------
    [INFO] 
    [INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ bdglue ---
    [INFO] Using 'UTF-8' encoding to copy filtered resources.
    [INFO] Copying 2 resources
    [INFO] Copying 2 resources
    < -- snip -- >
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 12.987 s
    [INFO] Finished at: 2016-06-08T16:02:03-04:00
    [INFO] Final Memory: 40M/314M
    [INFO] ------------------------------------------------------------------------
    mkdir –p ./deploy/lib/dependencies ./deploy/doc
    cp ./target/bdglue*.jar ./deploy/lib
    cp ./target/dependencies/*.jar ./deploy/lib/dependencies
    cp -R ./target/apidocs ./deploy/doc
    cp ./*.pdf ./deploy/doc
    [ogg@bigdatalite ~]$


Build with Maven:

    [ogg@bigdatalite ~]$ 
    [ogg@bigdatalite ~]$ mvn package -Dggbd.VERSION=$GGBD_VERSION 
    -Dggbd.HOME=$GGBD_HOME
    [ogg@bigdatalite ~]$ 

**CAUTION: Be sure that the versions of the java dependencies (i.e. Kafka, Avro, 
Cassandra, HBase, etc.) that BDGlue builds with are compatible with the version 
of those target solutions deployed in your environment. In many cases, the 
dependencies will be forward / backward compatible, but not always. If you have 
difficulties at run time, whether exceptions related to methods not being 
found, or unidentifiable failures, this could very well be the cause. You may 
need to alter the java dependencies in the pom.xml file (used for building), or 
install newer versions of the target solution in your environment.**

