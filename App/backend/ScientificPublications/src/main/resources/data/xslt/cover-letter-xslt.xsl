<?xml version="1.0" encoding="ISO-8859-1"?>
<!DOCTYPE html>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cmn="http://www.ftn.uns.ac.rs/common"
                xmlns:cl="http://www.ftn.uns.ac.rs/scientific-publication" version="2.0">

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
                    <table style="width:100%;margin-bottom:30px">
                        <tr style="margin-bottom:5px;">
                            <td class="bold">Sender</td>
                            <td class="bold align-right">Receiver</td>
                        </tr>
                        <tr>
                            <td><xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:name"/></td>
                            <td class="align-right"><xsl:value-of select="cl:cover-letter/cl:employer-contact/cl:name"/></td>
                        </tr>
                        <tr>
                            <td><xsl:value-of select="cl:cover-letter/cl:sender-contact/cmn:location/cmn:city"/>,
                                <xsl:value-of select="cl:cover-letter/cl:sender-contact/cmn:location/cmn:state"/>,
                                <xsl:value-of select="cl:cover-letter/cl:sender-contact/cmn:location/cmn:country"/></td>
                            <td class="align-right"><xsl:value-of select="cl:cover-letter/cl:employer-contact/cmn:location/cmn:city"/>,
                                <xsl:value-of select="cl:cover-letter/cl:employer-contact/cmn:location/cmn:country"/></td>
                        </tr>
                        <tr>
                            <td><xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:phone-number"/></td>
                            <td class="align-right"><xsl:value-of select="cl:cover-letter/cl:employer-contact/cl:phone-number"/></td>
                        </tr>
                        <tr>
                            <td><xsl:value-of select="cl:cover-letter/cl:sender-contact/cl:email"/></td>
                            <td class="align-right"><xsl:value-of select="cl:cover-letter/cl:employer-contact/cl:email"/></td>
                        </tr>
                    </table>
                    <p style="margin-bottom: 35px;"><xsl:value-of select="cl:cover-letter/cl:body/cl:salutation" /></p>
                    <xsl:for-each select="cl:cover-letter/cl:body/cl:paragraphs/cmn:paragraph">
                        <p style="margin-bottom: 10px;" class="align-justify">
                            <xsl:value-of select="."></xsl:value-of>
                        </p>
                    </xsl:for-each>
                    
                    <div style="margin-bottom:30px; margin-top: 30px;" margin-bottom="30px" margin-top="30px">
                        <p style="margin-bottom: 5px;">
                            <xsl:value-of select="cl:cover-letter/cl:body/cl:closure/cl:compliment"></xsl:value-of>
                        </p>
                        <p style="margin-bottom: 5px;">
                            <xsl:value-of select="cl:cover-letter/cl:body/cl:closure/cl:signature"></xsl:value-of>
                        </p>
                    </div>
                </div>
            </body>
        </html>

    </xsl:template>
</xsl:stylesheet>