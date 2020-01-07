package org.exoplatform.service.helpers;

import org.exoplatform.rest.response.HighlightSpaceConfiguration;
import org.exoplatform.rest.response.SpaceConfiguration;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SpaceAssertion {

    public static void assertSpaceConfiguration(SpaceConfiguration actual, SpaceConfiguration expected) {
        assertThat(actual.getId(), equalTo(expected.getId()));
        assertThat(actual.getDisplayName(), equalTo(expected.getDisplayName()));
        assertThat(actual.getDescription(), equalTo(expected.getDescription()));
        assertThat(actual.isActivityComposerVisible(), equalTo(expected.isActivityComposerVisible()));
        HighlightSpaceConfiguration highlightConfiguration = actual.getHighlightConfiguration();

        if (Objects.nonNull(highlightConfiguration) && Objects.nonNull(expected.getHighlightConfiguration())) {
            assertThat(highlightConfiguration.getOrder(), equalTo(expected.getHighlightConfiguration().getOrder()));
            assertThat(highlightConfiguration.isHighlight(), equalTo(expected.getHighlightConfiguration().isHighlight()));
        }
    }
}
