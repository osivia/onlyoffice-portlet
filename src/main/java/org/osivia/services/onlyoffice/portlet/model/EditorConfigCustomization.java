package org.osivia.services.onlyoffice.portlet.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "editorConfig")
public class EditorConfigCustomization {

    @XmlElement
    private boolean chat;

    @XmlElement
    private boolean compactToolbar;

    @XmlElement
    private EditorConfigCustomizationGoback goback;


    /**
     * @return the chat
     */
    public boolean isChat() {
        return chat;
    }

    /**
     * @param chat the chat to set
     */
    public void setChat(boolean chat) {
        this.chat = chat;
    }

    /**
     * Getter for compactToolbar.
     * 
     * @return the compactToolbar
     */
    public boolean isCompactToolbar() {
        return compactToolbar;
    }

    /**
     * Setter for compactToolbar.
     * 
     * @param compactToolbar the compactToolbar to set
     */
    public void setCompactToolbar(boolean compactToolbar) {
        this.compactToolbar = compactToolbar;
    }

    /**
     * Getter for goback.
     *
     * @return the goback
     */
    public EditorConfigCustomizationGoback getGoback() {
        return goback;
    }

    /**
     * Setter for goback.
     *
     * @param goback the goback to set
     */
    public void setGoback(EditorConfigCustomizationGoback goback) {
        this.goback = goback;
    }

}
