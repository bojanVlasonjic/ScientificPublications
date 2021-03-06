//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.06 at 12:16:00 AM CET 
//


package com.sp.ScientificPublications.models.paper_review;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header" type="{http://www.ftn.uns.ac.rs/scientific-publication}THeader"/&gt;
 *         &lt;element name="recommendation" type="{http://www.ftn.uns.ac.rs/scientific-publication}TRecommendation"/&gt;
 *         &lt;element name="questionare" type="{http://www.ftn.uns.ac.rs/scientific-publication}TQuestionare"/&gt;
 *         &lt;element name="comments-to-author" type="{http://www.ftn.uns.ac.rs/scientific-publication}TComments"/&gt;
 *         &lt;element name="comments-to-editor" type="{http://www.ftn.uns.ac.rs/scientific-publication}TComments"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "recommendation",
    "questionare",
    "commentsToAuthor",
    "commentsToEditor"
})
@XmlRootElement(name = "paper-review")
public class PaperReview {

    @XmlElement(required = true)
    protected THeader header;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TRecommendation recommendation;
    @XmlElement(required = true)
    protected TQuestionare questionare;
    @XmlElement(name = "comments-to-author", required = true)
    protected TComments commentsToAuthor;
    @XmlElement(name = "comments-to-editor", required = true)
    protected TComments commentsToEditor;

    /**
     * Gets the value of the header property.
     *
     * @return
     *     possible object is
     *     {@link THeader }
     *
     */
    public THeader getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     *
     * @param value
     *     allowed object is
     *     {@link THeader }
     *
     */
    public void setHeader(THeader value) {
        this.header = value;
    }

    /**
     * Gets the value of the recommendation property.
     *
     * @return
     *     possible object is
     *     {@link TRecommendation }
     *
     */
    public TRecommendation getRecommendation() {
        return recommendation;
    }

    /**
     * Sets the value of the recommendation property.
     *
     * @param value
     *     allowed object is
     *     {@link TRecommendation }
     *     
     */
    public void setRecommendation(TRecommendation value) {
        this.recommendation = value;
    }

    /**
     * Gets the value of the questionare property.
     * 
     * @return
     *     possible object is
     *     {@link TQuestionare }
     *     
     */
    public TQuestionare getQuestionare() {
        return questionare;
    }

    /**
     * Sets the value of the questionare property.
     * 
     * @param value
     *     allowed object is
     *     {@link TQuestionare }
     *     
     */
    public void setQuestionare(TQuestionare value) {
        this.questionare = value;
    }

    /**
     * Gets the value of the commentsToAuthor property.
     * 
     * @return
     *     possible object is
     *     {@link TComments }
     *     
     */
    public TComments getCommentsToAuthor() {
        return commentsToAuthor;
    }

    /**
     * Sets the value of the commentsToAuthor property.
     * 
     * @param value
     *     allowed object is
     *     {@link TComments }
     *     
     */
    public void setCommentsToAuthor(TComments value) {
        this.commentsToAuthor = value;
    }

    /**
     * Gets the value of the commentsToEditor property.
     * 
     * @return
     *     possible object is
     *     {@link TComments }
     *     
     */
    public TComments getCommentsToEditor() {
        return commentsToEditor;
    }

    /**
     * Sets the value of the commentsToEditor property.
     * 
     * @param value
     *     allowed object is
     *     {@link TComments }
     *     
     */
    public void setCommentsToEditor(TComments value) {
        this.commentsToEditor = value;
    }

}
