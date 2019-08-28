package org.exoplatform.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;

public class FunctionalConfigurationService {
  
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
  
  public void configureUserActivityComposer(String hideUserActivityComposer) {
    settingService.set(Context.GLOBAL, Scope.GLOBAL, HIDE_USER_ACTIVITY_COMPOSER, SettingValue.create(hideUserActivityComposer));
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
}
