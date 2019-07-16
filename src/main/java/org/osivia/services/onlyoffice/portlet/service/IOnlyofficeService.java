package org.osivia.services.onlyoffice.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import java.io.IOException;
import java.util.Map;

public interface IOnlyofficeService {

    /**
     * @param portletRequest
     * @param portletResponse
     * @param portletContext
     * @param documentPath
     * @return
     * @throws PortletException
     */
    public String getOnlyOfficeConfig(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext, String documentPath)
            throws PortletException;


    /**
     * Get toolbar properties.
     *
     * @param portalControllerContext portal controller context
     * @return toolbar properties
     * @throws PortletException
     * @throws IOException
     */
    Map<String, String> getToolbarProperties(PortalControllerContext portalControllerContext) throws PortletException, IOException;


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
    public boolean askForLocking(NuxeoController nuxeoController) throws PortletException;

    /**
     * Lock document during edition
     *
     * @param pcc
     * @throws PortletException
     */
    public void lockTemporary(NuxeoController nuxeoController) throws PortletException;


    /**
     * Get close redirection URL.
     *
     * @param portalControllerContext portal controller context
     * @param documentId              document identifier
     * @return URL
     */
    String getCloseRedirectionUrl(PortalControllerContext portalControllerContext, String documentId) throws PortletException;

}
