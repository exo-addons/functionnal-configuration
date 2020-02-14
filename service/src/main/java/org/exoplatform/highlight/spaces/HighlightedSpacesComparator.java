package org.exoplatform.highlight.spaces;

import java.util.Comparator;

class HighlightSpacesComparator implements Comparator<HighlightSpace> {

  public int compare(HighlightSpace highlightSpace1, HighlightSpace highlightSpace2) {

    int orderCompare = highlightSpace1.getOrder().compareTo(highlightSpace2.getOrder());

    if (orderCompare != 0) {
      return orderCompare;
    } else {
      return highlightSpace1.getDisplayName().compareToIgnoreCase(highlightSpace2.getDisplayName());
    }
  }
}
