<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="java.util.List" %>
<%@ page import="org.exoplatform.highlight.spaces.HighlightSpace" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<portlet:defineObjects/>

<%
  PortletPreferences preferences = renderRequest.getPreferences();

  String portletName = preferences.getValue("portlet_name", "");

  List<HighlightSpace> spaces = (List<HighlightSpace>) renderRequest.getAttribute("spaces");
%>

<div id="<portlet:namespace/>">
  <div class="highlightedSpacesGroup">
    <span class="portletName"><%=portletName%></span>
    <ul>
      <% for(HighlightSpace space : spaces) { %>
        <li id="highlightedspace_<%=space.getId()%>" class="portletSpace">
          <a class="portletSpaceLink" href="<%=space.getUri()%>"><%=space.getDisplayName()%></a>
        </li>
      <% } %>
    </ul>
  </div>
</div>

<script>
  (function() {
    let spacesItems = document.querySelectorAll('#<portlet:namespace/> li');
    for(spaceItem of spacesItems) {
      if(spaceItem.id.substring('highlightedspace_'.length) == eXo.env.portal.spaceId) {
        spaceItem.classList.add("spaceItemSelected");
      }
    }

  })();
</script>

<style>
  .highlightedSpacesGroup .portletName {
      color: #ffffff;
      padding-left: 15px;
      font-weight: bold;
  }

  .highlightedSpacesGroup .portletSpace {
      color: #ffffff;
      padding-left: 15px;
  }

  .highlightedSpacesGroup .portletSpaceLink {
      color: #ffffff;
      margin-left: 15px;
  }
</style>
