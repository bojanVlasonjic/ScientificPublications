//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.06 at 12:10:40 AM CET 
//


package com.sp.ScientificPublications.models.cover_letter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TClosure complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TClosure"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="compliment" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="signature" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TClosure", propOrder = {
    "compliment",
    "signature"
})
public class TClosure {

    @XmlElement(required = true)
    protected String compliment;
    @XmlElement(required = true)
    protected String signature;

    /**
     * Gets the value of the compliment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompliment() {
        return compliment;
    }

    /**
     * Sets the value of the compliment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompliment(String value) {
        this.compliment = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignature(String value) {
        this.signature = value;
    }

}
