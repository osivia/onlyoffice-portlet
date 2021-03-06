package org.osivia.services.onlyoffice.portlet.controller;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.onlyoffice.portlet.service.IOnlyofficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import javax.annotation.PostConstruct;
import javax.portlet.*;
import java.io.IOException;
import java.util.Map;

/**
 * OnlyofficeController
 *
 * @author dorian
 */
@Controller
@RequestMapping(value = "VIEW")
public class OnlyofficeController extends CMSPortlet implements PortletContextAware, PortletConfigAware {

    private static final String DEFAULT_VIEW = "view";

    private static final String CLOSE_VIEW = "close";

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
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        PortalWindow window = WindowFactory.getWindow(request);
        String action = window.getProperty("action");

        if(StringUtils.isNotEmpty(action) && "close".equals(action)) {
            return CLOSE_VIEW;
        }

        // LBI #1795 try to lock document if needed before edition
        boolean withLock = Boolean.parseBoolean(window.getProperty("osivia.onlyoffice.withLock"));
        if(withLock) {

            NuxeoController nuxeoController = new NuxeoController(request, response, getPortletContext());

            boolean askForLocking = onlyofficeService.askForLocking(nuxeoController);

            if(askForLocking) {
                onlyofficeService.lockTemporary(nuxeoController);
            }
        }

        return DEFAULT_VIEW;
    }


    @ActionMapping(params = "action=checkClosed")
    public void checkClosed(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        try {
            // Document identifier
            String documentId = window.getProperty("id");

            boolean waitForRefresh;
            int timeout = 10, i = 0;
            do {
                waitForRefresh = onlyofficeService.waitForRefresh(portalControllerContext, documentId);
                i++;
                Thread.sleep(1000);
            }
            // While the doc is not modified and i is below the timeout
            while (waitForRefresh && i < timeout);

            // Redirection URL
            String redirectionUrl = this.onlyofficeService.getCloseRedirectionUrl(portalControllerContext, documentId);
            if (StringUtils.isNotEmpty(redirectionUrl)) {
                response.sendRedirect(redirectionUrl);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @ModelAttribute(value = "apiUrl")
    public String getApiUrl(PortletRequest portletRequest, PortletResponse portletResponse) {
        return API_URL;
    }

    @ModelAttribute(value = "onlyOfficeConfig")
    public String getOnlyOfficeConfig(PortletRequest portletRequest, PortletResponse portletResponse) throws PortletException {

        PortalWindow window = WindowFactory.getWindow(portletRequest);
        String action = window.getProperty("action");
        String documentPath = window.getProperty(Constants.WINDOW_PROP_URI);

        if(StringUtils.isEmpty(action)) {
            return onlyofficeService.getOnlyOfficeConfig(portletRequest, portletResponse, getPortletContext(), documentPath);
        } else {
            return null;
        }
    }


    /**
     * Get toolbar properties model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return toolbar properties
     * @throws PortletException
     * @throws IOException
     */
    @ModelAttribute("toolbarProperties")
    public Map<String, String> getToolbarProperties(PortletRequest request, PortletResponse response) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.onlyofficeService.getToolbarProperties(portalControllerContext);
    }

}
