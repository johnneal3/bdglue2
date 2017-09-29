---
title: Building a Custom Encoder
permalink: /docs/custom-encoder/
---
Information pertaining to building a custom Encoder will go here. Encoders are created by implementing the interface bdglue2.encoder.BDGlueEncoder. 

```
package bdglue2.encoder;
import bdglue2.meta.transaction.DownstreamOperation;
import java.io.IOException;
public interface BDGlueEncoder {
    /**
     * @param op
     * @return the encoded operation
     * @throws IOException
     */
    public EventData encodeDatabaseOperation(DownstreamOperation op) throws IOException;
    /**
     * @return the EncoderType for this encoder.
     */
    public EncoderType getEncoderType();
}
```

More specific details will follow in a future revision of this document. You can of course review the source code for examples.

