<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rvw="http://www.ftn.uns.ac.rs/scientific-publication"
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
                    <fo:block font-size="14px" font-weight="bold" margin-bottom="5px">
                        Recommendation:
                    </fo:block>
                    <fo:block margin-bottom="20px">
                        <xsl:value-of select="rvw:paper-review/rvw:recommendation"></xsl:value-of>
                    </fo:block>
                    <fo:block font-size="14px" font-weight="bold" margin-bottom="5px">
                        Questions:
                    </fo:block>
                    <fo:block margin-bottom="20px">
                        <xsl:for-each select="rvw:paper-review/rvw:questionare/rvw:question">
                            <fo:block margin-bottom="8px">
                                <fo:block margin-bottom="2px" font-style="italic">
                                    <xsl:value-of select="rvw:question-text"></xsl:value-of>
                                </fo:block>
                                <fo:block margin-bottom="2px">
                                    <xsl:value-of select="rvw:selected"></xsl:value-of>
                                </fo:block>
                            </fo:block>
                        </xsl:for-each>
                    </fo:block>
                    <xsl:if test="rvw:paper-review/rvw:comments-to-editor/rvw:comment">
                        <fo:block font-size="14px" font-weight="bold" margin-bottom="5px">
                            Comments to author:
                        </fo:block>
                    </xsl:if>
                    <fo:block margin-bottom="20px">
                        <xsl:for-each select="rvw:paper-review/rvw:comments-to-author/rvw:comment">
                            <fo:block margin-bottom="2px">
                                <xsl:apply-templates></xsl:apply-templates>
                            </fo:block>
                        </xsl:for-each>
                    </fo:block>
                    
                    <xsl:if test="rvw:paper-review/rvw:comments-to-editor/rvw:comment">
                        <fo:block font-size="14px" font-weight="bold" margin-bottom="5px">
                            Comments to editor:
                        </fo:block>
                    </xsl:if>
                    <fo:block>
                        <xsl:for-each select="rvw:paper-review/rvw:comments-to-editor/rvw:comment">
                            <fo:block margin-bottom="2px">
                                <xsl:apply-templates></xsl:apply-templates>
                            </fo:block>
                        </xsl:for-each>
                    </fo:block>
                    
                    <fo:block id="page-number"></fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
        
    </xsl:template>
    
    <xsl:template match="rvw:paper-review/rvw:comments-to-author/rvw:comment/rvw:italic">
        <fo:inline font-style="italic">
            <xsl:apply-templates select="node()"/>
        </fo:inline>  
    </xsl:template>
    
    <xsl:template match="rvw:paper-review/rvw:comments-to-editor/rvw:comment/rvw:italic">
        <fo:inline font-style="italic">
            <xsl:apply-templates select="node()"/>
        </fo:inline>  
    </xsl:template>
    
</xsl:stylesheet>