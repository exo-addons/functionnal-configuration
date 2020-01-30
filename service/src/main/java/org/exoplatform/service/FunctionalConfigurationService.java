package org.exoplatform.service;

import org.apache.commons.lang3.StringUtils;
import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.rest.response.FunctionalConfiguration;
import org.exoplatform.rest.response.HighlightSpaceConfiguration;
import org.exoplatform.rest.response.SpaceConfiguration;
import org.exoplatform.rest.response.TermsAndConditions;
import org.exoplatform.service.exception.FunctionalConfigurationRuntimeException;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.service.LinkProvider;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.utils.NodeUtils;

import javax.jcr.Node;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

public class FunctionalConfigurationService {

  private static final Log LOGGER = ExoLogger.getLogger(FunctionalConfigurationService.class);

  static final String HIDE_DOCUMENT_ACTION_ACTIVITIES = "HIDE_DOCUMENT_ACTION_ACTIVITIES";

  static final String GROUP_SPACES_SETTINGS = "GROUP_SPACES_SETTINGS";

  public static final String HIDE_USER_ACTIVITY_COMPOSER = "hideUserActivityComposer";

  public static final String SPACES_WITHOUT_ACTIVITY_COMPOSER = "spacesWithoutActivityComposer";

  public static final String HIGHLIGHT_SPACES = "highlightspaces";

  static final String TERMS_AND_CONDITIONS_ACTIVE = "TERMS_AND_CONDITIONS_ACTIVE";
  static final String TERMS_AND_CONDITIONS_WEBCONTENT_URL = "TERMS_AND_CONDITIONS_WEBCONTENT_URL";

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

  protected List<SpaceConfiguration> findSpaceConfigurations() {

    List<SpaceConfiguration> spaceConfigurations = new ArrayList<>();

    Map<String, Integer> highlightConfigurationMap = loadHighlightConfigAsMap();
    Set<String> activityComposerConfiguration = loadActivityComposerConfigurationAsSet();
    Map<String, List<String>> groupSpacesConfigurations = loadGroupSpacesSettingAsMap();

    for (Space space: findAllSpaces()) {

      SpaceConfiguration spaceConfiguration = new SpaceConfiguration();


      boolean hideActivityComposer = activityComposerConfiguration.stream().anyMatch(f -> space.getId().equals(f));
      String spaceUri = LinkProvider.getActivityUriForSpace(space.getPrettyName(), space.getGroupId().replace("/spaces/", ""));

      spaceConfiguration.setSpaceUri(spaceUri);
      spaceConfiguration.setId(space.getId());
      spaceConfiguration.setDisplayName(space.getDisplayName());
      spaceConfiguration.setDescription(space.getDescription());
      spaceConfiguration.setActivityComposerVisible(!hideActivityComposer);

      HighlightSpaceConfiguration highlightConfiguration = new HighlightSpaceConfiguration();
      if (highlightConfigurationMap.containsKey(space.getId())) {
        highlightConfiguration.setHighlight(true);
        highlightConfiguration.setOrder(highlightConfigurationMap.get(space.getId()));
        highlightConfiguration.setGroupIdentifier(findGroupIdentifierForSpace(groupSpacesConfigurations, space.getId()));
      } else {
        highlightConfiguration.setHighlight(false);
      }

      spaceConfiguration.setHighlightConfiguration(highlightConfiguration);
      spaceConfigurations.add(spaceConfiguration);
    }

    return spaceConfigurations;
  }

  public String findGroupIdentifierForSpace(Map<String, List<String>> groupSpacesConfigurations, String spaceId) {
    final String DEFAULT_GROUP_ID = "";

    for (Map.Entry<String, List<String>> entry: groupSpacesConfigurations.entrySet()) {

      String groupId = entry.getKey();
      List<String> spacesIds = entry.getValue();

      if (spacesIds.contains(spaceId)) {
        return groupId;
      }
    }

    return DEFAULT_GROUP_ID;
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

    configuration.setTermsAndConditions(getTermsAndConditions());

    return configuration;
  }

  public TermsAndConditions getTermsAndConditions() {
    TermsAndConditions termsAndConditions = new TermsAndConditions();
    boolean isTermsAndConditionsActive = isTermsAndConditionsActive();
    if (isTermsAndConditionsActive) {
      termsAndConditions.setActive(isTermsAndConditionsActive);
      termsAndConditions.setWebContentUrl(loadSettingsAsString(TERMS_AND_CONDITIONS_WEBCONTENT_URL));
    }

    return termsAndConditions;
  }

  public boolean isDocumentActionActivityHidden() {
    LOGGER.debug("Get value for DocumentActionActivityHidden : "+ getSettingValueAsBoolean(settingService.get(Context.GLOBAL, Scope.GLOBAL, HIDE_DOCUMENT_ACTION_ACTIVITIES)));
    return getSettingValueAsBoolean(settingService.get(Context.GLOBAL, Scope.GLOBAL, HIDE_DOCUMENT_ACTION_ACTIVITIES));
  }

  private boolean isActivityComposerHidden() {
    return getSettingValueAsBoolean(settingService.get(Context.GLOBAL, Scope.GLOBAL, HIDE_USER_ACTIVITY_COMPOSER));
  }

  public boolean isTermsAndConditionsActive() {
    return getSettingValueAsBoolean(settingService.get(Context.GLOBAL, Scope.GLOBAL, TERMS_AND_CONDITIONS_ACTIVE));
  }

  public boolean getSettingValueAsBoolean(SettingValue<?> settingValue) {

    if (Objects.isNull(settingValue)
            || Objects.isNull(settingValue.getValue())
            || StringUtils.isEmpty(settingValue.getValue().toString())) {
      return false;
    }
    return Boolean.parseBoolean(settingValue.getValue().toString());
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

    HighlightSpaceConfiguration highlightConfiguration = spaceConfiguration.getHighlightConfiguration();
    if (nonNull(highlightConfiguration) && highlightConfiguration.isHighlight()) {
      addSpaceToGroup(space.getId(), highlightConfiguration.getGroupIdentifier());
    }

    return spaceConfiguration;
  }

  void addSpaceToGroup(String spaceId, String groupIdentifier) {

    Map<String, List<String>> groupSpacesConfigurationMap = loadGroupSpacesSettingAsMap();


    for (Map.Entry<String, List<String>> entry: groupSpacesConfigurationMap.entrySet()) {

      List<String> spacesIds = entry.getValue();
      spacesIds.remove(spaceId);
    }

    if (Objects.nonNull(groupIdentifier)) {

      if (!groupSpacesConfigurationMap.containsKey(groupIdentifier)) {
        groupSpacesConfigurationMap.put(groupIdentifier, new ArrayList<>());
      }
      groupSpacesConfigurationMap.get(groupIdentifier).add(spaceId);
    }


    saveGroupSpacesSetting(groupSpacesConfigurationMap);
  }

  private void saveGroupSpacesSetting(Map<String, List<String>> groupSpacesConfigurationMap) {
    String groupSpaceSettings = convertGroupSpaceSettingMapToStringConfiguration(groupSpacesConfigurationMap);
    settingService.set(Context.GLOBAL, Scope.GLOBAL, GROUP_SPACES_SETTINGS, SettingValue.create(groupSpaceSettings));
  }

  private String convertGroupSpaceSettingMapToStringConfiguration(Map<String, List<String>> groupSpacesConfigurationMap) {

    List<String> groupSettingsConfiguration = new ArrayList<>();

    for (Map.Entry<String, List<String>> entry: groupSpacesConfigurationMap.entrySet()) {
      String groupId = entry.getKey();
      List<String> spacesId = entry.getValue();

      StringJoiner groupSpaceJoiner = new StringJoiner(HIGHLIGHT_SPACES_SEPARATOR);

      groupSpaceJoiner.add(groupId);
      spacesId.forEach(groupSpaceJoiner::add);

      groupSettingsConfiguration.add(groupSpaceJoiner.toString());
    }

    StringJoiner groupJoiner = new StringJoiner(SETTINGS_SEPARATOR);
    groupSettingsConfiguration.forEach(groupJoiner::add);
    return groupJoiner.toString();
  }

  public Map<String, List<String>> loadGroupSpacesSettingAsMap() {

    final int GROUP_CONFIGURATION_POSITION = 0;

    Map<String, List<String>> groupSpacesConfigurationMap = new LinkedHashMap<>(); // Linked because we have to keep order map insertion

    String groupSpacesSettingsValue = loadSettingsAsString(GROUP_SPACES_SETTINGS);

    if (StringUtils.isEmpty(groupSpacesSettingsValue)) {
      return groupSpacesConfigurationMap;
    }

    String[] groupConfigurations = groupSpacesSettingsValue.split(SETTINGS_SEPARATOR);
    for (String groupConfiguration: groupConfigurations) {

      String[] arrayConfiguration = groupConfiguration.split(HIGHLIGHT_SPACES_SEPARATOR);

      if (arrayConfiguration.length == 1) {
        String groupId = arrayConfiguration[GROUP_CONFIGURATION_POSITION];
        groupSpacesConfigurationMap.put(groupId, new ArrayList<>());
      } else if (arrayConfiguration.length > 1) {
        String groupId = arrayConfiguration[GROUP_CONFIGURATION_POSITION];
        List<String> spacesId = Arrays.asList(arrayConfiguration).subList(1, arrayConfiguration.length);
        groupSpacesConfigurationMap.put(groupId, new ArrayList<>(spacesId));
      }
    }

    return groupSpacesConfigurationMap;
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

  private String loadSettingsAsString(String settingKey) {
    SettingValue settingValue = settingService.get(Context.GLOBAL, Scope.GLOBAL, settingKey);

    return nonNull(settingValue)
            ? (String) settingValue.getValue()
            : "";
  }

  public void updateTermsAndConditions(TermsAndConditions termsAndConditions) {

    boolean isActive = termsAndConditions.isActive();
    settingService.set(Context.GLOBAL, Scope.GLOBAL, TERMS_AND_CONDITIONS_ACTIVE, SettingValue.create(isActive));

    if (isActive && Objects.nonNull(NodeUtils.findCollaborationFile(termsAndConditions.getWebContentUrl()))) {
      settingService.set(Context.GLOBAL, Scope.GLOBAL, TERMS_AND_CONDITIONS_WEBCONTENT_URL, SettingValue.create(termsAndConditions.getWebContentUrl()));
    } else {
      settingService.set(Context.GLOBAL, Scope.GLOBAL, TERMS_AND_CONDITIONS_WEBCONTENT_URL, SettingValue.create(""));
    }
  }

  public List<SpaceConfiguration> getSpacesForGroup(String groupIdentifier) {

    final String LEGACY_IDENTIFIER = "0"; // For compatibility we are using identifier 0
    List<SpaceConfiguration> spaceConfigurations = findSpaceConfigurations();

    if (LEGACY_IDENTIFIER.equals(groupIdentifier)) {
      return spaceConfigurations.stream()
              .filter(space -> Objects.nonNull(space.getHighlightConfiguration()))
              .filter(space -> space.getHighlightConfiguration().isHighlight() && StringUtils.isEmpty(space.getHighlightConfiguration().getGroupIdentifier()))
              .collect(Collectors.toList());
    }

    return spaceConfigurations.stream()
            .filter(space -> Objects.nonNull(space.getHighlightConfiguration()) && space.getHighlightConfiguration().isHighlight())
            .filter(space -> groupIdentifier.equals(space.getHighlightConfiguration().getGroupIdentifier()))
            .collect(Collectors.toList());
  }
}
