package org.exoplatform.override;

import org.exoplatform.commons.file.services.FileService;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.portal.config.UserACL;
import org.exoplatform.service.FunctionalConfigurationService;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.manager.ActivityManagerImpl;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.storage.api.ActivityStorage;
import org.exoplatform.upload.UploadService;

public class FunctionalConfigurationActivityManagerImpl extends ActivityManagerImpl {

    private static final Log LOG = ExoLogger.getLogger(ActivityManagerImpl.class);

    private FunctionalConfigurationService functionalConfigurationService;
    public static final String DOCLINK = "DOCLINK";


    public FunctionalConfigurationActivityManagerImpl(FunctionalConfigurationService functionalConfigurationService,ActivityStorage activityStorage, IdentityManager identityManager, UserACL userACL, FileService fileService, UploadService uploadService, RepositoryService repositoryService, NodeHierarchyCreator nodeHierarchyCreator, InitParams params) {
        super(activityStorage, identityManager, userACL,
                fileService, uploadService, repositoryService,
                nodeHierarchyCreator, params);
        this.functionalConfigurationService=functionalConfigurationService;
    }

    @Override
    public void saveActivityNoReturn(Identity streamOwner, ExoSocialActivity newActivity) {


        boolean hideActivity=documentActivityHidden(newActivity);
        LOG.debug("Functional Configuration ActivityManagerOverride, check if we want activity for documents : documentActivityHidden="+hideActivity);
        if(!hideActivity) {
            super.saveActivityNoReturn(streamOwner, newActivity);
        }
    }
    @Override
    public void updateActivity(ExoSocialActivity existingActivity) {
        //in saveActivityNoReturn, if activity is contents:spaces, and so hidden, Utils.postActivity call updateActivity
        //with an activity with no id. So in this case, we don't update the activity.
        if (existingActivity.getId()!=null) {
            super.updateActivity(existingActivity);
        }
    }

    private boolean documentActivityHidden(ExoSocialActivity newActivity) {
        return functionalConfigurationService.isDocumentActionActivityHidden() &&
                (
                    (
                        newActivity.getType().equals("files:spaces") &&
                        !newActivity.getTemplateParams().containsKey(DOCLINK)
                    ) || newActivity.getType().equals("contents:spaces")
                );

        //functionConfiguration must have document activities hidden
        //&& activity must be type files:spaces
        //&& templateParams must not contain "DOCLINK" (if it contains it, the activity is created from the composer).
        //if activity is contents:space, we hide it. In fact this type is use when integ-ecms-social use Utils.postActivity for a comment, and doesnt find the initial activity. In this case a new activity of typs contents:space is created.
    }
}
