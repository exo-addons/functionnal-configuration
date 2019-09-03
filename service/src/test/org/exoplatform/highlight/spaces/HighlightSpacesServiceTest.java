package org.exoplatform.highlight.spaces;

import org.exoplatform.service.FunctionalConfigurationService;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    public void should_return_not_highlight_spaces_when_no_config_found() {

        String REMOTE_USER = "me";

        given(functionalConfigurationService.loadHighlightConfigAsMap()).willReturn(new HashMap<>());

        List<Space> highlightedSpaces = highlightSpacesService.getUserHighlightedSpaces(REMOTE_USER);

        assertThat(highlightedSpaces.size(), equalTo(0));
    }

    @Test
    public void should_return_no_space_when_remote_user_is_not_a_space_member() {

        String REMOTE_USER = "me";

        HashMap<String, Integer> CONFIGURATION = new HashMap<>();
        String ID_1 = "1";
        CONFIGURATION.put(ID_1, 1);
        Space SPACE_1 = new Space();
        SPACE_1.setPrettyName(ID_1);


        given(functionalConfigurationService.loadHighlightConfigAsMap()).willReturn(CONFIGURATION);
        given(spaceService.getSpaceById(ID_1)).willReturn(SPACE_1);
        given(spaceService.isMember(SPACE_1, REMOTE_USER)).willReturn(false);

        List<Space> highlightedSpaces = highlightSpacesService.getUserHighlightedSpaces(REMOTE_USER);

        assertThat(highlightedSpaces.size(), equalTo(0));
    }

    @Test
    public void should_return_sorted_space_by_order_and_name() {

        String REMOTE_USER = "me";

        String DISPLAY_NAME_1 = "DISPLAY_NAME_1";
        String DISPLAY_NAME_2 = "DISPLAY_NAME_2";
        String DISPLAY_NAME_3 = "DISPLAY_NAME_3";
        String DISPLAY_NAME_4 = "DISPLAY_NAME_4";

        String ID_1 = "1";
        String ID_2 = "2";
        String ID_3 = "3";
        String ID_4 = "4";

        HashMap<String, Integer> CONFIGURATION = new HashMap<>();
        CONFIGURATION.put(ID_1, 6);
        CONFIGURATION.put(ID_2, 1);
        CONFIGURATION.put(ID_3, 1);

        Space SPACE_1 = new Space();
        SPACE_1.setId(ID_1);
        SPACE_1.setDisplayName(DISPLAY_NAME_1);
        Space SPACE_2 = new Space();
        SPACE_2.setId(ID_2);
        SPACE_2.setDisplayName(DISPLAY_NAME_2);
        Space SPACE_3 = new Space();
        SPACE_3.setId(ID_3);
        SPACE_3.setDisplayName(DISPLAY_NAME_3);
        Space SPACE_4 = new Space();
        SPACE_4.setId(ID_4);
        SPACE_4.setDisplayName(DISPLAY_NAME_4);

        given(functionalConfigurationService.loadHighlightConfigAsMap()).willReturn(CONFIGURATION);
        given(spaceService.getSpaceById(ID_1)).willReturn(SPACE_1);
        given(spaceService.getSpaceById(ID_2)).willReturn(SPACE_2);
        given(spaceService.getSpaceById(ID_3)).willReturn(SPACE_3);
        when(spaceService.isMember(any(Space.class), anyString())).thenReturn(true);

        List<Space> highlightedSpaces = highlightSpacesService.getUserHighlightedSpaces(REMOTE_USER);

        assertThat(highlightedSpaces.size(), equalTo(3));
        assertThat(highlightedSpaces.get(0), equalTo(SPACE_2));
        assertThat(highlightedSpaces.get(1), equalTo(SPACE_3));
        assertThat(highlightedSpaces.get(2), equalTo(SPACE_1));
    }
}
