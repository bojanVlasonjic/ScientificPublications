//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.01.28 at 12:39:44 PM CET 
//


package com.sp.ScientificPublications.models.document_review;

import com.sp.ScientificPublications.models.common.TAuthors;
import com.sp.ScientificPublications.models.common.TSections;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="authors" type="{http://www.ftn.uns.ac.rs/common}TAuthors"/>
 *         &lt;element name="reviewer" type="{http://www.ftn.uns.ac.rs/scientific-publication}Reviewer"/>
 *         &lt;element name="sections" type="{http://www.ftn.uns.ac.rs/common}TSections"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "title",
    "authors",
    "reviewer",
    "sections"
})
@XmlRootElement(name = "document-review")
public class DocumentReview {

    @XmlElement(required = true)
    protected String title;
    @XmlElement(required = true)
    protected TAuthors authors;
    @XmlElement(required = true)
    protected Reviewer reviewer;
    @XmlElement(required = true)
    protected TSections sections;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the authors property.
     * 
     * @return
     *     possible object is
     *     {@link TAuthors }
     *     
     */
    public TAuthors getAuthors() {
        return authors;
    }

    /**
     * Sets the value of the authors property.
     * 
     * @param value
     *     allowed object is
     *     {@link TAuthors }
     *     
     */
    public void setAuthors(TAuthors value) {
        this.authors = value;
    }

    /**
     * Gets the value of the reviewer property.
     * 
     * @return
     *     possible object is
     *     {@link Reviewer }
     *     
     */
    public Reviewer getReviewer() {
        return reviewer;
    }

    /**
     * Sets the value of the reviewer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reviewer }
     *     
     */
    public void setReviewer(Reviewer value) {
        this.reviewer = value;
    }

    /**
     * Gets the value of the sections property.
     * 
     * @return
     *     possible object is
     *     {@link TSections }
     *     
     */
    public TSections getSections() {
        return sections;
    }

    /**
     * Sets the value of the sections property.
     * 
     * @param value
     *     allowed object is
     *     {@link TSections }
     *     
     */
    public void setSections(TSections value) {
        this.sections = value;
    }

}