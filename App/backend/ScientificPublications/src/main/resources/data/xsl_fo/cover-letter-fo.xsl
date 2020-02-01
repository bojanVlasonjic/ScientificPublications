<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cl="http://www.ftn.uns.ac.rs/scientific-publication"
                xmlns:cmn="http://www.ftn.uns.ac.rs/common"
                xmlns:fo="http://www.w3.org/1999/XSL/Format" version="2.0">

    <xsl:template match="/">
        
        <fo:root>

            <fo:layout-master-set>
                <fo:simple-page-master master-name="cover-letter-page">
                    <fo:region-body margin="1in"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="cover-letter-page">
                <fo:flow flow-name="xsl-region-body">
                    
                    <fo:table margin-bottom="30px">
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell>
                                    <!-- Sender contact -->
                                    <fo:block>
                                        <fo:block margin-bottom="5px">Sender</fo:block>
                                        <fo:block>
                                            <xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:name"/>
                                        </fo:block>
                                        <fo:block>
                                            <xsl:if test="cl:cover-letter/cl:sender-contact/cl:address/cl:street">
                                                <xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:address/cl:street"/>
                                            </xsl:if>
                                            <xsl:if test="cl:cover-letter/cl:sender-contact/cl:address/cl:street and cl:cover-letter/cl:sender-contact/cl:address/cl:number">,
                                            </xsl:if>
                                            <xsl:if test="cl:cover-letter/cl:sender-contact/cl:address/cl:number">
                                                <xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:address/cl:number"/>
                                            </xsl:if>
                                        </fo:block>
                                        <fo:block>
                                            <xsl:value-of select="cl:cover-letter/cl:sender-contact/cmn:location/cmn:city" />,
                                            <xsl:value-of select="cl:cover-letter/cl:sender-contact/cmn:location/cmn:state"/>,
                                            <xsl:value-of select="cl:cover-letter/cl:sender-contact/cmn:location/cmn:country"/>
                                        </fo:block>
                                        <fo:block>
                                            <xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:phone-number"/>
                                        </fo:block>
                                        <fo:block>
                                            <xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:email"/>
                                        </fo:block>
                                    </fo:block>
                                </fo:table-cell>
                                
                                <fo:table-cell>
                                    <!-- Receiver contact -->
                                    <fo:block text-align="right">
                                        <fo:block margin-bottom="5px">Receiver</fo:block>
                                        <fo:block>
                                            <xsl:value-of select="cl:cover-letter/cl:employer-contact/cl:name"/>
                                        </fo:block>
                                        <fo:block>
                                            <xsl:if test="cl:cover-letter/cl:employer-contact/cl:address/cl:street">
                                                <xsl:value-of select="cl:cover-letter/cl:employer-contact/cl:address/cl:street"/>
                                            </xsl:if>
                                            <xsl:if test="cl:cover-letter/cl:employer-contact/cl:address/cl:street and cl:cover-letter/cl:employer-contact/cl:address/cl:number">,
                                            </xsl:if>
                                            <xsl:if test="cl:cover-letter/cl:employer-contact/cl:address/cl:number">
                                                <xsl:value-of select="cl:cover-letter/cl:employer-contact/cl:address/cl:number"/>
                                            </xsl:if>
                                        </fo:block>
                                        <fo:block>
                                            <xsl:value-of select="cl:cover-letter/cl:employer-contact/cmn:location/cmn:city" />,
                                            <xsl:value-of select="cl:cover-letter/cl:employer-contact/cmn:location/cmn:country"/>
                                        </fo:block>
                                        <fo:block>
                                            <xsl:value-of select="cl:cover-letter/cl:employer-contact/cl:phone-number"/>
                                        </fo:block>
                                        <fo:block>
                                            <xsl:value-of select="cl:cover-letter/cl:employer-contact/cl:email"/>
                                        </fo:block>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    
                    <fo:block margin-bottom="5px">
                        <xsl:value-of select="cl:cover-letter/cl:body/cl:salutation"></xsl:value-of>
                    </fo:block>
                    <fo:block margin-bottom="30px" margin-top="30px">
                        
                        <xsl:for-each select="cl:cover-letter/cl:body/cl:paragraphs/cmn:paragraph">
                            <fo:block margin-bottom="10px" text-align="justify">
                                <xsl:apply-templates/>
                            </fo:block>
                        </xsl:for-each>
                    </fo:block>
                    
                    <fo:block margin-bottom="30px" margin-top="30px">
                        <fo:block margin-bottom="5px">
                            <xsl:value-of select="cl:cover-letter/cl:body/cl:closure/cl:compliment"></xsl:value-of>
                        </fo:block>
                        <fo:block margin-bottom="5px">
                            <xsl:value-of select="cl:cover-letter/cl:body/cl:closure/cl:signature"></xsl:value-of>
                        </fo:block>
                    </fo:block>
                </fo:flow>
                
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    
    <xsl:template match="cl:cover-letter/cl:body/cl:paragraphs/cmn:paragraph/cmn:bold">
        <fo:inline font-weight="bold">
            <xsl:apply-templates select="node()"/>
        </fo:inline>  
    </xsl:template>
    
    <xsl:template match="cl:cover-letter/cl:body/cl:paragraphs/cmn:paragraph/cmn:italic">
        <fo:inline font-style="italic">
            <xsl:apply-templates select="node()"/>
        </fo:inline>  
    </xsl:template>

</xsl:stylesheet>