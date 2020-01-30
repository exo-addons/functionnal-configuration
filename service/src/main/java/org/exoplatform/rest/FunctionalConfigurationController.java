package org.exoplatform.rest;

import org.exoplatform.rest.response.SpaceConfiguration;
import org.exoplatform.rest.response.TermsAndConditions;
import org.exoplatform.service.FunctionalConfigurationService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.exoplatform.rest.utils.RestUtils.isValidBooleanParameter;

@Path("/functional-configuration")
public class FunctionalConfigurationController implements ResourceContainer {

    private static final String DOCUMENT_ACTIVITY_ENDPOINT = "/document-activity";
    private static final String CONFIGURATION_ENDPOINT = "/configuration";
    private static final String SPACES_BY_GROUP = "/group/{id}/spaces";
    private static final String COMPOSER_ACTIVITY_ENDPOINT = "/composer-activity";
    private static final String UPDATE_SPACE_CONFIGURATION_ENDPOINT = "/configuration/space";
    private static final String UPDATE_TERMS_AND_CONDITIONS = "/terms-and-conditions";

    private FunctionalConfigurationService functionalConfigurationService;

    private static final Log LOGGER = ExoLogger.getLogger(FunctionalConfigurationService.class);


    public FunctionalConfigurationController(FunctionalConfigurationService functionalConfigurationService){
        this.functionalConfigurationService = functionalConfigurationService;
    }

    @GET
    @Path(SPACES_BY_GROUP)
    public Response getSpacesForGroup(@PathParam("id") String groupIdentifier){

        return Response
                .ok(functionalConfigurationService.getSpacesForGroup(groupIdentifier), MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path(CONFIGURATION_ENDPOINT)
    @RolesAllowed("administrators")
    public Response getConfiguration(){
        LOGGER.info("FunctionalConfiguration : GetConfiguration");
        return Response
                .ok(functionalConfigurationService.getConfiguration(), MediaType.APPLICATION_JSON)
                .build();
    }

    @PUT
    @Path(DOCUMENT_ACTIVITY_ENDPOINT)
    @RolesAllowed("administrators")
    public Response updateDocumentActionActivitiesVisibility(@QueryParam("hidden") String hidden) {
        LOGGER.info("FunctionalConfiguration : updateDocumentActionActivitiesVisibility, hidden="+hidden);

        if (!isValidBooleanParameter(hidden)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        functionalConfigurationService.configureDocumentActionActivities(hidden);

        return Response.ok().build();
    }

    @PUT
    @Path(COMPOSER_ACTIVITY_ENDPOINT)
    @RolesAllowed("administrators")
    public Response updateComposerActivity(@QueryParam("hidden") String hidden) {
        LOGGER.info("FunctionalConfiguration : updateComposerActivity, hidden="+hidden);

        if (!isValidBooleanParameter(hidden)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        functionalConfigurationService.configureActivityComposer(hidden);
        return Response.ok().build();
    }


    @PUT
    @Path(UPDATE_SPACE_CONFIGURATION_ENDPOINT)
    @RolesAllowed("administrators")
    public Response updateSpaceConfiguration(SpaceConfiguration space) {
        LOGGER.info("FunctionalConfiguration : updateSpaceConfiguration for space "+space.getDisplayName());

        SpaceConfiguration spaceConfiguration = functionalConfigurationService.updateSpaceConfiguration(space);

        return Response
                .ok(spaceConfiguration, MediaType.APPLICATION_JSON)
                .build();
    }

    @PUT
    @Path(UPDATE_TERMS_AND_CONDITIONS)
    @RolesAllowed("administrators")
    public Response updateTermsAndConditions(TermsAndConditions termsAndConditions) {
        LOGGER.info("FunctionalConfiguration : updateTermsAndConditions", termsAndConditions);

        functionalConfigurationService.updateTermsAndConditions(termsAndConditions);

        return Response
                .ok(MediaType.APPLICATION_JSON)
                .build();
    }
}
