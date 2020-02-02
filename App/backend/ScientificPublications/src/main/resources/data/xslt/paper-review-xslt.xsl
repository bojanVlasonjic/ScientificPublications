<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:cmn="http://www.ftn.uns.ac.rs/common"
    xmlns:rvw="http://www.ftn.uns.ac.rs/scientific-publication" version="2.0">
    
    <xsl:template match="/">
        
        <html>
            <head>
                <title>Document Review</title>
                <style type="text/css">
                    body {
                    font-family: sans-serif;
                    }
                    
                    .bold {
                    font-weight: bold;
                    }
                    
                    .italic {
                    font-style: italic;
                    }
                    
                    .underline {
                    text-decoration:underline;
                    }
                    
                    .align-right {
                    text-align: right;
                    }
                    
                    .align-center {
                    text-align: center;
                    }
                    
                    .align-justify {
                    text-align: justify:
                    }
                    
                    .center-content {
                    margin: auto;
                    width: 750px;
                    }
                </style>
            </head>
            
            <body>
                <div class="center-content">
                    <p class="bold">Recommendation:</p>
                    <p style="margin-bottom:30px;"><xsl:value-of select="rvw:paper-review/rvw:recommendation"></xsl:value-of></p>
                    <p class="bold">Questions:</p>
                    <div style="margin-bottom:30px;">
                        <xsl:for-each select="rvw:paper-review/rvw:questionare/rvw:question">
                            <div style="margin-bottom:15px;">
                                <p class="italic">
                                    <xsl:value-of select="rvw:question-text"></xsl:value-of>
                                </p>
                                <p>
                                    <xsl:value-of select="rvw:selected"></xsl:value-of>
                                </p>
                            </div>
                        </xsl:for-each>
                    </div>
                    <div style="margin-bottom:30px;">
                        <p class="bold">Comments to author:</p>
                        <xsl:for-each select="rvw:paper-review/rvw:comments-to-author/rvw:comment">
                            <p>
                                <xsl:apply-templates></xsl:apply-templates>
                            </p>
                        </xsl:for-each>
                    </div>
                    
                    <div>
                        <p class="bold">Comments to editor:</p>
                        <xsl:for-each select="rvw:paper-review/rvw:comments-to-editor/rvw:comment">
                            <p>
                                <xsl:apply-templates></xsl:apply-templates>
                            </p>
                        </xsl:for-each>
                    </div>
                    
                </div>
            </body>
        </html>
        
    </xsl:template>
    
    <xsl:template match="rvw:paper-review/rvw:comments-to-author/rvw:comment/rvw:italic">
        <span class="italic">
            <xsl:apply-templates select="node()"/>
        </span> 
    </xsl:template>
    
    
</xsl:stylesheet>

























