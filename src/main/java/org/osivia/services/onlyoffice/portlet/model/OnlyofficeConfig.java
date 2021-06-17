package org.osivia.services.onlyoffice.portlet.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OnlyofficeConfig")
public class OnlyofficeConfig {

    @XmlElement
    private OnlyOfficeDocument document;

    @XmlElement
    private String documentType;

    @XmlElement
    private EditorConfig editorConfig;

    @XmlElement
    private String width;

    @XmlElement
    private String height;

    @XmlElement
    private String type;
    
    @XmlElement
    private String token;


    /**
     * @return the document
     */
    public OnlyOfficeDocument getDocument() {
        return document;
    }


    /**
     * @param document the document to set
     */
    public void setDocument(OnlyOfficeDocument document) {
        this.document = document;
    }


    /**
     * @return the documentType
     */
    public String getDocumentType() {
        return documentType;
    }


    /**
     * @param documentType the documentType to set
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }


    /**
     * @return the editorConfig
     */
    public EditorConfig getEditorConfig() {
        return editorConfig;
    }


    /**
     * @param editorConfig the editorConfig to set
     */
    public void setEditorConfig(EditorConfig editorConfig) {
        this.editorConfig = editorConfig;
    }


    /**
     * @return the width
     */
    public String getWidth() {
        return width;
    }


    /**
     * @param width the width to set
     */
    public void setWidth(String width) {
        this.width = width;
    }


    /**
     * @return the height
     */
    public String getHeight() {
        return height;
    }


    /**
     * @param height the height to set
     */
    public void setHeight(String height) {
        this.height = height;
    }


    /**
     * Getter for type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }


    /**
     * Setter for type.
     * 
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}

    
}
