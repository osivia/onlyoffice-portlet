package org.osivia.services.onlyoffice.portlet.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "user")
public class OnlyOfficeUser {

    @XmlElement
    private String id;

    @XmlElement
    private String name;


    /**
     * @return the id
     */
    public String getId() {
        return id;
    }


    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
