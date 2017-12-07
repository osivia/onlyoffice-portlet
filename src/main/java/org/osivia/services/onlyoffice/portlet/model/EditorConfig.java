package org.osivia.services.onlyoffice.portlet.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "editorConfig")
public class EditorConfig {

    @XmlElement
    private String callbackUrl;

    @XmlElement
    private String createUrl;

    @XmlElement
    private String lang;

    @XmlElement
    private String mode;

    @XmlElement
    private boolean chat;

    @XmlElement
    private OnlyOfficeUser user;

    /**
     * @return the callbackUrl
     */
    public String getCallbackUrl() {
        return callbackUrl;
    }


    /**
     * @param callbackUrl the callbackUrl to set
     */
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }


    /**
     * @return the createUrl
     */
    public String getCreateUrl() {
        return createUrl;
    }


    /**
     * @param createUrl the createUrl to set
     */
    public void setCreateUrl(String createUrl) {
        this.createUrl = createUrl;
    }


    /**
     * @return the lang
     */
    public String getLang() {
        return lang;
    }


    /**
     * @param lang the lang to set
     */
    public void setLang(String lang) {
        this.lang = lang;
    }


    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }


    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }


    /**
     * @return the user
     */
    public OnlyOfficeUser getUser() {
        return user;
    }


    /**
     * @param user the user to set
     */
    public void setUser(OnlyOfficeUser user) {
        this.user = user;
    }


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
