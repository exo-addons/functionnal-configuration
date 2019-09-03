package org.exoplatform.highlight.spaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.exoplatform.service.FunctionalConfigurationService;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;

public class HighlightSpacesService {

  private SpaceService spaceService;
  private FunctionalConfigurationService functionalConfigurationService;

  public HighlightSpacesService(SpaceService spaceService, FunctionalConfigurationService functionalConfigurationService) {
    this.functionalConfigurationService = functionalConfigurationService;
    this.spaceService = spaceService;
  }

  public List<Space> getUserHighlightedSpaces(String remoteUser) {

    Map<String, Integer> configurations = functionalConfigurationService.loadHighlightConfigAsMap();

    List<HighlightSpace> highlightSpaces = new ArrayList<>();

    for (Map.Entry<String, Integer> entry : configurations.entrySet()) {
      String spaceId = entry.getKey();
      Integer order = entry.getValue();

      Space space = spaceService.getSpaceById(spaceId);

      if (space != null && spaceService.isMember(space, remoteUser)) {

        HighlightSpace highlightSpace = new HighlightSpace();
        highlightSpace.setOrder(order);
        highlightSpace.setSpace(space);

        highlightSpaces.add(highlightSpace);
      }
    }

    highlightSpaces.sort(new HighlightSpacesComparator());

    return highlightSpaces.stream().map(HighlightSpace::getSpace).collect(Collectors.toList());
  }
}
