//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.06 at 12:12:15 AM CET 
//


package com.sp.ScientificPublications.models.scientific_paper;

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
     * Create an instance of {@link TMetas }
     *
     */
    public TMetas createTMetas() {
        return new TMetas();
    }

    /**
     * Create an instance of {@link TReferences }
     *
     */
    public TReferences createTReferences() {
        return new TReferences();
    }

    /**
     * Create an instance of {@link THeader }
     *
     */
    public THeader createTHeader() {
        return new THeader();
    }

    /**
     * Create an instance of {@link ScientificPaper }
     *
     */
    public ScientificPaper createScientificPaper() {
        return new ScientificPaper();
    }

    /**
     * Create an instance of {@link TAbstract }
     *
     */
    public TAbstract createTAbstract() {
        return new TAbstract();
    }

    /**
     * Create an instance of {@link TReference }
     *
     */
    public TReference createTReference() {
        return new TReference();
    }

    /**
     * Create an instance of {@link TMetas.Meta }
     *
     */
    public TMetas.Meta createTMetasMeta() {
        return new TMetas.Meta();
    }

    /**
     * Create an instance of {@link TReferences.Reference }
     * 
     */
    public TReferences.Reference createTReferencesReference() {
        return new TReferences.Reference();
    }

    /**
     * Create an instance of {@link THeader.Metas }
     * 
     */
    public THeader.Metas createTHeaderMetas() {
        return new THeader.Metas();
    }

}
