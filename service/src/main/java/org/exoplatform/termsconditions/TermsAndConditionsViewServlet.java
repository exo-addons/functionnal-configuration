package org.exoplatform.termsconditions;

import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.rest.response.TermsAndConditions;
import org.exoplatform.service.FunctionalConfigurationService;
import org.exoplatform.utils.NodeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TermsAndConditionsViewServlet extends HttpServlet {

    private static final String JSP_PATH = "/WEB-INF/jsp/user-terms-and-conditions/terms-and-conditions.jsp";
    private static final String WEB_CONTENT_ATTRIBUTE_NAME = "WEB_CONTENT";

    private FunctionalConfigurationService getTermsAndConditionsService() {
        return CommonsUtils.getService(FunctionalConfigurationService.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TermsAndConditions termsAndConditions = getTermsAndConditionsService().getTermsAndConditions();

        request.setAttribute(WEB_CONTENT_ATTRIBUTE_NAME, NodeUtils.getWebContentContentFromUrl(termsAndConditions.getWebContentUrl()));

        getServletContext().getRequestDispatcher(JSP_PATH).forward(request, response);
    }

    @Override    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
