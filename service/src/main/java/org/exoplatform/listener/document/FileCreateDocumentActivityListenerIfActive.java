package org.exoplatform.listener.document;

import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.services.listener.Event;
import org.exoplatform.wcm.ext.component.activity.listener.FileCreateActivityListener;
import org.exoplatform.wcm.ext.component.activity.listener.Utils;

import javax.jcr.Node;

import static java.util.Objects.nonNull;

public class FileCreateDocumentActivityListenerIfActive extends FileCreateActivityListener {
  private boolean             isListeningCreateDocument                    = true;

  private static final String RESOURCE_BUNDLE_KEY_CREATED_BY               = "SocialIntegration.messages.createdBy";

  private final String        DOES_DOCUMENT_EDITION_SHOULD_CREATE_ACTIVITY = "DOES_DOCUMENT_EDITION_SHOULD_CREATE_ACTIVITY";

  private SettingService      settingService;

  /**
   * Instantiates a new post create content event listener.
   */
  public FileCreateDocumentActivityListenerIfActive(SettingService settingService) {
    this.settingService = settingService;
  }

  private boolean getIsListening() {
    SettingValue isListening = this.settingService.get(Context.GLOBAL, Scope.GLOBAL, DOES_DOCUMENT_EDITION_SHOULD_CREATE_ACTIVITY);

    if (nonNull(isListening)) {
      return (boolean) isListening.getValue();
    }
    return true;
  }

  @Override
  public void onEvent(Event<Object, Node> event) throws Exception {
    if (getIsListening()) {
      Node currentNode = event.getData();
      Utils.postFileActivity(currentNode, RESOURCE_BUNDLE_KEY_CREATED_BY, true, false, "", "");
    }
  }

}
