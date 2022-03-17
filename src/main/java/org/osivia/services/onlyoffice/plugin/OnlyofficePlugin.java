package org.osivia.services.onlyoffice.plugin;

import java.util.List;
import java.util.Map;

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.player.IPlayerModule;

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
        // Document types
        this.customizeDocumentTypes(context);
        
        this.customizePlayers(context);

    }

    private void customizePlayers(CustomizationContext context) {
		List<IPlayerModule> players = this.getPlayers(context);
		DocxfPlayer player = new DocxfPlayer();
		players.add(player);
		
	}

	private void customizeDocumentTypes(CustomizationContext context) {
        // Document types
        Map<String, DocumentType> types = this.getDocTypes(context);

        
        DocumentType docxf = new DocumentType("DocxfFile", false, true, true, false, true, true, null, null, "glyphicons glyphicons-list-alt");
        docxf.setFile(true);
        types.put(docxf.getName() ,docxf);
        
        DocumentType oform = new DocumentType("OformFile", false, true, true, false, true, true, null, null, "glyphicons glyphicons-elections");
        oform.setFile(true);
        types.put(oform.getName() ,oform);

	}

    
    
    
	@Override
    protected String getPluginName() {
        return OnlyofficeLiveEditHelper.ONLYOFFICE_PLUGIN_NAME;
    }



}
