---
title: The JSON Encoder
permalink: /docs/json-encoder/
---
“JSON” is short for “JavaScript Object Notation”. It is a lightweight 
data interchange format that uses human-readable text to transmit data 
comprised of attribute-value pairs. It is language-independent and has proven 
to be quite useful in many Big Data use cases. In the case of this encoder, the 
“attributes” are the column/field names, and the “values” are the 
actual data values associated with those names.

Here is an example of JSON-encoded data:

    {"ID":"2871","NAME":"Dane Nash","GENDER":"Male","CITY":"Le Grand-Quevilly", 
    "PHONE":"(874)373-6196","OLD_ID":"1","ZIP":"81558-771","CUST_DATE":"2014/04/13"}


Column/field names are ID, NAME, GENDER, CITY, and so on.

To tell BDGlue to make use of the JSON Encoder, simply specify the encoder in 
the _bdglue.properties_ file as follows:

    bdglue.encoder.class = bdglue2.encoder.JsonEncoder


