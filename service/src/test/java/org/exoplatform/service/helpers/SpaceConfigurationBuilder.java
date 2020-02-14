package org.exoplatform.service.helpers;

import org.exoplatform.rest.response.HighlightSpaceConfiguration;
import org.exoplatform.rest.response.SpaceConfiguration;

public class SpaceConfigurationBuilder {

    private SpaceConfiguration spaceConfiguration;

    public  SpaceConfigurationBuilder() {
        this.spaceConfiguration = new SpaceConfiguration();
    }

    public SpaceConfigurationBuilder spaceId(String spaceId) {
        spaceConfiguration.setId(spaceId);
        return this;
    }

    public SpaceConfigurationBuilder displayName(String displayName) {
        spaceConfiguration.setDisplayName(displayName);
        return this;
    }

    public SpaceConfigurationBuilder descritpion(String description) {
        spaceConfiguration.setDescription(description);
        return this;
    }

    public SpaceConfigurationBuilder activityComposerVisible(boolean activityComposerVisible) {
        spaceConfiguration.setActivityComposerVisible(activityComposerVisible);
        return this;
    }

    public SpaceConfigurationBuilder order(int order) {
        HighlightSpaceConfiguration highlightConfiguration = new HighlightSpaceConfiguration();
        highlightConfiguration.setOrder(order);
        highlightConfiguration.setHighlight(true);
        spaceConfiguration.setHighlightConfiguration(highlightConfiguration);
        return this;
    }

    public SpaceConfiguration build() {
        return spaceConfiguration;
    }
}