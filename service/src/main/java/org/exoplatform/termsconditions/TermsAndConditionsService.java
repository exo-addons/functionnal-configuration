package org.exoplatform.termsconditions;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.rest.response.TermsAndConditions;
import org.exoplatform.service.FunctionalConfigurationService;
import org.exoplatform.service.exception.FunctionalConfigurationRuntimeException;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.utils.NodeUtils;

import javax.jcr.Node;
import java.util.Objects;

import static org.exoplatform.utils.IdentityUtils.findUserProfileByUserName;

public class TermsAndConditionsService {

    private static final String TERMS_AND_CONDITONS_PROPERTY = "acceptedTermsAndConditions";
    private static final Log LOGGER = ExoLogger.getLogger(TermsAndConditionsService.class);

    private final FunctionalConfigurationService functionalConfigurationService;

    private IdentityManager identityManager;


    public TermsAndConditionsService(FunctionalConfigurationService functionalConfigurationService, IdentityManager identityManager) {
        this.functionalConfigurationService = functionalConfigurationService;
        this.identityManager = identityManager;
    }

    boolean isTermsAndConditionsAcceptedBy(String userName){
        Profile socialProfile = findUserProfileByUserName(userName);

        try {
            String acceptedTermsVersion = socialProfile.getProperty(TERMS_AND_CONDITONS_PROPERTY) + "";
            String currentVersion = getCurrentTermsAndConditionsVersion();

            return currentVersion.equals(acceptedTermsVersion);
        } catch (Exception e) {
            LOGGER.warn("Terms and conditions still not accepted by user: " + userName, e);
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

    public void accept(String userName){

        Profile userProfile = findUserProfileByUserName(userName);
        String currentVersionName = getCurrentTermsAndConditionsVersion();

        userProfile.setProperty(TERMS_AND_CONDITONS_PROPERTY, currentVersionName);

        try {
            identityManager.updateProfile(userProfile);
        } catch (Exception e) {
            LOGGER.error("Cannot update user profile to store terms and conditions acceptation", e);
        }
    }

    boolean isTermsAndConditionsActive()  {

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
}
