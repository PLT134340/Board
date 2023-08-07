package study.board.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import study.board.service.dto.user.UserInform;

import java.util.Map;

public class UserEditInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        UserInform userInform = (UserInform) session.getAttribute("userInform");
        Long userId = userInform.getId();

        Map<String, Object> pathVariables = (Map<String, Object>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long reviewId = Long.parseLong((String) pathVariables.get("userId"));

        if (userId == null || reviewId == null || userId != reviewId) {
            response.sendRedirect("/users");
            return false;
        }

        return true;
    }

}
