package org.osivia.services.onlyoffice.portlet.command;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import net.sf.json.JSONObject;


public class GetUserJWTTokenCommand implements INuxeoCommand {

    private final String algorithmId;

    public GetUserJWTTokenCommand(String algorithmId) {
        this.algorithmId = algorithmId;
    }

    @Override
    public String execute(Session nuxeoSession) throws Exception {

        OperationRequest newRequest = nuxeoSession.newRequest("UserGroup.GetJWTToken");

        newRequest.set("algorithmId", algorithmId);

        Blob tokenJsonBlob = (Blob) nuxeoSession.execute(newRequest);

        String token = StringUtils.EMPTY;
        if (tokenJsonBlob != null) {
            String tokenJson = IOUtils.toString(tokenJsonBlob.getStream(), "UTF-8");
            JSONObject jsonToken = JSONObject.fromObject(tokenJson);
            token = jsonToken.getString("token");
        }

        return token;
    }

    @Override
    public String getId() {
        return "GetUserJWTTokenCommand/" + algorithmId;
    }

}
