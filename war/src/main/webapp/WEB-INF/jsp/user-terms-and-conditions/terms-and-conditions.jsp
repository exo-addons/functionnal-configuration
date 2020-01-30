<%@ page import="java.net.URLEncoder" %>
<%@ page import="javax.servlet.http.Cookie" %>
<%@ page import="org.exoplatform.container.PortalContainer" %>
<%@ page import="org.exoplatform.services.resources.ResourceBundleService" %>
<%@ page import="org.exoplatform.portal.config.UserPortalConfigService" %>
<%@ page import="org.exoplatform.portal.resource.SkinService"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="org.gatein.common.text.EntityEncoder" %>
<%@ page language="java" %>
<%@ page import="java.util.ResourceBundle"%>

<%
    String contextPath = request.getContextPath();
    String lang = request.getLocale().getLanguage();
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");


    PortalContainer portalContainer = PortalContainer.getCurrentInstance(session.getServletContext());
    UserPortalConfigService userPortalConfigService = portalContainer.getComponentInstanceOfType(UserPortalConfigService.class);
    String skinName = userPortalConfigService.getDefaultPortalSkinName();

    SkinService skinService = portalContainer.getComponentInstanceOfType(SkinService.class);
    String cssPath = skinService.getSkin("portal/Conditions", skinName).getCSSPath();

    String webContentContent = (String) request.getAttribute("WEB_CONTENT");

    ResourceBundleService service = (ResourceBundleService) portalContainer.getComponentInstanceOfType(ResourceBundleService.class);
    ResourceBundle resource = service.getResourceBundle(service.getSharedResourceBundleNames(), request.getLocale());
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="<%=lang%>" lang="<%=lang%>">
<head>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <title>Charte utilisateur</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <link href="<%=cssPath%>" rel="stylesheet" type="text/css"/>

</head>
<body>

    <div class="uiWelcomeBox">

        <h2 class="termsTitle"><%=resource.getString("termsAndConditions.title")%></h2>

        <div class="termsContent"><%=webContentContent%></div>

        <div class="bottom clearfix">
            <form name="tcForm" action="<%= contextPath + "/terms-and-conditions-action"%>" method="post">
                <div class="pull-right">
                    <button class="btn" id="continueButton" ><%=resource.getString("termsAndConditions.accept")%></button>
                </div>
            </form>
        </div>
    </div>

</body>

<style>
.termsTitle {
    text-align: center;
}
.termsContent {
    margin-left: 20px;
    max-height: 450px;
    overflow-y: scroll;
}
</style>
</html>
