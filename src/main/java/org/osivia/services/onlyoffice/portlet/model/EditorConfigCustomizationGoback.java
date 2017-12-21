package org.osivia.services.onlyoffice.portlet.model;

import javax.xml.bind.annotation.XmlElement;

public class EditorConfigCustomizationGoback {

    @XmlElement
    private String text;

    @XmlElement
    private String url;


    /**
     * Getter for text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }


    /**
     * Setter for text.
     *
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }


    /**
     * Getter for url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }


    /**
     * Setter for url.
     *
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }


}
