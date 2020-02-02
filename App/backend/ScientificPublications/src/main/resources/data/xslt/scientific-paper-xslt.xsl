<?xml version="1.0" encoding="ISO-8859-1"?>
<!DOCTYPE html>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:cmn="http://www.ftn.uns.ac.rs/common"
    xmlns:sp="http://www.ftn.uns.ac.rs/scientific-publication" version="2.0">
    
    <xsl:template match="/">
        
        <html>
            <head>
                <title>
                    <xsl:value-of select="sp:scientific-paper/sp:header/sp:title" />
                </title>
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
                    <h1 class="align-center"><xsl:value-of select="sp:scientific-paper/sp:header/sp:title"></xsl:value-of></h1>
                    <div style="margin-bottom:50px;">
                        <xsl:for-each select="sp:scientific-paper/sp:header/sp:authors/cmn:author">
                            <p class="align-center">
                                <xsl:value-of select="cmn:first-name"></xsl:value-of>&#160;<xsl:value-of select="cmn:last-name"></xsl:value-of>
                            </p>
                            <p class="align-center" style="font-style:italic;font-size: 10px;margin-bottom:2px;">
                                <xsl:value-of select="cmn:institution/sp:name"></xsl:value-of>,
                                <xsl:value-of select="cmn:institution/sp:location/sp:city"></xsl:value-of>
                                <xsl:value-of select="cmn:institution/sp:location/sp:state"></xsl:value-of>
                                <xsl:value-of select="cmn:institution/sp:location/sp:country"></xsl:value-of>
                            </p>
                        </xsl:for-each>
                    </div>
                    <div style="margin-bottom:30px;">
                        <p class="align-justify" style="margin-bottom:2px;">
                            <span class="bold">Purpose</span> - <xsl:value-of select="sp:scientific-paper/sp:abstract/sp:purpose"></xsl:value-of>
                        </p>
                        <p class="align-justify" style="margin-bottom:2px;">
                            <span class="bold">Keywords</span> - <xsl:value-of select="sp:scientific-paper/sp:abstract/sp:keywords"></xsl:value-of>
                        </p>
                        <p class="align-justify" style="margin-bottom:2px;">
                            <span class="bold">Paper type</span> - <xsl:value-of select="sp:scientific-paper/sp:abstract/sp:paper-type"></xsl:value-of>
                        </p>
                    </div>
                    <xsl:for-each select="sp:scientific-paper/sp:sections/cmn:section">
                        <p class="bold" style="font-size:18px;margin-bottom:10px;margin-top:20px;">
                            <xsl:value-of select="cmn:header"></xsl:value-of>
                        </p>
                        <xsl:for-each select="cmn:paragraphs/cmn:paragraph">
                            <p class="align-justify" style="text-index:20px;font-size:16px;margin-bottom:5px">
                                <xsl:apply-templates />
                            </p>
                        </xsl:for-each>
                    </xsl:for-each>
                    <div>
                        <p class="bold" style="font-size:14px;margin-bottom:5px;margin-top:30px;">References</p>
                        <xsl:for-each select="sp:scientific-paper/sp:references/sp:reference">
                            <p style="margin-bottom:5px;font-size:12px;">
                                <xsl:for-each select="sp:authors/cmn:author">
                                    <xsl:value-of select="cmn:first-name"></xsl:value-of>&#160;<xsl:value-of select="cmn:last-name"></xsl:value-of>
                                </xsl:for-each>
                                <xsl:value-of select="sp:year"></xsl:value-of>&#160;
                                <xsl:value-of select="sp:paper-name"></xsl:value-of>&#160;
                                <xsl:value-of select="sp:journal"></xsl:value-of>
                            </p>
                        </xsl:for-each>
                    </div>
                </div>
            </body>
        </html>
        
    </xsl:template>
    
    <xsl:template match="sp:scientific-paper/sp:sections/cmn:section/cmn:paragraphs/cmn:paragraph/cmn:bold">
        <span class="bold">
            <xsl:apply-templates select="node()"/>
        </span>  
    </xsl:template>
    
    <xsl:template match="sp:scientific-paper/sp:sections/cmn:section/cmn:paragraphs/cmn:paragraph/cmn:italic">
        <span class="italic">
            <xsl:apply-templates select="node()"/>
        </span> 
    </xsl:template>
    
    
</xsl:stylesheet>