package org.exoplatform.rest.response;

import java.io.Serializable;

public class SpaceConfiguration implements Serializable {

    private String id;
    private String displayName;
    private String description;
    private boolean activityComposerVisible;

    private HighlightSpaceConfiguration highlightConfiguration;
    private String spaceUri;

    public SpaceConfiguration() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActivityComposerVisible() {
        return activityComposerVisible;
    }

    public void setActivityComposerVisible(boolean activityComposerVisible) {
        this.activityComposerVisible = activityComposerVisible;
    }

    public HighlightSpaceConfiguration getHighlightConfiguration() {
        return highlightConfiguration;
    }

    public void setHighlightConfiguration(HighlightSpaceConfiguration highlightConfiguration) {
        this.highlightConfiguration = highlightConfiguration;
    }

    public void setSpaceUri(String spaceUri) {
        this.spaceUri = spaceUri;
    }

    public String getSpaceUri() {
        return spaceUri;
    }
}
