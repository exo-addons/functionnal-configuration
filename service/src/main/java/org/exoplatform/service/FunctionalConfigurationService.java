package org.exoplatform.service;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.rest.response.FunctionalConfiguration;
import org.exoplatform.rest.response.HighlightSpaceConfiguration;
import org.exoplatform.rest.response.SpaceConfiguration;
import org.exoplatform.service.exception.FunctionalConfigurationRuntimeException;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

public class FunctionalConfigurationService {

  private static final Log LOGGER = ExoLogger.getLogger(FunctionalConfigurationService.class);

  static final String HIDE_DOCUMENT_ACTION_ACTIVITIES = "HIDE_DOCUMENT_ACTION_ACTIVITIES";

  public static final String HIDE_USER_ACTIVITY_COMPOSER = "hideUserActivityComposer";

  public static final String SPACES_WITHOUT_ACTIVITY_COMPOSER = "spacesWithoutActivityComposer";

  public static final String HIGHLIGHT_SPACES = "highlightspaces";

  // SEPARATOR between space_id and highlight_space_order : ID#ORDER
  public static final String HIGHLIGHT_SPACES_SEPARATOR = "#";

  private static final String SETTINGS_SEPARATOR = ";";


  private SettingService settingService;

  private List<Space> listSpacesWithActivityComposer;

  private List<Space> listSpacesWithoutActivityComposer;
  private SpaceService spaceService;

  public FunctionalConfigurationService(SettingService settingService, SpaceService spaceService) {
    this.settingService = settingService;
    this.spaceService = spaceService;
  }

  public List<Space> getListSpacesWithActivityComposer() throws Exception {
    initSpaces();
    return listSpacesWithActivityComposer;
  }

  /**
   * @return the listSpacesWithoutActivityComposer
   */
  public List<Space> getListSpacesWithoutActivityComposer() throws Exception {
    initSpaces();
    return listSpacesWithoutActivityComposer;
  }

  public String getUserActivityComposerState() {
    SettingValue hideUserActivityComposerSetting = settingService.get(Context.GLOBAL, Scope.GLOBAL, HIDE_USER_ACTIVITY_COMPOSER);
    return hideUserActivityComposerSetting != null ? (String) hideUserActivityComposerSetting.getValue() : "false";
  }

  public void hideSpaceActivityComposer(String spaces) {
    String spacesWithoutActivityComposer = loadSettingsAsString(SPACES_WITHOUT_ACTIVITY_COMPOSER);
    settingService.set(Context.GLOBAL, Scope.GLOBAL, SPACES_WITHOUT_ACTIVITY_COMPOSER, SettingValue.create(spacesWithoutActivityComposer + (spaces.startsWith(",") ? spaces.substring(0).replace(",", SETTINGS_SEPARATOR) : spaces.replace(",", SETTINGS_SEPARATOR)) + SETTINGS_SEPARATOR));
  }

  public void showSpaceActivityComposer(String spaces) {
    String spacesWithoutActivityComposer = loadSettingsAsString(SPACES_WITHOUT_ACTIVITY_COMPOSER);
    settingService.set(Context.GLOBAL, Scope.GLOBAL, SPACES_WITHOUT_ACTIVITY_COMPOSER, SettingValue.create(spacesWithoutActivityComposer.replace((spaces.startsWith(",") ? spaces.substring(0).replace(",", SETTINGS_SEPARATOR) : spaces.replace(",", SETTINGS_SEPARATOR)) + SETTINGS_SEPARATOR, "")));
  }

  private void initSpaces() throws Exception {
    ListAccess<Space> allSpacesListAccess = spaceService.getAllSpacesWithListAccess();
    Space[] allSpaces = allSpacesListAccess.load(0, allSpacesListAccess.getSize());
    String spacesWithoutActivityComposer = loadSettingsAsString(SPACES_WITHOUT_ACTIVITY_COMPOSER);
    List<String> spacesWithoutList = asList(spacesWithoutActivityComposer.split(";"));


    listSpacesWithoutActivityComposer = new ArrayList<>();
    listSpacesWithActivityComposer = new ArrayList<>();


    for (Space space : allSpaces) {
      if (!spacesWithoutActivityComposer.isEmpty() && spacesWithoutList.contains(space.getId())) {
        listSpacesWithoutActivityComposer.add(space);
      }
      else {
        listSpacesWithActivityComposer.add(space);
      }
    }
  }

  private List<SpaceConfiguration> findSpaceConfigurations() {

    List<SpaceConfiguration> spaceConfigurations = new ArrayList<>();

    for (Space space: findAllSpaces()) {

      SpaceConfiguration spaceConfiguration = new SpaceConfiguration();

      Map<String, Integer> highlightConfigurationMap = loadHighlightConfigAsMap();
      Set<String> activityComposerConfiguration = loadActivityComposerConfigurationAsSet();

      boolean hideActivityComposer = activityComposerConfiguration.stream().anyMatch(f -> space.getId().equals(f));

      spaceConfiguration.setId(space.getId());
      spaceConfiguration.setDisplayName(space.getDisplayName());
      spaceConfiguration.setDescription(space.getDescription());
      spaceConfiguration.setActivityComposerVisible(!hideActivityComposer);

      HighlightSpaceConfiguration highlightConfiguration = new HighlightSpaceConfiguration();
      if (highlightConfigurationMap.containsKey(space.getId())) {
        highlightConfiguration.setHighlight(true);
        highlightConfiguration.setOrder(highlightConfigurationMap.get(space.getId()));
      } else {
        highlightConfiguration.setHighlight(false);
      }

      spaceConfiguration.setHighlightConfiguration(highlightConfiguration);
      spaceConfigurations.add(spaceConfiguration);
    }

    return spaceConfigurations;
  }

  protected List<Space> findAllSpaces() {

    try {
      ListAccess<Space> allSpacesListAccess = spaceService.getAllSpacesWithListAccess();
      return asList(allSpacesListAccess.load(0, allSpacesListAccess.getSize()));
    } catch (Exception e) {
      throw new FunctionalConfigurationRuntimeException("space.error.loading");
    }
  }


  /**
   * configuration of show/hide document action in space on activity
   * @param hide
   */
  public void configureActivityComposer(String hide) {
      settingService.set(Context.GLOBAL, Scope.GLOBAL, HIDE_USER_ACTIVITY_COMPOSER, SettingValue.create(hide));
  }


  public void configureDocumentActionActivities(String hidden) {
    settingService.set(Context.GLOBAL, Scope.GLOBAL, HIDE_DOCUMENT_ACTION_ACTIVITIES, SettingValue.create(hidden));
  }

  public FunctionalConfiguration getConfiguration() {

    FunctionalConfiguration configuration = new FunctionalConfiguration();

    configuration.setHideComposerActivities(isActivityComposerHidden());
    configuration.setHideDocumentActionActivities(isDocumentActionActivityHidden());

    configuration.setSpaceConfigurations(findSpaceConfigurations());

    return configuration;
  }

  public boolean isDocumentActionActivityHidden() {
    return getSettingValueAsBoolean(settingService.get(Context.GLOBAL, Scope.GLOBAL, HIDE_DOCUMENT_ACTION_ACTIVITIES));
  }

  private boolean isActivityComposerHidden() {
    return getSettingValueAsBoolean(settingService.get(Context.GLOBAL, Scope.GLOBAL, HIDE_USER_ACTIVITY_COMPOSER));
  }

  private boolean getSettingValueAsBoolean(SettingValue<?> settingValue) {

    if (Objects.isNull(settingValue)
            || Objects.isNull(settingValue.getValue())
            || StringUtils.isEmpty(settingValue.getValue().toString())) {
      return false;
    }
    return Boolean.valueOf(settingValue.getValue().toString());
  }

  public SpaceConfiguration updateSpaceConfiguration(SpaceConfiguration spaceConfiguration) {

    Space space = spaceService.getSpaceById(spaceConfiguration.getId());

    if (Objects.isNull(space)) {
      LOGGER.error("Space with id : " + spaceConfiguration.getId() + " NOT FOUND");
      throw new FunctionalConfigurationRuntimeException("space.notfound");
    }

    // load highlight configuration as map from setting service
    Map<String, Integer> highlightConfigurationsMap = loadHighlightConfigAsMap();
    // update and save highlight configuration
    updateAndSaveHighlightConfigurationMap(space, spaceConfiguration.getHighlightConfiguration(), highlightConfigurationsMap);


    // load activity composer configuration as set from setting service
    Set<String> activityComposerConfigurations = loadActivityComposerConfigurationAsSet();
    // update and save activity composer configuration
    updateAndSaveActivityComposerConfigurationSet(spaceConfiguration, space, activityComposerConfigurations);

    return spaceConfiguration;
  }

  private void updateAndSaveActivityComposerConfigurationSet(SpaceConfiguration spaceConfiguration, Space space, Set<String> activityComposerConfigurations) {

    String spaceId = space.getId();

    if (!spaceConfiguration.isActivityComposerVisible()) {
      activityComposerConfigurations.add(spaceId);
    }  else {
      activityComposerConfigurations = activityComposerConfigurations.stream()
              .filter(configuration -> !StringUtils.equals(spaceId, configuration))
              .collect(Collectors.toSet());
    }

    parseAndSaveSettings(SPACES_WITHOUT_ACTIVITY_COMPOSER, new ArrayList<>(activityComposerConfigurations));
  }

  private void updateAndSaveHighlightConfigurationMap(Space space, HighlightSpaceConfiguration highlightConfiguration, Map<String, Integer> highlightConfigurations) {

    String spaceId = space.getId();

    if (highlightConfiguration.isHighlight()) {

      if (!highlightConfigurations.containsKey(spaceId)) {
        highlightConfigurations.put(spaceId, null);
      }

      highlightConfigurations.put(spaceId, highlightConfiguration.getOrder());

    } else {
      highlightConfigurations.remove(spaceId);
    }

    saveHighlightConfiguration(highlightConfigurations);
  }

  private void saveHighlightConfiguration(Map<String, Integer> highlightConfigurationsAsMap) {

    List<String> highlightConfigurationsAsList = highlightConfigurationsAsMap.entrySet().stream()
            .map(entry -> entry.getKey() + HIGHLIGHT_SPACES_SEPARATOR + entry.getValue())
            .collect(Collectors.toList());

    parseAndSaveSettings(HIGHLIGHT_SPACES, highlightConfigurationsAsList);
  }

  private void parseAndSaveSettings(String settingKey, List<String> configurations) {

    String settingsAsString = configurations.stream()
            .collect(Collectors.joining(FunctionalConfigurationService.SETTINGS_SEPARATOR, "", ""));

    settingService.set(Context.GLOBAL, Scope.GLOBAL, settingKey, SettingValue.create(settingsAsString));
  }

  private Set<String> loadActivityComposerConfigurationAsSet() {

    return new HashSet<>(
            loadAndParseSettings(SPACES_WITHOUT_ACTIVITY_COMPOSER));
  }

  public Map<String, Integer> loadHighlightConfigAsMap() {

    List<String> settings = loadAndParseSettings(HIGHLIGHT_SPACES);
    return convertHighlightSpacesConfigurationsFromListToMap(settings);
  }

  private Map<String, Integer> convertHighlightSpacesConfigurationsFromListToMap(List<String> highlightSpacesConfigurationsAsList) {

    final int HIGHLIGHT_SPACES_CONFIGURATION_LENGTH = 2;

    Map<String, Integer> configurationAsMap = new HashMap<>();

    for (String configuration: highlightSpacesConfigurationsAsList) {

      // Split configuration KEY#VALUE
      String[] split = configuration.split(HIGHLIGHT_SPACES_SEPARATOR);

      // After split, array should have a length of 2
      if (HIGHLIGHT_SPACES_CONFIGURATION_LENGTH == split.length) {
        configurationAsMap.put(split[0], Integer.valueOf(split[1]));
      }
    }
    return configurationAsMap;
  }

  private List<String> loadAndParseSettings(String settingKey) {

    String settingsAsString = loadSettingsAsString(settingKey);

    return (StringUtils.isNotEmpty(settingsAsString))
            ? asList(settingsAsString.split(SETTINGS_SEPARATOR))
            : new ArrayList<>();
  }

  private String loadSettingsAsString(String highlightSpaces) {
    SettingValue settingValue = settingService.get(Context.GLOBAL, Scope.GLOBAL, highlightSpaces);

    return nonNull(settingValue)
            ? (String) settingValue.getValue()
            : "";
  }
}
