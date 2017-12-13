package org.osivia.services.onlyoffice.portlet.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "editorConfig")
public class EditorConfigCustomization {

    @XmlElement
    private boolean chat;

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

}
