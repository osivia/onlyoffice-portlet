package org.osivia.services.onlyoffice.portlet.model;

import javax.xml.bind.annotation.XmlElement;

/**
 * Changes the image file at the top left corner of the Editor header. The recommended image height is 20 pixels.
 */
public class EditorConfigCustomizationLogo {

    /**
     * Path to the image file used to show in common work mode (i.e. in view and edit modes for all editors). The image must have the following size: 172x40.
     * Example: "https://example.com/logo.png"
     */
    @XmlElement
    private String image;

    /**
     * Path to the image file used to show in the embedded mode (see the config section to find out how to define the embedded document type). The image must have the following size: 248x40.
     * Example: "https://example.com/logo_em.png"
     */
    @XmlElement
    private String imageEmbedded;

    /**
     * The absolute URL which will be used when someone clicks the logo image (can be used to go to your web site, etc.). Leave as an empty string or null to make the logo not clickable.
     * Example: "https://example.com"
     */
    @XmlElement
    private String url;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageEmbedded() {
        return imageEmbedded;
    }

    public void setImageEmbedded(String imageEmbedded) {
        this.imageEmbedded = imageEmbedded;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
