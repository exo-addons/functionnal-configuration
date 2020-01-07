package org.exoplatform.service;

import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.test.matchers.SettingValueMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.exoplatform.service.FunctionalConfigurationService.GROUP_SPACES_SETTINGS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GroupSpacesFunctionalConfigurationServiceTest {

    @Mock
    private SettingService settingService;

    @Mock
    private SpaceService spaceService;

    private final String GROUP_SPACE_CONFIGURATION = "group1#space1#space2;group2#space6";

    private FunctionalConfigurationService functionalConfigurationService;

    @Before
    public void setUp() {
        functionalConfigurationService = new FunctionalConfigurationService(settingService, spaceService);

        given(settingService.get(Context.GLOBAL, Scope.GLOBAL, GROUP_SPACES_SETTINGS)).willReturn((SettingValue) SettingValue.create(GROUP_SPACE_CONFIGURATION));
        doNothing().when(settingService).set(any(), any(), any(), any());
    }

    @Test
    public void should_update_space6_to_group_1() {


        functionalConfigurationService.addSpaceToGroup("space6", "group1");

        String expectedSettings = "group1#space1#space2#space6;group2";
        verify(settingService).set(eq(Context.GLOBAL), eq(Scope.GLOBAL), eq(GROUP_SPACES_SETTINGS), argThat(new SettingValueMatcher(SettingValue.create(expectedSettings))));
    }

    @Test
    public void should_create_space3_to_group_1() {

        functionalConfigurationService.addSpaceToGroup("space3", "group1");

        String expectedSettings = "group1#space1#space2#space3;group2#space6";
        verify(settingService).set(eq(Context.GLOBAL), eq(Scope.GLOBAL), eq(GROUP_SPACES_SETTINGS), argThat(new SettingValueMatcher(SettingValue.create(expectedSettings))));
    }

    @Test
    public void should_remove_space1_from_group_1() {

        functionalConfigurationService.addSpaceToGroup("space1", null);

        String expectedSettings = "group1#space2;group2#space6";
        verify(settingService).set(eq(Context.GLOBAL), eq(Scope.GLOBAL), eq(GROUP_SPACES_SETTINGS), argThat(new SettingValueMatcher(SettingValue.create(expectedSettings))));
    }

    private static final LinkedHashMap<String, List<String>> GROUP_SPACES_CONFIGURATIONS = new LinkedHashMap<>();
    static {
        GROUP_SPACES_CONFIGURATIONS.put("group1", Collections.singletonList("space1"));
        GROUP_SPACES_CONFIGURATIONS.put("group2", asList("space4", "space6", "space8"));
        GROUP_SPACES_CONFIGURATIONS.put("group3", new ArrayList<>());
    }

    @Test
    public void should_find_group2_for_space4() {

        String groupId = functionalConfigurationService.findGroupIdentifierForSpace(GROUP_SPACES_CONFIGURATIONS, "space4");

        assertThat(groupId, equalTo("group2"));
    }

    @Test
    public void should_find_default_group_when_space_not_referenced_in_configuration() {

        String groupId = functionalConfigurationService.findGroupIdentifierForSpace(GROUP_SPACES_CONFIGURATIONS, "toto");

        assertThat(groupId, equalTo("0"));
    }
}
