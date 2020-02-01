<?xml version="1.0" encoding="ISO-8859-1"?>
<!DOCTYPE html>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:cmn="http://www.ftn.uns.ac.rs/common"
    xmlns:sp="http://www.ftn.uns.ac.rs/scientific-publication" version="2.0">
    
    <xsl:template match="/">
        
        <html>
            <head>
                <title>Cover Letter</title>
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
                    
                    
                </div>
            </body>
        </html>
        
    </xsl:template>
</xsl:stylesheet>