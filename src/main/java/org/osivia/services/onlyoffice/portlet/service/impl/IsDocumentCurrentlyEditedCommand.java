/**
 * 
 */
package org.osivia.services.onlyoffice.portlet.service.impl;

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * @author Loïc Billon
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IsDocumentCurrentlyEditedCommand implements INuxeoCommand {

	
	private String id;

	public IsDocumentCurrentlyEditedCommand(String id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#execute(org.nuxeo.ecm.automation.client.Session)
	 */
	@Override
	public JSONObject execute(Session nuxeoSession) throws Exception {
		
		OperationRequest operation = nuxeoSession.newRequest("Document.IsDocumentCurrentlyEdited").set("id", this.id);
		Blob blob = (Blob)  operation.execute();
		JSONObject jsonObject = new JSONObject();
		
		if (blob != null) {
            String fileContent = IOUtils.toString(blob.getStream(), CharEncoding.UTF_8);
            
            JSONArray json = JSONArray.fromObject(fileContent);
            
            Iterator<?> it = json.iterator();
            while (it.hasNext()) {
            	jsonObject = (JSONObject) it.next();
            }
            
        }
		return jsonObject;
		
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#getId()
	 */
	@Override
	public String getId() {
		return this.getClass().getSimpleName() + "/" + id;
	}

}
