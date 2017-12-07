package org.osivia.services.onlyoffice.portlet.controller;

import javax.annotation.PostConstruct;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.onlyoffice.portlet.service.IOnlyofficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * OnlyofficeController
 *
 * @author dorian
 */
@Controller
@RequestMapping(value = "VIEW")
public class OnlyofficeController extends CMSPortlet implements PortletContextAware, PortletConfigAware {

    private static final String DEFAULT_VIEW = "view";

    private static final String ONLYOFFICE_DOMAIN = System.getProperty("osivia.onlyoffice.url", StringUtils.EMPTY);

    private static final String API_URL = ONLYOFFICE_DOMAIN + "/web-apps/apps/api/documents/api.js";

    /** Portlet context. */
    private PortletContext portletContext;
    /** Portlet config. */
    private PortletConfig portletConfig;

    /** Calendar service. */
    @Autowired
    private IOnlyofficeService onlyofficeService;

    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

    /**
     * Default constructor.
     */
    public OnlyofficeController() {
        super();
    }

    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(portletConfig);
    }

    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) {
        return DEFAULT_VIEW;
    }

    @ModelAttribute(value = "apiUrl")
    public String getApiUrl(PortletRequest portletRequest, PortletResponse portletResponse) {
        return API_URL;
    }

    @ModelAttribute(value = "onlyOfficeConfig")
    public String getOnlyOfficeConfig(PortletRequest portletRequest, PortletResponse portletResponse) throws PortletException {
        return onlyofficeService.getOnlyOfficeConfig(portletRequest, portletResponse, getPortletContext());
    }

}
