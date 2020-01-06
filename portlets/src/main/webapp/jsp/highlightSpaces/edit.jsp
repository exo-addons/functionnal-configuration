<%@ page import="javax.portlet.PortletURL" %>
<%@ page import="javax.portlet.PortletPreferences" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<portlet:defineObjects/>

<%
  PortletURL actionURL = renderResponse.createActionURL();

  PortletPreferences preferences = renderRequest.getPreferences();
  String portletName = preferences.getValue("portlet_name", "");
  String portletGroup = preferences.getValue("portlet_group", "");
%>

<div id="highlightSpacesEdit">
	<script>
		require(['SHARED/highlightSpacesEditBundle'], function(highlightSpacesEditBundle) {

		    var preferences = { actionURL: "<%=actionURL%>", portletName: "<%=portletName%>", portletGroup: "<%=portletGroup%>" };

			highlightSpacesEditBundle.init(preferences);
	    });

	</script>
</div>