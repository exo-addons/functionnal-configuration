package org.exoplatform.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.rest.response.FunctionalConfigurationResponse;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;

public class FunctionalConfigurationService {

  private final String HIDE_DOCUMENT_ACTION_ACTIVITIES = "HIDE_DOCUMENT_ACTION_ACTIVITIES";

  public static final String HIDE_USER_ACTIVITY_COMPOSER = "hideUserActivityComposer";
  
  public static final String SPACES_WITHOUT_ACTIVITY_COMPOSER = "spacesWithoutActivityComposer";
  
  private static final String SPACES_SEPARATOR = ";";

  private List<Space> listSpacesWithActivityComposer;
  
  private List<Space> listSpacesWithoutActivityComposer;

  private SettingService settingService;
  
  private SpaceService spaceService;

  /**
   * @param settingService
   * @param spaceService
   */
  public FunctionalConfigurationService(SettingService settingService, SpaceService spaceService) {
    this.settingService = settingService;
    this.spaceService = spaceService;
  }

  /**
   * @return the listSpacesWithActivityComposer
   */
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
  
  public String getUserActivityComposerState() throws Exception {
    SettingValue hideUserActivityComposerSetting = settingService.get(Context.GLOBAL, Scope.GLOBAL, HIDE_USER_ACTIVITY_COMPOSER);
    return hideUserActivityComposerSetting != null ? (String) hideUserActivityComposerSetting.getValue() : "false";
  }

  public void hideSpaceActivityComposer(String spaces) {
    SettingValue spacesWithoutActivityComposerSetting = settingService.get(Context.GLOBAL, Scope.GLOBAL, SPACES_WITHOUT_ACTIVITY_COMPOSER);
    String spacesWithoutActivityComposer = spacesWithoutActivityComposerSetting != null ? (String) spacesWithoutActivityComposerSetting.getValue() : "";
    settingService.set(Context.GLOBAL, Scope.GLOBAL, SPACES_WITHOUT_ACTIVITY_COMPOSER, SettingValue.create(spacesWithoutActivityComposer + (spaces.startsWith(",") ? spaces.substring(0).replace(",", SPACES_SEPARATOR) : spaces.replace(",", SPACES_SEPARATOR)) + SPACES_SEPARATOR));
  }
  
  public void showSpaceActivityComposer(String spaces) {
    SettingValue spacesWithoutActivityComposerSetting = settingService.get(Context.GLOBAL, Scope.GLOBAL, SPACES_WITHOUT_ACTIVITY_COMPOSER);
    String spacesWithoutActivityComposer = spacesWithoutActivityComposerSetting != null ? (String) spacesWithoutActivityComposerSetting.getValue() : "";
    settingService.set(Context.GLOBAL, Scope.GLOBAL, SPACES_WITHOUT_ACTIVITY_COMPOSER, SettingValue.create(spacesWithoutActivityComposer.replace((spaces.startsWith(",") ? spaces.substring(0).replace(",", SPACES_SEPARATOR) : spaces.replace(",", SPACES_SEPARATOR)) + SPACES_SEPARATOR, "")));
  }
  
  public void updateUserActivityComposer(boolean hide) {
    settingService.set(Context.GLOBAL, Scope.GLOBAL, HIDE_USER_ACTIVITY_COMPOSER, SettingValue.create(hide));
  }
  
  private void initSpaces() throws Exception {
    ListAccess<Space> allSpacesListAccess = spaceService.getAllSpacesWithListAccess();
    Space[] allSpaces = allSpacesListAccess.load(0, allSpacesListAccess.getSize());
    SettingValue spacesWithoutActivityComposerSetting = settingService.get(Context.GLOBAL, Scope.GLOBAL, SPACES_WITHOUT_ACTIVITY_COMPOSER);
    String spacesWithoutActivityComposer = spacesWithoutActivityComposerSetting != null ? (String) spacesWithoutActivityComposerSetting.getValue() : "";
    List<String> spacesWithoutList= Arrays.asList(spacesWithoutActivityComposer.split(";"));
    listSpacesWithoutActivityComposer = new ArrayList<Space>();
    listSpacesWithActivityComposer = new ArrayList<Space>();
    for (Space space : allSpaces) {
      if (!spacesWithoutActivityComposer.isEmpty() && spacesWithoutList.contains(space.getPrettyName())) {
        listSpacesWithoutActivityComposer.add(space);
      }
      else {
        listSpacesWithActivityComposer.add(space);
      }
    }
  }

  public void updateDocumentActionActivity(boolean hide) {
      settingService.set(Context.GLOBAL, Scope.GLOBAL, HIDE_DOCUMENT_ACTION_ACTIVITIES, SettingValue.create(hide));

  }

  public FunctionalConfigurationResponse getConfiguration() {

    boolean settingValueDocumentAction = getSettingValueAsBoolean(settingService.get(Context.GLOBAL, Scope.GLOBAL, HIDE_DOCUMENT_ACTION_ACTIVITIES));
    boolean settingValueActivityComposer = getSettingValueAsBoolean(settingService.get(Context.GLOBAL, Scope.GLOBAL, HIDE_USER_ACTIVITY_COMPOSER));

    FunctionalConfigurationResponse response = new FunctionalConfigurationResponse();
    response.setHideComposerActivities(settingValueActivityComposer);
    response.setHideDocumentActionActivities(settingValueDocumentAction);

    return response;
  }

  private boolean getSettingValueAsBoolean(SettingValue<?> settingValue) {

    if (Objects.isNull(settingValue)
            || Objects.isNull(settingValue.getValue())
            || StringUtils.isEmpty(settingValue.getValue().toString())) {
      return false;
    }
    return Boolean.valueOf(settingValue.getValue().toString());
  }
}
