---
title: Building a Custom Publisher
permalink: /docs/custom-publisher/
---
Information pertaining to building a custom Publisher will go here. Publishers are created by implementing the interface bdglue2.publisher.BDGluePublisher. 

```
package bdglue2.publisher;
import bdglue2.encoder.EventData;
public interface BDGluePublisher {
    /**
     * Connect to the target.
     */
    void connect();
    /**
     * Format the event and write it to the target.
     * 
     * @param threadName the name of the calling thread.
     * @param evt the encoded event.
     */
    void writeEvent(String threadName, EventData evt); 
    /**
     * Close connections and clean up as needed.
     */
    void cleanup();
}
```

More specific details will follow in a future revision of this document. You can of course review the source code for specific examples of how to implement a BDGlue publisher.

