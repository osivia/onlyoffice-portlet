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

	/**
	 * Test if current document can be locked.
	 * The condition is to be alone on the document and if the document is not currently locked.
	 * 	 * 
	 * @param portal controller context
	 * @return
	 * @throws PortletException 
	 */
	public boolean askForLocking(PortalControllerContext pcc) throws PortletException;

	/**
	 * Lock document during edition
	 * 
	 * @param pcc
	 * @throws PortletException 
	 */
	public void lockTemporary(PortalControllerContext pcc) throws PortletException;


}
