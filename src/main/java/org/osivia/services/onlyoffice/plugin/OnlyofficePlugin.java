package org.osivia.services.onlyoffice.plugin;

import org.osivia.portal.api.customization.CustomizationContext;

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
    protected String getPluginName() {
        return OnlyofficeLiveEditHelper.ONLYOFFICE_PLUGIN_NAME;
    }

    @Override
    protected void customizeCMSProperties(CustomizationContext context) {
        // TODO Auto-generated method stub
        
    }



}
