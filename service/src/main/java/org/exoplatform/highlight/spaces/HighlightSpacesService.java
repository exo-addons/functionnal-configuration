package org.exoplatform.highlight.spaces;

import org.exoplatform.rest.response.SpaceConfiguration;
import org.exoplatform.service.FunctionalConfigurationService;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;

import java.util.ArrayList;
import java.util.List;

public class HighlightSpacesService {

  public static final String SPACES_GROUP_LEGACY_ID = "0"; // For compatibility we are using identifier 0

  private SpaceService spaceService;
  private FunctionalConfigurationService functionalConfigurationService;

  public HighlightSpacesService(SpaceService spaceService, FunctionalConfigurationService functionalConfigurationService) {
    this.functionalConfigurationService = functionalConfigurationService;
    this.spaceService = spaceService;
  }

  /**
   * Get highlighted spaces for the legacy space group and the given user
   * @param userId User id
   * @return The list of highlighted spaces for the given group and user
   */
  public List<HighlightSpace> getHighlightedSpacesForUser(String userId) {
    return this.getHighlightedSpacesForUser(SPACES_GROUP_LEGACY_ID, userId);
  }

  /**
   * Get highlighted spaces for the given space group and the given user
   * @param spacesGroupId Spaces group id
   * @param userId User id
   * @return The list of highlighted spaces for the given group and user
   */
  public List<HighlightSpace> getHighlightedSpacesForUser(String spacesGroupId, String userId) {

    List<SpaceConfiguration> spaceConfigurationsForGroup = functionalConfigurationService.getSpacesForGroup(spacesGroupId);

    List<HighlightSpace> highlightSpaces = new ArrayList<>();

    for (SpaceConfiguration spaceConfiguration : spaceConfigurationsForGroup) {
      Space space = spaceService.getSpaceById(spaceConfiguration.getId());

      if (space != null && spaceService.isMember(space, userId)) {

        HighlightSpace highlightSpace = new HighlightSpace();
        highlightSpace.setOrder(spaceConfiguration.getHighlightConfiguration().getOrder());
        highlightSpace.setId(space.getId());
        highlightSpace.setDisplayName(space.getDisplayName());
        highlightSpace.setAvatarUri(space.getAvatarUrl());
        highlightSpace.setUri(spaceConfiguration.getSpaceUri());
        highlightSpace.setGroupIdentifier(spaceConfiguration.getHighlightConfiguration().getGroupIdentifier());

        highlightSpaces.add(highlightSpace);
      }
    }

    highlightSpaces.sort(new HighlightSpacesComparator());

    return highlightSpaces;
  }
}
