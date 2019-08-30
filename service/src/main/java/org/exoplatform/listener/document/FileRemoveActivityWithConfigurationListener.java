package org.exoplatform.listener.document;

import javax.jcr.Node;

import org.exoplatform.services.listener.Event;
import org.exoplatform.wcm.ext.component.activity.listener.FileRemoveActivityListener;

public class FileRemoveActivityWithConfigurationListener extends FileRemoveActivityListener {

  private DocumentListenerAction documentListenerAction;

  public FileRemoveActivityWithConfigurationListener(DocumentListenerAction documentListenerAction) {
    this.documentListenerAction= documentListenerAction;
  }

  @Override
  public void onEvent(Event<Object, Node> event) throws Exception {
    if (documentListenerAction.isDocumentActivityVisible()) {
      super.onEvent(event);
    }
  }
}
