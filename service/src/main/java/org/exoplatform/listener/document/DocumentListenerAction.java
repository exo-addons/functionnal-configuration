package org.exoplatform.listener.document;

import org.exoplatform.service.FunctionalConfigurationService;

public class DocumentListenerAction {

  private FunctionalConfigurationService functionalConfigurationService;

  DocumentListenerAction(FunctionalConfigurationService functionalConfigurationService) {
    this.functionalConfigurationService = functionalConfigurationService;
  }

  boolean isDocumentActivityVisible() {
    return !functionalConfigurationService.getDocumentActionActivityConfiguration();
  }
}
