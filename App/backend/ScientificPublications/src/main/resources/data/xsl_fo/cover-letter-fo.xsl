<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cl="http://www.ftn.uns.ac.rs/cover-letter"
                xmlns:fo="http://www.w3.org/1999/XSL/Format" version="2.0">

    <xsl:template match="/">

        <fo:root>

            <fo:layout-master-set>
                <fo:simple-page-master master-name="cover-letter-page">
                    <fo:region-body />
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="cover-letter-page">
                <fo:flow flow-name="xsl-region-body">

                    <!-- Sender contact -->
                    <fo:block margin-left="0.5in" padding-bottom="0.5in">
                        <fo:block>
                            <xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:name"/>
                        </fo:block>
                        <fo:block>
                            <xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:address/cl:street"/>,
                            <xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:address/cl:number"/>
                        </fo:block>
                        <fo:block>
                            <xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:address/cl:city"/>,
                            <xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:address/cl:country"/>
                        </fo:block>
                        <fo:block>
                            <xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:phone-number"/>
                        </fo:block>
                        <fo:block>
                            <xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:email"/>
                        </fo:block>
                    </fo:block>

                    <!-- Receiver contact -->
                    <fo:block margin-left="0.5in">
                        <fo:block>
                            <xsl:value-of select="cl:cover-letter/cl:employer-contact/cl:name"/>
                        </fo:block>
                        <fo:block>
                            <xsl:value-of select="cl:cover-letter/cl:employer-contact/cl:address/cl:street"/>,
                            <xsl:value-of select="cl:cover-letter/cl:employer-contact/cl:address/cl:number"/>
                        </fo:block>
                        <fo:block>
                            <xsl:value-of select="cl:cover-letter/cl:employer-contact/cl:address/cl:city"/>,
                            <xsl:value-of select="cl:cover-letter/cl:employer-contact/cl:address/cl:country"/>
                        </fo:block>
                        <fo:block>
                            <xsl:value-of select="cl:cover-letter/cl:employer-contact/cl:phone-number"/>
                        </fo:block>
                        <fo:block>
                            <xsl:value-of select="cl:cover-letter/cl:employer-contact/cl:email"/>
                        </fo:block>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

</xsl:stylesheet>