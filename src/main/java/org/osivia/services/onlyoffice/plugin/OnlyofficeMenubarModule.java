package org.osivia.services.onlyoffice.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.cms.impl.BasicPermissions;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * OnlyofficeMenubarModule
 *
 * @author dorian
 */
public class OnlyofficeMenubarModule implements MenubarModule {

    private static final String ONLYOFFICE_PORTLET_INSTANCE = "osivia-services-onlyoffice-portletInstance";

    /** Menubar service. */
    private final IMenubarService menubarService;

    /** Internationalization bundle factory. */
    private final IBundleFactory bundleFactory;

    public OnlyofficeMenubarModule() {
        super();

        menubarService = Locator.findMBean(IMenubarService.class, IMenubarService.MBEAN_NAME);

        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }

    @Override
    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
            DocumentContext<? extends EcmDocument> spaceDocumentContext) throws PortalException {
    }

    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,  DocumentContext<? extends EcmDocument> documentContext) throws PortalException {

        EcmDocument document = documentContext.getDoc();
        BasicPermissions basicPermissions = documentContext.getPermissions(BasicPermissions.class);
        if (basicPermissions.isEditableByUser() && document != null && document instanceof Document) {

            Document nuxeoDocument = (Document) document;
            String type = nuxeoDocument.getType();
            String documentPath = nuxeoDocument.getPath();

            if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(documentPath)) {

                // build onlyoffice menuitem
                NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

                Bundle bundle = bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());
                final MenubarDropdown parent = menubarService.getDropdown(portalControllerContext, MenubarDropdown.CMS_EDITION_DROPDOWN_MENU_ID);
                MenubarItem item;
                if (nuxeoDocument.isLocked()) {
                    item = new MenubarItem("LIVE_EDIT", bundle.getString("LIVE_EDIT"), "halflings halflings-pencil", parent, 0, "#", null, null,
                            null);
                    item.isDisabled();
                } else {
                    // build onlyoffice portlet url
                    Map<String, String> windowProperties = new HashMap<>();
                    windowProperties.put(Constants.WINDOW_PROP_URI, documentPath);

                    String url = nuxeoController.getPortalUrlFactory().getStartPortletUrl(portalControllerContext, ONLYOFFICE_PORTLET_INSTANCE,
                            windowProperties);

                    item = new MenubarItem("LIVE_EDIT", bundle.getString("LIVE_EDIT"), "halflings halflings-pencil", parent, 0, url, null, null,
                            null);
                }
                menubar.add(item);
            }
        }
    }
}
