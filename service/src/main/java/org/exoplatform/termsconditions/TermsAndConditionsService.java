package org.exoplatform.termsconditions;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.portal.config.UserACL;
import org.exoplatform.rest.response.TermsAndConditions;
import org.exoplatform.service.FunctionalConfigurationService;
import org.exoplatform.service.exception.FunctionalConfigurationRuntimeException;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.utils.NodeUtils;

import javax.jcr.Node;
import java.util.Objects;

import static java.util.Objects.isNull;

public class TermsAndConditionsService {

    private static final Log LOGGER = ExoLogger.getLogger(TermsAndConditionsService.class);

    private static final String TERMS_AND_CONDITONS_PROPERTY = "acceptedTermsAndConditions";

    private FunctionalConfigurationService functionalConfigurationService;

    private OrganizationService organizationService;

    private UserACL userACL;

    public TermsAndConditionsService(FunctionalConfigurationService functionalConfigurationService,
                                     OrganizationService organizationService,
                                     UserACL userACL) {
        this.functionalConfigurationService = functionalConfigurationService;
        this.organizationService = organizationService;
        this.userACL = userACL;
    }

    boolean isTermsAndConditionsAcceptedBy(String userName){
        if(userACL.isSuperUser()) {
            return true;
        }

        try {
            UserProfile userProfile = findUserProfileByUserName(userName);

            String acceptedTermsVersion = userProfile.getAttribute(TERMS_AND_CONDITONS_PROPERTY);
            String currentVersion = getCurrentTermsAndConditionsVersion();

            return currentVersion.equals(acceptedTermsVersion);
        } catch (Exception e) {
            LOGGER.warn("Error while checking Terms and conditions accepted for user: " + userName + " - considered as not accepted", e);
            return false;
        }
    }

    private String getCurrentTermsAndConditionsVersion() {

        final String DEFAULT_DRAFT_VERSION_NAME = "jcr:rootVersion";

        TermsAndConditions termsAndConditions = functionalConfigurationService.getTermsAndConditions();
        Node termsAndConditionsNode = NodeUtils.findCollaborationFile(termsAndConditions.getWebContentUrl());

        try {
            String name = termsAndConditionsNode.getBaseVersion().getName();

            if (DEFAULT_DRAFT_VERSION_NAME.equals(name)) {
                throw new FunctionalConfigurationRuntimeException("invalid.termsAndConditions");
            }

            return name;
        } catch (Exception e) {
            throw new FunctionalConfigurationRuntimeException("invalid.termsAndConditions");
        }
    }

    public void accept(String userName) {

        RequestLifeCycle.begin(PortalContainer.getInstance());
        try {
            UserProfile userProfile = findUserProfileByUserName(userName);
            String currentVersionName = getCurrentTermsAndConditionsVersion();

            userProfile.setAttribute(TERMS_AND_CONDITONS_PROPERTY, currentVersionName);

            organizationService.getUserProfileHandler().saveUserProfile(userProfile, false);
        } catch (Exception e) {
            LOGGER.error("Cannot update user profile to store terms and conditions acceptation", e);
        } finally {
            RequestLifeCycle.end();
        }

    }

    public boolean isTermsAndConditionsActive()  {

        TermsAndConditions termsAndConditions = functionalConfigurationService.getTermsAndConditions();

        boolean isValidFile;
        try {
            String currentVersion = getCurrentTermsAndConditionsVersion();
            isValidFile = Objects.nonNull(NodeUtils.findCollaborationFile(termsAndConditions.getWebContentUrl()))
                    && StringUtils.isNotEmpty(currentVersion);
        } catch (Exception e) {
            return false;
        }

        return termsAndConditions.isActive() && isValidFile;
    }

    private UserProfile findUserProfileByUserName(String userName) throws Exception {
        User user = organizationService.getUserHandler().findUserByName(userName);
        if (isNull(user)) {
            throw new FunctionalConfigurationRuntimeException("User not found");
        }

        UserProfile userProfile = organizationService.getUserProfileHandler().findUserProfileByName(userName);
        if(userProfile == null) {
            userProfile = organizationService.getUserProfileHandler().createUserProfileInstance(userName);
        }

        return userProfile;
    }
}
