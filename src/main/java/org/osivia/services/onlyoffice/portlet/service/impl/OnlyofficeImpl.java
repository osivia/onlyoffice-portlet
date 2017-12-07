package org.osivia.services.onlyoffice.portlet.service.impl;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cms.impl.BasicPermissions;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.onlyoffice.portlet.command.GetUserJWTTokenCommand;
import org.osivia.services.onlyoffice.portlet.model.EditorConfig;
import org.osivia.services.onlyoffice.portlet.model.OnlyOfficeDocument;
import org.osivia.services.onlyoffice.portlet.model.OnlyOfficeUser;
import org.osivia.services.onlyoffice.portlet.model.OnlyofficeConfig;
import org.osivia.services.onlyoffice.portlet.service.IOnlyofficeService;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import net.sf.json.JSONObject;

@Service
public class OnlyofficeImpl implements IOnlyofficeService {

    private static final String WEBSERVICE_CALLBACKEDIT_PATH = "/nuxeo/site/onlyoffice/callbackEdit/";

    private String getWindowProperty(PortletRequest request, String property) {
        PortalWindow window = WindowFactory.getWindow(request);
        return window.getProperty(property);
    }

    private PropertyMap getCurrentFileContent(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext)
            throws PortletException {
        PropertyMap properties = getCurrentProperties(portletRequest, portletResponse, portletContext);
        PropertyMap fileContent = properties.getMap("file:content");
        return fileContent;
    }

    private PropertyMap getCurrentProperties(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext)
            throws PortletException {
        Document currentDoc = getCurrentDoc(portletRequest, portletResponse, portletContext);
        PropertyMap properties = currentDoc.getProperties();
        return properties;
    }

    private Document getCurrentDoc(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext) throws PortletException {
        NuxeoController nuxeoController = new NuxeoController(portletRequest, portletResponse, portletContext);

        Document currentDoc = nuxeoController.getCurrentDoc();
        if (currentDoc == null) {
            String path = getWindowProperty(portletRequest, Constants.WINDOW_PROP_URI);
            NuxeoDocumentContext documentContext;
            try {
                documentContext = NuxeoController.getDocumentContext(portletRequest, portletResponse, portletContext, path);
            } catch (PortletException e) {
                throw new PortletException(e);
            }
            Document document = documentContext.getDoc();
            nuxeoController.setCurrentDoc(document);
            currentDoc = nuxeoController.getCurrentDoc();
        }
        return currentDoc;
    }

    private String getMode(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext) throws PortletException {

        NuxeoDocumentContext documentContext = NuxeoController.getDocumentContext(portletRequest, portletResponse, portletContext);

        BasicPermissions permissions = documentContext.getPermissions(BasicPermissions.class);
        if (permissions.isEditableByUser()) {
            return "edit";
        } else {
            return "view";
        }
    }

    private String getDocUrl(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext) throws PortletException {

        PropertyMap fileContent = getCurrentFileContent(portletRequest, portletResponse, portletContext);

        String docUrl = fileContent.getString("data");

        NuxeoController nuxeoController = new NuxeoController(portletRequest, portletResponse, portletContext);

        String userToken = (String) nuxeoController.executeNuxeoCommand(new GetUserJWTTokenCommand("onlyoffice"));

        docUrl = docUrl + "?token=" + userToken;

        return docUrl;
    }

    private String getDocFileType(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext) throws PortletException {
        String filename = getDocTitle(portletRequest, portletResponse, portletContext);
        String docExtension = StringUtils.substringAfterLast(filename, ".");
        return docExtension;
    }

    private String getDocKey(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext) throws PortletException {
        Document currentDoc = getCurrentDoc(portletRequest, portletResponse, portletContext);
        String docKey = currentDoc.getId() + "." + currentDoc.getLastModified().getTime();
        return docKey;
    }

    private String getDocTitle(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext) throws PortletException {
        PropertyMap fileContent = getCurrentFileContent(portletRequest, portletResponse, portletContext);
        String filename = fileContent.getString("name");
        return filename;
    }

    private String getCallbackUrl(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext) throws PortletException {

        Document currentDoc = getCurrentDoc(portletRequest, portletResponse, portletContext);
        String currentDocId = currentDoc.getId();

        String onlyofficePrivateUrl = "http://vm-dli-foad";

        return onlyofficePrivateUrl + WEBSERVICE_CALLBACKEDIT_PATH + currentDocId;
    }

    @Override
    public String getOnlyOfficeConfig(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext) throws PortletException {

        OnlyofficeConfig onlyOfficeConfig = new OnlyofficeConfig();
        OnlyOfficeDocument onlyOfficeDocument = new OnlyOfficeDocument();
        onlyOfficeDocument.setFileType(getDocFileType(portletRequest, portletResponse, portletContext));
        onlyOfficeDocument.setKey(getDocKey(portletRequest, portletResponse, portletContext));
        onlyOfficeDocument.setTitle(getDocTitle(portletRequest, portletResponse, portletContext));
        onlyOfficeDocument.setUrl(getDocUrl(portletRequest, portletResponse, portletContext));
        onlyOfficeConfig.setDocument(onlyOfficeDocument);

        onlyOfficeConfig.setDocumentType("text");

        EditorConfig onlyOfficeEditorConfig = new EditorConfig();
        onlyOfficeEditorConfig.setCallbackUrl(getCallbackUrl(portletRequest, portletResponse, portletContext));
        OnlyOfficeUser user = new OnlyOfficeUser();
        user.setId(portletRequest.getUserPrincipal().getName());
        onlyOfficeEditorConfig.setUser(user);
        onlyOfficeEditorConfig.setMode(getMode(portletRequest, portletResponse, portletContext));
        onlyOfficeEditorConfig.setChat(false);
        onlyOfficeConfig.setEditorConfig(onlyOfficeEditorConfig);

        onlyOfficeConfig.setWidth("100%");
        onlyOfficeConfig.setHeight("800");

        JSONObject onlyOfficeConfigJson = JSONObject.fromObject(onlyOfficeConfig);

        return onlyOfficeConfigJson.toString();
    }
}
