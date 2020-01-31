<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cl="http://www.ftn.uns.ac.rs/scientific-publication" version="2.0">

    <xsl:template match="/">

        <html>
            <head>

            </head>

            <body>
                <h1><xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:name"/></h1>
            </body>
        </html>

    </xsl:template>
</xsl:stylesheet>