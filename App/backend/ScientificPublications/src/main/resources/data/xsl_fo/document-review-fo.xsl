<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:dr="http://www.ftn.uns.ac.rs/scientific-publication"
                xmlns:cmn="http://www.ftn.uns.ac.rs/common"
                xmlns:fo="http://www.w3.org/1999/XSL/Format" version="2.0">
    
    <xsl:template match="/">
        
        <fo:root>
            
            <fo:layout-master-set>
                <fo:simple-page-master master-name="document-review-page">
                    <fo:region-body margin="1in"/>
                    <fo:region-after extent="1in"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            
            <fo:page-sequence master-reference="document-review-page">
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block text-align="end" margin-right="1in" margin-top="20px" font-size="10px">
                        Page <fo:page-number/>
                        of <fo:page-number-citation ref-id="page-number"/>
                    </fo:block>
                </fo:static-content>
                
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="18px" font-weight="bold" text-align="center" margin-bottom="50px">
                        <xsl:value-of select="dr:document-review/dr:title"></xsl:value-of>
                    </fo:block>
                    
                    <fo:block>
                        <fo:block font-weight="bold" margin-bottom="5px" font-size="14px">
                            Authors:
                        </fo:block>
                        <xsl:for-each select="dr:document-review/dr:authors/cmn:author">
                            <fo:block>
                                <xsl:value-of select="cmn:first-name"></xsl:value-of>&#160;<xsl:value-of select="cmn:last-name"></xsl:value-of>,
                                <xsl:value-of select="cmn:institution/cmn:location/cmn:city"></xsl:value-of>,
                                <xsl:value-of select="cmn:institution/cmn:location/cmn:country"></xsl:value-of>
                            </fo:block>
                        </xsl:for-each>
                        
                        <fo:block font-weight="bold" margin-bottom="5px"  margin-top="10px" font-size="14px">
                            Reviewer:
                        </fo:block>
                        <fo:block>
                            <xsl:value-of select="dr:document-review/dr:reviewer/dr:first-name"></xsl:value-of>&#160;<xsl:value-of select="dr:document-review/dr:reviewer/dr:last-name"></xsl:value-of>
                        </fo:block>
                    </fo:block>
                    
                    <fo:block font-weight="bold" font-size="16px" margin-top="30px" margin-bottom="5px">
                        Review:
                    </fo:block>
                    
                    <xsl:for-each select="dr:document-review/dr:sections/cmn:section">
                        <fo:block font-weight="bold" font-size="14px" margin-bottom="5px" margin-top="10px">
                            <xsl:value-of select="cmn:header"></xsl:value-of>
                        </fo:block>
                        <xsl:for-each select="cmn:paragraphs/cmn:paragraph">
                            <fo:block>
                                <xsl:apply-templates/>
                            </fo:block>
                        </xsl:for-each>
                    </xsl:for-each>
                    
                    
                    <fo:block id="page-number"></fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
        
    </xsl:template>
    
    <xsl:template match="dr:document-review/dr:sections/cmn:section/cmn:paragraphs/cmn:paragraph/cmn:underline">
        <fo:inline text-decoration="underline">
            <xsl:apply-templates select="node()"/>
        </fo:inline>  
    </xsl:template>
    
    <xsl:template match="dr:document-review/dr:sections/cmn:section/cmn:paragraphs/cmn:paragraph/cmn:bold">
        <fo:inline font-weight="bold">
            <xsl:apply-templates select="node()"/>
        </fo:inline>  
    </xsl:template>
    
    <xsl:template match="dr:document-review/dr:sections/cmn:section/cmn:paragraphs/cmn:paragraph/cmn:italic">
        <fo:inline font-style="italic">
            <xsl:apply-templates select="node()"/>
        </fo:inline>  
    </xsl:template>
    
</xsl:stylesheet>