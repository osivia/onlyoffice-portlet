package org.osivia.services.onlyoffice.portlet.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
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
    public String view(RenderRequest request, RenderResponse response) {
    	
    	PortalWindow window = WindowFactory.getWindow(request);
		String action = window.getProperty("action");
		
		if(StringUtils.isNotEmpty(action) && "close".equals(action)) {
			
			return CLOSE_VIEW;
		}
    	
        return DEFAULT_VIEW;
    }

    
    @ActionMapping(params = "action=checkClosed")
    public void checkClosed(ActionRequest request, ActionResponse response) {
    	
    	PortalWindow window = WindowFactory.getWindow(request);
		String backUrl = window.getProperty("backURL");

		PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
		
		try {
			String id = window.getProperty("id");
			boolean waitForRefresh = false;
			int timeout = 10, i=0;
			
			do {
				waitForRefresh = onlyofficeService.waitForRefresh(pcc, id);
				i++;
				Thread.sleep(1000);
				
			}
			// While the doc is not modified and i is below the timeout
			while(waitForRefresh && i < timeout);
			
			
			response.sendRedirect(backUrl);
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
		
		if(StringUtils.isEmpty(action)) {
    	
			return onlyofficeService.getOnlyOfficeConfig(portletRequest, portletResponse, getPortletContext());
		}
		else return null;
    }

}
