package org.osivia.services.onlyoffice.portlet.service;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osivia.portal.api.context.PortalControllerContext;

public interface IOnlyofficeService {

    public String getOnlyOfficeConfig(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext) throws PortletException;

	/**
	 * Call nuxeo cache for current editing users.
	 * If the user is the only one on the document, wait for 10 seconds until the document is released.
	 * In the other cases (no modification, or multiple users on the doc) don't wait. 
	 * 
	 * @param pcc
	 * @param id nuxeo docid
	 * @return
	 */
	public boolean waitForRefresh(PortalControllerContext pcc, String id);


}
