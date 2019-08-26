package org.exoplatform.portlets.functionalConfiguration;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;

public class FunctionalConfigurationPortlet extends GenericPortlet {
  @Override
  protected void doView(RenderRequest request, RenderResponse response) throws IOException, PortletException {
    PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher("/jsp/functionalConfiguration/view.jsp");
    dispatcher.forward(request, response);
  }

}
