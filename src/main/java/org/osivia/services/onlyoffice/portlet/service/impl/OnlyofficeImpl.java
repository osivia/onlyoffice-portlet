package org.osivia.services.onlyoffice.portlet.service.impl;

import java.security.Principal;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.onlyoffice.portlet.command.GetUserJWTTokenCommand;
import org.osivia.services.onlyoffice.portlet.model.EditorConfig;
import org.osivia.services.onlyoffice.portlet.model.EditorConfigCustomization;
import org.osivia.services.onlyoffice.portlet.model.EditorConfigCustomizationGoback;
import org.osivia.services.onlyoffice.portlet.model.OnlyOfficeDocument;
import org.osivia.services.onlyoffice.portlet.model.OnlyOfficeUser;
import org.osivia.services.onlyoffice.portlet.model.OnlyofficeConfig;
import org.osivia.services.onlyoffice.portlet.service.IOnlyofficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.LockStatus;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPermissions;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;
import fr.toutatice.portail.cms.nuxeo.api.liveedit.OnlyofficeLiveEditHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class OnlyofficeImpl implements IOnlyofficeService {


    private static final String ANONYMOUS_NAME = "Utilisateur invité";

    private static final String ANONYMOUS_LOGIN = "Anonymous";

    private static final String WEBSERVICE_CALLBACKEDIT_PATH = "/site/onlyoffice/callbackEdit/";

    private static final String ONLYOFFICE_TOKEN_ID = System.getProperty("osivia.onlyoffice.token.id", "onlyoffice");

    private static final String ONLYOFFICE_NUXEO_URL = System.getProperty("osivia.onlyoffice.nuxeo.url", StringUtils.EMPTY);

    private static final String ONLYOFFICE_LANG = System.getProperty("osivia.onlyoffice.lang", "fr-FR");

    private final PersonService personService;

    /** Internationalization bundle factory. */
    private final IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;



    public OnlyofficeImpl() {
        personService = DirServiceFactory.getService(PersonService.class);

        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());

    }

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
            documentContext = nuxeoController.getDocumentContext( path);
             
            currentDoc = documentContext.getDocument();
            nuxeoController.setCurrentDoc(currentDoc);

        }
        return currentDoc;
    }

    private String getMode(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext, String documentPath)
            throws PortletException {

        PortalControllerContext portalCtx = new PortalControllerContext(portletContext, portletRequest, portletResponse);
        NuxeoController ctl = new NuxeoController(portalCtx );
        NuxeoDocumentContext documentContext = ctl.getDocumentContext(documentPath);

        NuxeoPermissions permissions = documentContext.getPermissions();
        if (permissions.isEditable()) {
            return "edit";
        } else {
            return "view";
        }
    }

    private String getDocUrl(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext) throws PortletException {

        PropertyMap fileContent = getCurrentFileContent(portletRequest, portletResponse, portletContext);
        String docUrl = fileContent.getString("data");
        NuxeoController nuxeoController = new NuxeoController(portletRequest, portletResponse, portletContext);
        String userToken = (String) nuxeoController.executeNuxeoCommand(new GetUserJWTTokenCommand(ONLYOFFICE_TOKEN_ID));

        if (StringUtils.contains(docUrl, "/nxbigfile/")) {
            int indexOf = StringUtils.indexOf(docUrl, "/nxbigfile/");
            docUrl = ONLYOFFICE_NUXEO_URL + StringUtils.substring(docUrl, indexOf) + "?token=" + userToken;
        } else if (StringUtils.contains(docUrl, "/nxfile/")) {
            int indexOf = StringUtils.indexOf(docUrl, "/nxfile/");
            docUrl = ONLYOFFICE_NUXEO_URL + StringUtils.substring(docUrl, indexOf) + "?token=" + userToken;
        } else {
            docUrl = StringUtils.EMPTY;
        }
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
        return ONLYOFFICE_NUXEO_URL + WEBSERVICE_CALLBACKEDIT_PATH + currentDocId;
    }

    private String getDocumentType(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext) throws PortletException {
        PropertyMap fileContent = getCurrentFileContent(portletRequest, portletResponse, portletContext);
        String mimeType = fileContent.getString("mime-type");
        return OnlyofficeLiveEditHelper.getFileType(mimeType).name();
    }

    private EditorConfigCustomizationGoback getGoback(Bundle bundle, PortletRequest portletRequest, PortletResponse portletResponse,
            PortletContext portletContext) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, portletRequest, portletResponse);

        EditorConfigCustomizationGoback goback = new EditorConfigCustomizationGoback();
        goback.setText(bundle.getString("BACK_TEXT"));

        String toCloseUrl = this.getCloseUrl(portalControllerContext);
        goback.setUrl(toCloseUrl);

        return goback;
    }


    @Override
    public String getOnlyOfficeConfig(PortletRequest portletRequest, PortletResponse portletResponse, PortletContext portletContext, String documentPath)
            throws PortletException {

        OnlyofficeConfig onlyOfficeConfig = new OnlyofficeConfig();
        OnlyOfficeDocument onlyOfficeDocument = new OnlyOfficeDocument();
        onlyOfficeDocument.setFileType(getDocFileType(portletRequest, portletResponse, portletContext));
        onlyOfficeDocument.setKey(getDocKey(portletRequest, portletResponse, portletContext));

        // LBI #1913 - Escape quotes in title and url
        String escapeTitle = getDocTitle(portletRequest, portletResponse, portletContext);
        onlyOfficeDocument.setTitle(escapeTitle.replaceAll("'", " "));

        String escapeUrl = getDocUrl(portletRequest, portletResponse, portletContext);
        onlyOfficeDocument.setUrl(escapeUrl.replaceAll("'", ""));

        onlyOfficeConfig.setDocument(onlyOfficeDocument);
        onlyOfficeConfig.setDocumentType(getDocumentType(portletRequest, portletResponse, portletContext));

        EditorConfig onlyOfficeEditorConfig = new EditorConfig();
        onlyOfficeEditorConfig.setCallbackUrl(getCallbackUrl(portletRequest, portletResponse, portletContext));
        onlyOfficeEditorConfig.setUser(buildOnlyOfficeUser(portletRequest.getUserPrincipal()));
        onlyOfficeEditorConfig.setMode(getMode(portletRequest, portletResponse, portletContext, documentPath));
        onlyOfficeEditorConfig.setLang(getLang(portletRequest.getLocale()));
        EditorConfigCustomization editorConfigCustomization = new EditorConfigCustomization();
        editorConfigCustomization.setChat(true);
        editorConfigCustomization.setCompactToolbar(false);
        Bundle bundle = bundleFactory.getBundle(portletRequest.getLocale());
        editorConfigCustomization.setGoback(getGoback(bundle, portletRequest, portletResponse, portletContext));
        onlyOfficeEditorConfig.setCustomization(editorConfigCustomization);
        onlyOfficeConfig.setEditorConfig(onlyOfficeEditorConfig);

        JSONObject onlyOfficeConfigJson = JSONObject.fromObject(onlyOfficeConfig);

        return onlyOfficeConfigJson.toString();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getToolbarProperties(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Current window
        PortalWindow window = WindowFactory.getWindow(request);
        // Action
        String action = window.getProperty("action");

        // Toolbar properties
        Map<String, String> properties;
        if (StringUtils.isEmpty(action)) {
            properties = new HashMap<>();

            // Current document title
            Document document = this.getCurrentDoc(request, portalControllerContext.getResponse(), portalControllerContext.getPortletCtx());
            properties.put("documentTitle", document.getTitle());

            // Close URL
            String closeUrl = this.getCloseUrl(portalControllerContext);
            properties.put("closeUrl", closeUrl);
        } else {
            properties = null;
        }

        return properties;
    }


    /**
     * Get close URL.
     * 
     * @param portalControllerContext portal controller context
     * @return close URL
     * @throws PortletException
     */
    private String getCloseUrl(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Portal URL factory
        IPortalUrlFactory portalUrlFactory = nuxeoController.getPortalUrlFactory();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Current Nuxeo document
        Document document = this.getCurrentDoc(request, portalControllerContext.getResponse(), portalControllerContext.getPortletCtx());

        // Window properties
        Map<String, String> properties = new HashMap<>();

        // Back URL
        String backUrl = portalUrlFactory.getBackURL(portalControllerContext, false);
        if (StringUtils.isNotEmpty(backUrl)) {
            backUrl = backUrl.replace("/pagemarker", "/refresh/pagemarker");
        } else {
            // displaycontext = "menu" afin de réinitialiser le backpagemarker
            backUrl = nuxeoController.getLink(document, "menu").getUrl();
        }
        properties.put("backURL", backUrl);
        properties.put("osivia.title", bundle.getString("CLOSING"));
        properties.put("id", document.getId());
        properties.put("action", "close");

        // URL
        String url;
        try {
            url = portalUrlFactory.getStartPortletUrl(portalControllerContext, "osivia-services-onlyoffice-portletInstance", properties);
        } catch (PortalException e) {
            url = StringUtils.EMPTY;
        }

        return url;
    }


    private String getLang(Locale locale) {
        return ONLYOFFICE_LANG;
    }

    private OnlyOfficeUser buildOnlyOfficeUser(Principal userPrincipal) {

        OnlyOfficeUser user = new OnlyOfficeUser();
        String id = ANONYMOUS_LOGIN;
        // LBI #1732 gestion user anonyme
        if(userPrincipal != null) {
            id = userPrincipal.getName();

            Person person = personService.getPerson(id);
            if (person != null) {
                // LBI #1734 user avec chars spéciaux
                String displayName = person.getDisplayName().replace("'", " ");
                user.setName(displayName);
            } else {
                user.setName(id);
            }
        }
        else {
            user.setName(ANONYMOUS_NAME);
        }

        user.setId(id);


        return user;
    }

    /* (non-Javadoc)
     * @see org.osivia.services.onlyoffice.portlet.service.IOnlyofficeService#checkClosed(java.lang.String, long)
     */
    @Override
    public boolean waitForRefresh(PortalControllerContext pcc, String uuid) {

        boolean editedByMe = false;
        boolean editedByOthers = false;

        Principal principal = pcc.getRequest().getUserPrincipal();

        NuxeoController controller = new NuxeoController(pcc);
        INuxeoCommand command = new IsDocumentCurrentlyEditedCommand(uuid);
        JSONObject info = (JSONObject) controller.executeNuxeoCommand(command);

        if(info!= null && info.containsKey("isCurrentlyEdited")) {

            if(info.getBoolean("isCurrentlyEdited")) {
                JSONObject currentlyEditedEntry = 	info.getJSONObject("currentlyEditedEntry");

                if(currentlyEditedEntry != null) {
                    JSONArray usernamesArray = currentlyEditedEntry.getJSONArray("username");

                    if (usernamesArray != null) {
                        ListIterator userNamesI = usernamesArray.listIterator();
                        while (userNamesI.hasNext()) {
                            String userName = (String) userNamesI.next();

                            if (principal != null && StringUtils.equals(principal.getName(), userName)) {
                                editedByMe = true;
                            }
                            else {
                                editedByOthers = true;
                            }

                        }
                    }
                }
            }

        }

        if(editedByMe && !editedByOthers) {
            return true;
        } else {
            return false;
        }

    }

    /* (non-Javadoc)
     * @see org.osivia.services.onlyoffice.portlet.service.IOnlyofficeService#askForLocking(javax.portlet.RenderRequest, javax.portlet.RenderResponse, javax.portlet.PortletContext)
     */
    @Override
    public boolean askForLocking(NuxeoController nuxeoController) throws PortletException {

        NuxeoDocumentContext ctx = nuxeoController.getDocumentContext(getCurrentDoc(nuxeoController.getRequest(), nuxeoController.getResponse(), nuxeoController.getPortletCtx()).getPath());
        NuxeoPublicationInfos infos = ctx.getPublicationInfos();

        Bundle bundle = bundleFactory.getBundle(nuxeoController.getRequest().getLocale());

        if(infos.isBeingModified()) {

            notificationsService.addSimpleNotification(nuxeoController.getPortalCtx(), bundle.getString("CURRENTLY_EDITED"), NotificationsType.WARNING);
            return false;
        }
        else if(infos.getLockStatus() != null && infos.getLockStatus().equals(LockStatus.LOCKED)) {

            notificationsService.addSimpleNotification(nuxeoController.getPortalCtx(), bundle.getString("CURRENTLY_LOCKED"), NotificationsType.WARNING);
            return false;
        } else {
            return true;
        }

    }

    /* (non-Javadoc)
     * @see org.osivia.services.onlyoffice.portlet.service.IOnlyofficeService#lockTemporary(org.osivia.portal.api.context.PortalControllerContext)
     */
    @Override
    public void lockTemporary(NuxeoController nuxeoController) throws PortletException {

        Document currentDoc = getCurrentDoc(nuxeoController.getRequest(), nuxeoController.getResponse(), nuxeoController.getPortletCtx());

        INuxeoCommand command = new LockTemporaryCommand(currentDoc.getId());

        JSONObject info = (JSONObject) nuxeoController.executeNuxeoCommand(command);
        String error = info.getString("error");

        if("1".equals(error)) {
            Bundle bundle = bundleFactory.getBundle(nuxeoController.getRequest().getLocale());
            notificationsService.addSimpleNotification(nuxeoController.getPortalCtx(), bundle.getString("LOCK_ERROR"), NotificationsType.ERROR);
        }
    }

}

