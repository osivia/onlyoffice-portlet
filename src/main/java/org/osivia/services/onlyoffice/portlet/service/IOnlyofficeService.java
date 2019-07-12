package org.osivia.services.onlyoffice.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

public interface IOnlyofficeService {

    /**
     * @param portletRequest
     * @param portletResponse
     * @param portletContext
     * @param documentPath
     * @return
     * @throws PortletException
     */
    String getOnlyOfficeConfig(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext, String documentPath)
            throws PortletException;


    /**
     * Call nuxeo cache for current editing users.
     * If the user is the only one on the document, wait for 10 seconds until the document is released.
     * In the other cases (no modification, or multiple users on the doc) don't wait.
     *
     * @param pcc
     * @param id  nuxeo docid
     * @return
     */
    boolean waitForRefresh(PortalControllerContext pcc, String id);


    /**
     * Test if current document can be locked.
     * The condition is to be alone on the document and if the document is not currently locked.
     * *
     *
     * @param nuxeoController Nuxeo controller
     * @return
     */
    boolean askForLocking(NuxeoController nuxeoController) throws PortletException;


    /**
     * Lock document during edition
     *
     * @param nuxeoController Nuxeo controller
     */
    void lockTemporary(NuxeoController nuxeoController) throws PortletException;

}
