<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:cmn="http://www.ftn.uns.ac.rs/common"
    xmlns:dr="http://www.ftn.uns.ac.rs/scientific-publication" version="2.0">
    
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
                    <h1 class="align-center" style="margin-bottom: 50px;">
                        <xsl:value-of select="dr:document-review/dr:title"></xsl:value-of>
                    </h1>
                    <p class="bold">Authors:</p>
                    <div style="margin-bottom: 10px;">
                        <xsl:for-each select="dr:document-review/dr:authors/cmn:author">
                            <p>
                                <xsl:value-of select="cmn:first-name"></xsl:value-of>&#160;<xsl:value-of select="cmn:last-name"></xsl:value-of>,
                                <xsl:value-of select="cmn:institution/cmn:location/cmn:city"></xsl:value-of>,
                                <xsl:value-of select="cmn:institution/cmn:location/cmn:country"></xsl:value-of>
                            </p>
                        </xsl:for-each>
                    </div>
                    <p class="bold">
                        Reviewer:
                    </p>
                    <p>
                        <xsl:value-of select="dr:document-review/dr:reviewer/dr:first-name"></xsl:value-of>&#160;<xsl:value-of select="dr:document-review/dr:reviewer/dr:last-name"></xsl:value-of>
                    </p>
                    
                    <p class="bold" style="font-size:18px;margin-bottom:15px;margin-top:20px;">Review:</p>
                    <xsl:for-each select="dr:document-review/dr:sections/cmn:section">
                        <p class="bold" style="margin-bottom:5px;"><xsl:value-of select="cmn:header"></xsl:value-of></p>
                        <xsl:for-each select="cmn:paragraphs/cmn:paragraph">
                            <p>
                                <xsl:apply-templates/>
                            </p>
                        </xsl:for-each>
                    </xsl:for-each>
                </div>
            </body>
        </html>
        
    </xsl:template>
    
    <xsl:template match="dr:document-review/dr:sections/cmn:section/cmn:paragraphs/cmn:paragraph/cmn:underline">
        <span class="underline">
            <xsl:apply-templates select="node()"/>
        </span>
    </xsl:template>
    
    <xsl:template match="dr:document-review/dr:sections/cmn:section/cmn:paragraphs/cmn:paragraph/cmn:bold">
        <span class="bold">
            <xsl:apply-templates select="node()"/>
        </span>
    </xsl:template>
    
    <xsl:template match="dr:document-review/dr:sections/cmn:section/cmn:paragraphs/cmn:paragraph/cmn:italic">
        <span class="italic">
            <xsl:apply-templates select="node()"/>
        </span> 
    </xsl:template>
    
    
</xsl:stylesheet>

























