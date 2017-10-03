---
title: BDGlue Sources
permalink: /docs/source-intro/
---
The concept for BDGlue originated while building a Java-based adapter to allow Oracle GoldenGate to connect to Big Data targets. It quickly became evident that BDGlue had the potential of being far more generally useful, so we made a deliberate effort to decouple the code that is used to tie BDGlue to a source from all of the downstream logic that interfaces with Big Data targets to encourage broader use of the solution.

At present, GoldenGate is the only "source" technology supported by BDGlue, but there are plans to develop other sources such as a flat file reader as time goes on.

