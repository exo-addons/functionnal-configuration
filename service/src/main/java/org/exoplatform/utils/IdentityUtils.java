package org.exoplatform.utils;

import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.service.exception.FunctionalConfigurationRuntimeException;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;

import static java.util.Objects.isNull;

public class IdentityUtils {

    public static Profile findUserProfileByUserName(String userName) {
        Identity identity = CommonsUtils.getService(IdentityManager.class)
                .getOrCreateIdentity(OrganizationIdentityProvider.NAME, userName, true);

        if (isNull(identity)) {
            throw new FunctionalConfigurationRuntimeException("identityNotFound");
        }

        return identity.getProfile();
    }
}
