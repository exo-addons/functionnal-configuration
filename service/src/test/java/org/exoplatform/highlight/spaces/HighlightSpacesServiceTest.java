package org.exoplatform.highlight.spaces;

import org.exoplatform.rest.response.HighlightSpaceConfiguration;
import org.exoplatform.rest.response.SpaceConfiguration;
import org.exoplatform.service.FunctionalConfigurationService;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HighlightSpacesServiceTest {

    private HighlightSpacesService highlightSpacesService;

    @Mock
    private SpaceService spaceService;

    @Mock
    private FunctionalConfigurationService functionalConfigurationService;

    @Before
    public void setUp() {
        highlightSpacesService = new HighlightSpacesService(spaceService, functionalConfigurationService);
    }

    @Test
    public void should_return_not_highlighted_spaces_in_legacy_group_when_no_config_found() {
        // Given
        String REMOTE_USER = "me";

        // When
        List<HighlightSpace> highlightedSpaces = highlightSpacesService.getHighlightedSpacesForUser(REMOTE_USER);

        // Then
        assertThat(highlightedSpaces.size(), equalTo(0));
    }

    @Test
    public void should_return_not_highlighted_spaces_in_group_when_no_config_found() {
        // Given
        String REMOTE_USER = "me";

        // When
        List<HighlightSpace> highlightedSpaces = highlightSpacesService.getHighlightedSpacesForUser("1", REMOTE_USER);

        // Then
        assertThat(highlightedSpaces.size(), equalTo(0));
    }

    @Test
    public void should_return_no_highlighted_space_in_legacy_group_when_no_highlighted_space_in_this_group() {
        // Given
        String REMOTE_USER = "me";

        // When
        List<HighlightSpace> highlightedSpaces = highlightSpacesService.getHighlightedSpacesForUser(REMOTE_USER);

        // Then
        assertThat(highlightedSpaces.size(), equalTo(0));
    }

    @Test
    public void should_return_highlighted_space_in_group_when_remote_user_is_a_space_member() {
        // Given
        String REMOTE_USER = "me";

        String SPACE_ID_1 = "space1";
        String SPACE_ID_2 = "space2";
        String SPACE_ID_3 = "space3";

        Space SPACE_1 = new Space();
        SPACE_1.setId(SPACE_ID_1);
        SPACE_1.setPrettyName(SPACE_ID_1);
        Space SPACE_2 = new Space();
        SPACE_2.setId(SPACE_ID_2);
        SPACE_2.setPrettyName(SPACE_ID_2);
        Space SPACE_3 = new Space();
        SPACE_3.setId(SPACE_ID_3);
        SPACE_3.setPrettyName(SPACE_ID_3);

        HighlightSpaceConfiguration highlightSpaceConfiguration1 = new HighlightSpaceConfiguration();
        highlightSpaceConfiguration1.setHighlight(true);
        highlightSpaceConfiguration1.setGroupIdentifier("1");
        highlightSpaceConfiguration1.setOrder(1);
        SpaceConfiguration spaceConfiguration1 = new SpaceConfiguration();
        spaceConfiguration1.setId(SPACE_ID_1);
        spaceConfiguration1.setHighlightConfiguration(highlightSpaceConfiguration1);

        HighlightSpaceConfiguration highlightSpaceConfiguration2 = new HighlightSpaceConfiguration();
        highlightSpaceConfiguration2.setHighlight(true);
        highlightSpaceConfiguration2.setGroupIdentifier("2");
        highlightSpaceConfiguration2.setOrder(1);
        SpaceConfiguration spaceConfiguration2 = new SpaceConfiguration();
        spaceConfiguration2.setId(SPACE_ID_2);
        spaceConfiguration2.setHighlightConfiguration(highlightSpaceConfiguration2);

        HighlightSpaceConfiguration highlightSpaceConfiguration3 = new HighlightSpaceConfiguration();
        highlightSpaceConfiguration3.setHighlight(true);
        highlightSpaceConfiguration3.setGroupIdentifier("2");
        highlightSpaceConfiguration3.setOrder(1);
        SpaceConfiguration spaceConfiguration3 = new SpaceConfiguration();
        spaceConfiguration3.setId(SPACE_ID_3);
        spaceConfiguration3.setHighlightConfiguration(highlightSpaceConfiguration3);

        given(functionalConfigurationService.getSpacesForGroup(eq("2"))).willReturn(Arrays.asList(spaceConfiguration2, spaceConfiguration3));
        given(spaceService.getSpaceById(SPACE_ID_2)).willReturn(SPACE_2);
        given(spaceService.getSpaceById(SPACE_ID_3)).willReturn(SPACE_3);
        given(spaceService.isMember(eq(SPACE_2), eq(REMOTE_USER))).willReturn(true);
        given(spaceService.isMember(eq(SPACE_3), eq(REMOTE_USER))).willReturn(false);

        // When
        List<HighlightSpace> highlightedSpaces = highlightSpacesService.getHighlightedSpacesForUser("2", REMOTE_USER);

        // Then
        assertThat(highlightedSpaces.size(), equalTo(1));
        assertThat(highlightedSpaces.get(0).getGroupIdentifier(), equalTo("2"));
        assertThat(highlightedSpaces.get(0).getId(), equalTo(SPACE_ID_2));
    }

    @Test
    public void should_return_sorted_space_by_order_and_name() {
        // Given
        String REMOTE_USER = "me";

        String DISPLAY_NAME_1 = "DISPLAY_NAME_1";
        String DISPLAY_NAME_2 = "DISPLAY_NAME_2";
        String DISPLAY_NAME_3 = "DISPLAY_NAME_3";

        String SPACE_ID_1 = "1";
        String SPACE_ID_2 = "2";
        String SPACE_ID_3 = "3";

        Space SPACE_1 = new Space();
        SPACE_1.setId(SPACE_ID_1);
        SPACE_1.setDisplayName(DISPLAY_NAME_1);
        Space SPACE_2 = new Space();
        SPACE_2.setId(SPACE_ID_2);
        SPACE_2.setDisplayName(DISPLAY_NAME_2);
        Space SPACE_3 = new Space();
        SPACE_3.setId(SPACE_ID_3);
        SPACE_3.setDisplayName(DISPLAY_NAME_3);

        HighlightSpaceConfiguration highlightSpaceConfiguration1 = new HighlightSpaceConfiguration();
        highlightSpaceConfiguration1.setHighlight(true);
        highlightSpaceConfiguration1.setGroupIdentifier("1");
        highlightSpaceConfiguration1.setOrder(6);
        SpaceConfiguration spaceConfiguration1 = new SpaceConfiguration();
        spaceConfiguration1.setId(SPACE_ID_1);
        spaceConfiguration1.setHighlightConfiguration(highlightSpaceConfiguration1);

        HighlightSpaceConfiguration highlightSpaceConfiguration2 = new HighlightSpaceConfiguration();
        highlightSpaceConfiguration2.setHighlight(true);
        highlightSpaceConfiguration2.setGroupIdentifier("2");
        highlightSpaceConfiguration2.setOrder(1);
        SpaceConfiguration spaceConfiguration2 = new SpaceConfiguration();
        spaceConfiguration2.setId(SPACE_ID_2);
        spaceConfiguration2.setHighlightConfiguration(highlightSpaceConfiguration2);

        HighlightSpaceConfiguration highlightSpaceConfiguration3 = new HighlightSpaceConfiguration();
        highlightSpaceConfiguration3.setHighlight(true);
        highlightSpaceConfiguration3.setGroupIdentifier("2");
        highlightSpaceConfiguration3.setOrder(1);
        SpaceConfiguration spaceConfiguration3 = new SpaceConfiguration();
        spaceConfiguration3.setId(SPACE_ID_3);
        spaceConfiguration3.setHighlightConfiguration(highlightSpaceConfiguration3);

        given(functionalConfigurationService.getSpacesForGroup(eq("1"))).willReturn(Arrays.asList(spaceConfiguration1, spaceConfiguration2, spaceConfiguration3));
        given(spaceService.getSpaceById(SPACE_ID_1)).willReturn(SPACE_1);
        given(spaceService.getSpaceById(SPACE_ID_2)).willReturn(SPACE_2);
        given(spaceService.getSpaceById(SPACE_ID_3)).willReturn(SPACE_3);
        when(spaceService.isMember(any(Space.class), anyString())).thenReturn(true);

        // When
        List<HighlightSpace> highlightedSpaces = highlightSpacesService.getHighlightedSpacesForUser("1", REMOTE_USER);

        // Then
        assertThat(highlightedSpaces.size(), equalTo(3));
        assertThat(highlightedSpaces.get(0).getId(), equalTo(SPACE_ID_2));
        assertThat(highlightedSpaces.get(1).getId(), equalTo(SPACE_ID_3));
        assertThat(highlightedSpaces.get(2).getId(), equalTo(SPACE_ID_1));
    }
}
