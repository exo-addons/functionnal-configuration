package org.exoplatform.portlets.highlightspaces;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.highlight.spaces.HighlightSpace;
import org.exoplatform.highlight.spaces.HighlightSpacesService;
import org.gatein.api.PortalRequest;

import javax.portlet.*;
import java.io.IOException;
import java.util.List;

public class HighlightSpacesPortlet extends GenericPortlet {

    @Override
    protected void doView(RenderRequest request, RenderResponse response) throws IOException, PortletException {
        PortletPreferences preferences = request.getPreferences();
        String portletGroup = preferences.getValue("portlet_group", "");

        HighlightSpacesService highlightSpacesService = ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(HighlightSpacesService.class);
        List<HighlightSpace> highlightSpaces = highlightSpacesService.getHighlightedSpacesForUser(portletGroup, request.getRemoteUser());
        request.setAttribute("spaces", highlightSpaces);

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
        PortletPreferences preferences = request.getPreferences();
        preferences.setValue("portlet_name", request.getParameter("portlet_name"));
        preferences.setValue("portlet_group", request.getParameter("portlet_group"));
        preferences.store();

        response.setPortletMode(PortletMode.VIEW);
    }

}
