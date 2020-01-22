package com.sp.ScientificPublications.config;

import org.apache.fop.apps.FopFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xml.sax.SAXException;


import javax.xml.transform.TransformerFactory;
import java.io.File;
import java.io.IOException;

@Configuration
public class PdfTransformerConfig {

    private static final String fopConfigPath = "src/main/resources/data/fop.xconf";

    @Bean
    public FopFactory fopFactory() throws SAXException, IOException {
        return FopFactory.newInstance(new File(fopConfigPath));
    }

    @Bean
    public TransformerFactory transformerFactory() {
        return TransformerFactory.newInstance();
    }

}
