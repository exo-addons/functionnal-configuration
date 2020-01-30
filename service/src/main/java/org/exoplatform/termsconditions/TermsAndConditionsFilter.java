package org.exoplatform.termsconditions;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.web.filter.Filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TermsAndConditionsFilter implements Filter {

    private static final String TERMS_AND_CONDITIONS_SERVLET_URL = "/terms-and-conditions";
    private static final String FUNCTIONAL_CONFIGURATION_EXTENSION_WAR = "/functional-configuration";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String remoteUserName = getRemoteUserName(httpServletRequest);

        TermsAndConditionsService termsAndConditionsService = getTermsAndConditionsService();
        if (termsAndConditionsService.isTermsAndConditionsActive() && StringUtils.isNotEmpty(remoteUserName)) {

            if (!termsAndConditionsService.isTermsAndConditionsAcceptedBy(remoteUserName)) {
                ServletContext context = httpServletRequest.getSession().getServletContext().getContext(FUNCTIONAL_CONFIGURATION_EXTENSION_WAR);
                context.getRequestDispatcher(TERMS_AND_CONDITIONS_SERVLET_URL).forward(httpServletRequest, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private String getRemoteUserName(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRemoteUser();
    }

    private TermsAndConditionsService getTermsAndConditionsService() {
        return CommonsUtils.getService(TermsAndConditionsService.class);
    }
}
