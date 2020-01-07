<%@ page import="javax.portlet.PortletPreferences" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<portlet:defineObjects/>

<%
  PortletPreferences preferences = renderRequest.getPreferences();

  String portletName = preferences.getValue("portlet_name", "");
  String portletGroup = preferences.getValue("portlet_group", "");

%>
<div id="<portlet:namespace/>"></div>

<script>
	require(['SHARED/highlightSpacesViewBundle'], function(highlightSpacesViewBundle) {

		var preferences = { portletName: "<%=portletName%>", portletGroup: "<%=portletGroup%>", idPortlet: '<portlet:namespace/>' };

		highlightSpacesViewBundle.init(preferences);
	});
</script>
