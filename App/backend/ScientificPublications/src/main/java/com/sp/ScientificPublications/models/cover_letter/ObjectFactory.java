//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.06 at 12:10:40 AM CET 
//


package com.sp.ScientificPublications.models.cover_letter;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the rs.ac.uns.ftn.scientific_publication package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: rs.ac.uns.ftn.scientific_publication
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CoverLetter }
     *
     */
    public CoverLetter createCoverLetter() {
        return new CoverLetter();
    }

    /**
     * Create an instance of {@link TContact }
     *
     */
    public TContact createTContact() {
        return new TContact();
    }

    /**
     * Create an instance of {@link TBody }
     *
     */
    public TBody createTBody() {
        return new TBody();
    }

    /**
     * Create an instance of {@link TClosure }
     *
     */
    public TClosure createTClosure() {
        return new TClosure();
    }

}
