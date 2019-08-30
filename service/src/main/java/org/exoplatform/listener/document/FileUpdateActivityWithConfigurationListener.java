package org.exoplatform.listener.document;

import org.apache.commons.chain.Context;

import org.exoplatform.services.listener.Event;
import org.exoplatform.wcm.ext.component.activity.listener.FileUpdateActivityListener;

public class FileUpdateActivityWithConfigurationListener extends FileUpdateActivityListener {

  private DocumentListenerAction documentListenerAction;

  public FileUpdateActivityWithConfigurationListener(DocumentListenerAction documentListenerAction) {
    this.documentListenerAction= documentListenerAction;
  }

  @Override
  public void onEvent(Event<Context, String> event) throws Exception {
    if (documentListenerAction.isDocumentActivityVisible()) {
      super.onEvent(event);
    }
  }
}
