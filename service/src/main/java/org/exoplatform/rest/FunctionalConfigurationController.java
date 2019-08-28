package org.exoplatform.rest;

import static java.util.Objects.nonNull;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.rest.response.FunctionalConfigurationResponse;
import org.exoplatform.services.rest.resource.ResourceContainer;

@Path("/functional-configuration")
public class FunctionalConfigurationController implements ResourceContainer {
    private final String DOES_DOCUMENT_EDITION_SHOULD_CREATE_ACTIVITY = "DOES_DOCUMENT_EDITION_SHOULD_CREATE_ACTIVITY";
    private SettingService settingService;

    public FunctionalConfigurationController(SettingService settingService){
        this.settingService = settingService;
    }

    @POST
    @Path("/hide-user-activity-composer")
    public Response hideUserActivity(String isHidden) {
        settingService.set(Context.GLOBAL, Scope.GLOBAL, "hideUserActivityComposer", SettingValue.create(isHidden));
        return Response.ok().build();
    }

    @POST
    @Path("/hide-document-activities")
    public Response hideDocumentsActionsActivity(String isHidden) {

        settingService.set(Context.GLOBAL, Scope.GLOBAL, "DOES_DOCUMENT_EDITION_SHOULD_CREATE_ACTIVITY", SettingValue.create(isHidden));
        return Response.ok().build();
    }

    @GET
    @Path("/configuration")
    public Response getConfiguration(){

        FunctionalConfigurationResponse response = new FunctionalConfigurationResponse();
        response.setHideComposerActivities(true);
        response.setHideDocumentActionActivities(true);

        return Response
                .ok(response, MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/listening")
    public Response isListeningDocumentActivity() {
        SettingValue<?> isListening = settingService.get(Context.GLOBAL, Scope.GLOBAL, DOES_DOCUMENT_EDITION_SHOULD_CREATE_ACTIVITY);

        if (nonNull(isListening)) {
            return Response.ok(isListening.getValue()).build();
        }
        else {
            return Response.ok(true).build();
        }
    }


    //todo change get to post
    @GET
    @Path("/set-listening")
    public Response setListeningDocumentActivity(@QueryParam("status") String status) {
        if (status.equals("true") || status.equals("false")) {
            boolean booleanStatus = status.equals("true");

            settingService.set(Context.GLOBAL, Scope.GLOBAL, DOES_DOCUMENT_EDITION_SHOULD_CREATE_ACTIVITY, SettingValue.create(booleanStatus));

            return Response.ok(status).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
