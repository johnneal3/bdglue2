---
title: Schema-related Properties
permalink: /docs/properties-bdglue-schema/
---
The following table lists the properties related to schema formatting that can be specified in the _bdglue.properties_ file. These properties are the same as several properties specified for the SchemaDef utility and should be specified in an identical manner in both places.

| Property | Required | Type | Default | Notes |
| -------- | -------- | ---- | ------- | ----- |
schemadef.replace.invalid_char|No|String|_ (underscore)|Replace non-alphanumeric “special” characters that are supported in table and column names in some databases with the specified character or characters. This is needed because most of the big data targets are much more limited in terms of the characters that are supported. Note that this property begins with schemadef and should be identical to the property specified to the schemadef utility. 
schemadef.replace.invalid_first_char|No|String|x (lower case x)|Prepend this string to table and column names that begin with anything other than an alpha character. This is needed because of limitations on the big data side of things. Set to a null value to avoid this functionality. Note that this property begins with schemadef and should be identical to the property specified to the schemadef utility.
schemadef.replace.regex|No|String|[^a-zA-Z0-9_\\.]|This is a regular expression that contains the characters that *are* supported in the target. (Note: the ^ is required just as in the default). All characters not in this list will be replaced by the character or characters specified in schemadef.replace.invalid_char.  Note that this property begins with schemadef and should be identical to the property specified to the schemadef utility.


