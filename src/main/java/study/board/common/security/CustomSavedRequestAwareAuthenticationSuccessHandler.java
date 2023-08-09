package study.board.common.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

public class CustomSavedRequestAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        String prevPage = (String) request.getSession().getAttribute("prevPage");
        if (prevPage != null) {
            request.getSession().removeAttribute("prevPage");
        }

        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        if (savedRequest == null && prevPage != null && !prevPage.equals("")) {
            String targetUrl;
            if (prevPage.contains("/sign-up")) {
                targetUrl = "/";
            } else {
                targetUrl = prevPage;
            }
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
            return;
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
