package com.sp.ScientificPublications.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.SchemaFactory;

import static org.apache.xerces.jaxp.JAXPConstants.JAXP_SCHEMA_LANGUAGE;
import static org.apache.xerces.jaxp.JAXPConstants.W3C_XML_SCHEMA;

@Configuration
public class DomParserConfig {

    @Bean
    public DocumentBuilderFactory documentBuilderFactory() {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        dbFactory.setValidating(true);
        dbFactory.setNamespaceAware(true);
        dbFactory.setIgnoringComments(true);
        dbFactory.setIgnoringElementContentWhitespace(true);
        dbFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);

        return dbFactory;
    }

    @Bean
    public SchemaFactory xmlSchemaFactory() {
        return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    }
}
