<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:sp="http://www.ftn.uns.ac.rs/scientific-publication"
                xmlns:cmn="http://www.ftn.uns.ac.rs/common"
                xmlns:fo="http://www.w3.org/1999/XSL/Format" version="2.0">
    
    <xsl:template match="/">
        
        <fo:root>
            
            <fo:layout-master-set>
                <fo:simple-page-master master-name="scientific-paper-page">
                    <fo:region-body margin="1in"/>
                    <fo:region-before extent="1in"/>
                    <fo:region-after extent="1in"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            
            <fo:page-sequence master-reference="scientific-paper-page">
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block text-align="center" margin-top="20px" font-size="7px">
                        Received: <xsl:value-of select="sp:scientific-paper/@received"></xsl:value-of> Revised: <xsl:value-of select="sp:scientific-paper/@revised"></xsl:value-of> Accepted: <xsl:value-of select="sp:scientific-paper/@accepted"></xsl:value-of>
                    </fo:block>
                </fo:static-content>
                
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block text-align="end" margin-right="1in" margin-top="20px" font-size="10px">
                        Page <fo:page-number/>
                        of <fo:page-number-citation ref-id="page-number"/>
                    </fo:block>
                </fo:static-content>
                
                <fo:flow flow-name="xsl-region-body">
                    <fo:block margin="0px">
                        <fo:block text-align="center" margin-bottom="50px">
                            <fo:block  font-weight="bold" font-size="20px" margin-bottom="18px">
                                <xsl:value-of select="sp:scientific-paper/sp:header/sp:title"></xsl:value-of>
                            </fo:block>
                            <fo:block>
                                <xsl:for-each select="sp:scientific-paper/sp:header/sp:authors/cmn:author">
                                    <fo:block>
                                        <xsl:value-of select="cmn:first-name"></xsl:value-of>&#160;<xsl:value-of select="cmn:last-name"></xsl:value-of>
                                    </fo:block>
                                    <fo:block font-style="italic" font-size="10px">
                                        <xsl:value-of select="cmn:institution/sp:name"></xsl:value-of>,
                                        <xsl:value-of select="cmn:institution/sp:location/sp:city"></xsl:value-of>
                                        <xsl:value-of select="cmn:institution/sp:location/sp:state"></xsl:value-of>
                                        <xsl:value-of select="cmn:institution/sp:location/sp:country"></xsl:value-of>
                                    </fo:block>
                                    <fo:block margin="2px"> </fo:block>
                                </xsl:for-each>
                            </fo:block>
                        </fo:block>
                        <fo:block font-size="11px" margin-bottom="20px">
                            <fo:block font-weight="bold" font-size="14px" margin-bottom="2px">Abstract</fo:block>
                            <fo:block margin-bottom="2px" text-align="justify">
                                <fo:inline font-weight="bold">Purpose</fo:inline> - <xsl:value-of select="sp:scientific-paper/sp:abstract/sp:purpose"></xsl:value-of>
                            </fo:block>
                            <fo:block margin-bottom="2px" text-align="justify">
                                <fo:inline font-weight="bold">Keywords</fo:inline> - <xsl:value-of select="sp:scientific-paper/sp:abstract/sp:keywords"></xsl:value-of>
                            </fo:block>
                            <fo:block margin-bottom="2px" text-align="justify">
                                <fo:inline font-weight="bold">Paper type</fo:inline> - <xsl:value-of select="sp:scientific-paper/sp:abstract/sp:paper-type"></xsl:value-of>
                            </fo:block>
                        </fo:block>
                        <xsl:for-each select="sp:scientific-paper/sp:sections/cmn:section">
                            <fo:block font-weight="bold" font-size="14px" margin-bottom="10px" margin-top="20px">
                                <xsl:value-of select="cmn:header"></xsl:value-of>
                            </fo:block>
                            <xsl:for-each select="cmn:paragraphs/cmn:paragraph">
                                <fo:block text-indent="20px" font-size="12px" margin-bottom="5px" text-align="justify">
                                    <xsl:apply-templates />
                                </fo:block>
                            </xsl:for-each>
                        </xsl:for-each>
                        <fo:block>
                            <fo:block font-weight="bold" font-size="12px" margin-bottom="5px" margin-top="30px">References</fo:block>
                            <xsl:for-each select="sp:scientific-paper/sp:references/sp:reference">
                                <fo:block margin-bottom="5px" font-size="10px">
                                    <xsl:for-each select="sp:authors/cmn:author">
                                        <xsl:value-of select="cmn:first-name"></xsl:value-of>&#160;<xsl:value-of select="cmn:last-name"></xsl:value-of>
                                    </xsl:for-each>
                                    <xsl:value-of select="sp:year"></xsl:value-of>&#160;
                                    <xsl:value-of select="sp:paper-name"></xsl:value-of>&#160;
                                    <xsl:value-of select="sp:journal"></xsl:value-of>
                                </fo:block>
                            </xsl:for-each>
                        </fo:block>
                        
                    </fo:block>
                    <fo:block id="page-number"></fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    
    <xsl:template match="sp:scientific-paper/sp:sections/cmn:section/cmn:paragraphs/cmn:paragraph/cmn:bold">
        <fo:inline font-weight="bold">
            <xsl:apply-templates select="node()"/>
        </fo:inline>  
    </xsl:template>
    
    <xsl:template match="sp:scientific-paper/sp:sections/cmn:section/cmn:paragraphs/cmn:paragraph/cmn:italic">
        <fo:inline font-style="italic">
            <xsl:apply-templates select="node()"/>
        </fo:inline>  
    </xsl:template>
    
</xsl:stylesheet>