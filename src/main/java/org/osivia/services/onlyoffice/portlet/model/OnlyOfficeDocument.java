package org.osivia.services.onlyoffice.portlet.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "document")
public class OnlyOfficeDocument {

    @XmlElement
    private String fileType;

    @XmlElement
    private String key;

    @XmlElement
    private String title;

    @XmlElement
    private String url;


    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }


    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }


    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }


    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }


    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }


    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
