package org.osivia.services.onlyoffice.plugin;

import java.util.List;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarModule;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.liveedit.OnlyofficeLiveEditHelper;

/**
 * OnlyofficePlugin
 *
 * @author dorian
 *
 */
public class OnlyofficePlugin extends AbstractPluginPortlet {

    @Override
    protected void customizeCMSProperties(String customizationId, CustomizationContext context) {
        customizeMenubarModules(context);
    }

    @Override
    protected String getPluginName() {
        return OnlyofficeLiveEditHelper.ONLYOFFICE_PLUGIN_NAME;
    }

    private void customizeMenubarModules(CustomizationContext context) {
        // Menubar modules
        List<MenubarModule> modules = getMenubarModules(context);

        MenubarModule mbModule = new OnlyofficeMenubarModule();
        modules.add(mbModule);
    }

}
