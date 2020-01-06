<%@ page import="javax.portlet.PortletPreferences" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<portlet:defineObjects/>

<%
  PortletPreferences preferences = renderRequest.getPreferences();

  String portletName = preferences.getValue("portlet_name", "");
  String portletGroup = preferences.getValue("portlet_group", "");
%>

<span style="margin-left:15px;color:white;font-weight:bold"><%=portletName%></span>

<div id="highlightSpacesView">
    <script>
		require(['SHARED/highlightSpacesViewBundle'], function(highlightSpacesViewBundle) {

		    var preferences = { portletGroup: "<%=portletGroup%>" };

			highlightSpacesViewBundle.init(preferences);
	    });

	</script>
</div>
