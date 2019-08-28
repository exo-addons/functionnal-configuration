package org.exoplatform.rest;

import static java.util.Objects.nonNull;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.rest.response.FunctionalConfigurationResponse;
import org.exoplatform.service.FunctionalConfigurationService;
import org.exoplatform.services.rest.resource.ResourceContainer;

@Path("/functional-configuration")
public class FunctionalConfigurationController implements ResourceContainer {
    private FunctionalConfigurationService functionalConfigurationService;

    public FunctionalConfigurationController(FunctionalConfigurationService functionalConfigurationService){
        this.functionalConfigurationService = functionalConfigurationService;
    }

//    @POST
//    @Path("/hide-user-activity-composer")
//    public Response hideUserActivity(String isHidden) {
//        settingService.set(Context.GLOBAL, Scope.GLOBAL, "hideUserActivityComposer", SettingValue.create(isHidden));
//        return Response.ok().build();
//    }

//    @POST
//    @Path("/hide-document-activities")
//    public Response hideDocumentsActionsActivity(String isHidden) {
//
//        settingService.set(Context.GLOBAL, Scope.GLOBAL, "DOES_DOCUMENT_EDITION_SHOULD_CREATE_ACTIVITY", SettingValue.create(isHidden));
//        return Response.ok().build();
//    }

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


    @PUT
    @Path("/hide-document-activities")
    public Response updateDocumentActionActivitiesVisibility(@QueryParam("hidden") String hidden) {

//        functionalConfigurationService.updateDocumentActionActivitiesVisibility(hidden);

        return Response.ok().build();
    }


    @PUT
    @Path("/composer-activity")
    public Response updateComposerActivity(@QueryParam("hidden") String hidden) {

//        functionalConfigurationService.updateDocumentActionActivitiesVisibility(hidden);

        return Response.ok().build();
    }

    //todo change get to post
//    @GET
//    @Path("/set-listening")
//    public Response setListeningDocumentActivity(@QueryParam("status") String status) {
//
//    }

}
