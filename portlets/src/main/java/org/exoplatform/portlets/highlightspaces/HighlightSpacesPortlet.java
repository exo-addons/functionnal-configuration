package org.exoplatform.portlets.highlightspaces;

import javax.portlet.*;
import java.io.IOException;

public class HighlightSpacesPortlet extends GenericPortlet {

    @Override
    protected void doView(RenderRequest request, RenderResponse response) throws IOException, PortletException {

        PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher("/jsp/highlightSpaces/view.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doEdit(RenderRequest request, RenderResponse response) throws IOException, PortletException {

        PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher("/jsp/highlightSpaces/edit.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    public void processAction(ActionRequest request, ActionResponse response) throws IOException, PortletException {
        getPortletContext()
        PortletPreferences preferences = request.getPreferences();
        preferences.setValue("portlet_name", request.getParameter("portlet_name"));
        preferences.setValue("portlet_group", request.getParameter("portlet_group"));
        preferences.store();

        response.setPortletMode(PortletMode.VIEW);
    }

}
