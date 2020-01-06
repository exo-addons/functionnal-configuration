package org.exoplatform.rest.response;

import java.io.Serializable;

public class HighlightSpaceConfiguration implements Serializable {

    private boolean highlight;
    private int order;

    private String groupIdentifier;

    public HighlightSpaceConfiguration() {}

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getGroupIdentifier() {
        return groupIdentifier;
    }

    public void setGroupIdentifier(String groupIdentifier) {
        this.groupIdentifier = groupIdentifier;
    }
}
