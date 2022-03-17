package org.osivia.services.onlyoffice.plugin;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.player.Player;

import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;

public class DocxfPlayer implements INuxeoPlayerModule  {

	
	/* (non-Javadoc)
	 * @see org.osivia.portal.api.cms.IPlayerModule#getCMSPlayer(org.osivia.portal.api.cms.DocumentContext)
	 */
	@Override
	public Player getCMSPlayer(DocumentContext<Document> docCtx) {
		
        // Document
        Document document = docCtx.getDoc();
		
        if ("DocxfFile".equals(document.getType()) || "OformFile".equals(document.getType())) {
            // Window properties
            Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put("osivia.document.dispatch.jsp", "file");
            windowProperties.put(Constants.WINDOW_PROP_URI, document.getPath());

            Player props = new Player();
            props.setWindowProperties(windowProperties);
            props.setPortletInstance("toutatice-portail-cms-nuxeo-viewDocumentPortletInstance");

            return props;
        }
        else return null;
	}
}
