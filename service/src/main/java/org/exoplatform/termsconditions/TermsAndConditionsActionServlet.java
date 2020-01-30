package org.exoplatform.termsconditions;

import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.service.FunctionalConfigurationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TermsAndConditionsActionServlet extends HttpServlet {

    private static final String ACTION_REDIRECT_URI = "/portal/";

    private TermsAndConditionsService getTermsAndConditionsService() {
        return CommonsUtils.getService(TermsAndConditionsService.class);
    }

    public FunctionalConfigurationService getFunctionalConfigurationService() {
        return CommonsUtils.getService(FunctionalConfigurationService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        getTermsAndConditionsService().accept(request.getRemoteUser());
        response.sendRedirect(ACTION_REDIRECT_URI);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
