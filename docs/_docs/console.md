---
title: The Console Publisher
permalink: /docs/console-publisher/
---
The first, and simplest, publisher we will cover is the “Console 
Publisher.” It was developed to assist with certain troubleshooting 
processes, particularly in areas pertaining to ensuring that things are 
configured properly before we actually start trying to “publish” data to a 
target. The Console Publisher simply takes the records that are passed to it 
and writes them to standard out (the “console”). Because it is writing to 
what could very well be a display screen, configuring the JSON Encoder when 
using the Console Publisher is probably best … records are more easily 
readable.

Here is how you might configure BDGlue’s properties file to use the Console 
Publisher.

    # bdglue.properties to make use of the ConsolePublisher.
    #
    
    bdglue.encoder.class = bdglue2.encoder.JsonEncoder
    bdglue.encoder.threads = 2
    bdglue.encoder.tx-optype = false
    bdglue.encoder.tx-timestamp = false
    bdglue.encoder.user-token = false
    
    bdglue.event.header-optype = false
    bdglue.event.header-timestamp = false
    bdglue.event.header-rowkey = false
    bdglue.event.header-avropath = false
    
    bdglue.publisher.class = bdglue2.publisher.console.ConsolePublisher
    bdglue.publisher.threads = 2
    bdglue.publisher.hash = table

