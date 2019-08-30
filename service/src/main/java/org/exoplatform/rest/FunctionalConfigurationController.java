package org.exoplatform.rest;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.exoplatform.service.FunctionalConfigurationService;
import org.exoplatform.services.rest.resource.ResourceContainer;

import java.util.Arrays;
import java.util.List;

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

        return Response
                .ok(functionalConfigurationService.getConfiguration(), MediaType.APPLICATION_JSON)
                .build();
    }

    @PUT
    @Path("/document-activity")
    public Response updateDocumentActionActivitiesVisibility(@QueryParam("hidden") String hidden) {

        if (!isValidBooleanParameter(hidden)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        functionalConfigurationService.configureActivityComposer(hidden);

        return Response.ok().build();
    }

    private boolean isValidBooleanParameter(String value) {
        List<String> booleansValueAsString = Arrays.asList("true", "false");
        return isNotEmpty(value) && booleansValueAsString.contains(value);
    }

    @PUT
    @Path("/composer-activity")
    public Response updateComposerActivity(@QueryParam("hidden") String hidden) {

        if (!isValidBooleanParameter(hidden)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        functionalConfigurationService.configureActivityComposer(hidden);
        return Response.ok().build();
    }
}
