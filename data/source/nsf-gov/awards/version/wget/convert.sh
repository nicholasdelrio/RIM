#!/bin/bash
#
#3> <> a conversion:ConversionTrigger; # Could also be conversion:Idempotent;
#3>    foaf:name    "convert.sh";
#3>    rdfs:seeAlso <https://github.com/timrdf/csv2rdf4lod-automation/wiki/Triggers#wiki-3-computation-triggers>;
#3> .
#
# Requires https://github.com/timrdf/csv2rdf4lod-automation/wiki/Krextor

krextor nsf-awards..turtle automatic/2011/1135525.xml 2> /dev/null
