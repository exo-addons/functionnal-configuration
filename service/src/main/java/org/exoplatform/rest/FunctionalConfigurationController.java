package org.exoplatform.rest;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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
    public Response updateDocumentActionActivitiesVisibility(@QueryParam("hidden") String hiddenAsString) {

        if (!isValidBooleanParameter(hiddenAsString)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        functionalConfigurationService.updateDocumentActionActivity(Boolean.valueOf(hiddenAsString));

        return Response.ok().build();
    }

    private boolean isValidBooleanParameter(String value) {
        List<String> booleansValueAsString = Arrays.asList("true", "false");

        return isNotEmpty(value) && booleansValueAsString.contains(value);
    }

    @PUT
    @Path("/composer-activity")
    public Response updateComposerActivity(@QueryParam("hidden") String hiddenAsString) {

        if (!isValidBooleanParameter(hiddenAsString)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        functionalConfigurationService.updateUserActivityComposer(Boolean.valueOf(hiddenAsString));
        return Response.ok().build();
    }
}
