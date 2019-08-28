package org.exoplatform.rest.response;

public class FunctionalConfigurationResponse {

    private boolean hideDocumentActionActivities;

    private boolean hideComposerActivities;

    public boolean isHideDocumentActionActivities() {
        return hideDocumentActionActivities;
    }

    public void setHideDocumentActionActivities(boolean hideDocumentActionActivities) {
        this.hideDocumentActionActivities = hideDocumentActionActivities;
    }

    public boolean isHideComposerActivities() {
        return hideComposerActivities;
    }

    public void setHideComposerActivities(boolean hideComposerActivities) {
        this.hideComposerActivities = hideComposerActivities;
    }
}