<!--
#3> <> prov:wasDerivedFrom <https://github.com/timrdf/csv2rdf4lod-automation/wiki/Krextor>;
#3> .
-->
<!DOCTYPE rdf:RDF [
<!ENTITY rdf "http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<!ENTITY rdfs "http://www.w3.org/2000/01/rdf-schema#">
<!ENTITY dc "http://purl.org/dc/elements/1.1/">
<!ENTITY foaf "http://xmlns.com/foaf/0.1/">
]>

<xsl:transform version="2.0" 
               xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
               xmlns:krextor="http://kwarc.info/projects/krextor"
               xmlns:xd="http://www.pnp-software.com/XSLTdoc"
               xmlns:rat="java:edu.rpi.tw.data.rdf.utils.pipes.starts.Cat"
               xmlns:eparams="java:edu.rpi.tw.data.csv.impl.DefaultEnhancementParameters"
                      exclude-result-prefixes="">

<xd:doc>Path to RDF encoding of enhancement parameters.</xd:doc>
<xsl:param name="eparams-ttl" select="'../e1.params.ttl'"/>

<xd:doc>Java object representing the RDF encoding of enhancement parameters.</xd:doc>
<xsl:variable name="eParamsRep" select="rat:load($eparams-ttl)"/>

<xd:doc>Java object that calculates namespaces.</xd:doc>
<xsl:variable name="eParams" select="eparams:new($eParamsRep)"/>

<xsl:template match="Award" mode="krextor:main">
  <xsl:call-template name="krextor:create-resource">
    <xsl:with-param name="subject" select="concat(eparams:getURIOfVersionedDataset($eParams),AwardID)"/>
    <xsl:with-param name="type" select="'&foaf;Award'"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="person/@friends" mode="krextor:main">
  <xsl:call-template name="krextor:add-uri-property">
    <xsl:with-param name="property" select="'&foaf;knows'"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="person/name" mode="krextor:main">
  <xsl:call-template name="krextor:add-literal-property">
    <xsl:with-param name="property" select="'&foaf;name'"/>
  </xsl:call-template>
</xsl:template>

</xsl:transform>
