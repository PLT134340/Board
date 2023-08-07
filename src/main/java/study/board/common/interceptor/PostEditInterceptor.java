package study.board.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import study.board.service.PostService;
import study.board.service.dto.user.UserInform;

import java.util.Map;

@RequiredArgsConstructor
public class PostEditInterceptor implements HandlerInterceptor {

    private final PostService postService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        UserInform userInform = (UserInform) session.getAttribute("userInform");
        Long userId = userInform.getId();

        Map<String, Object> pathVariables = (Map<String, Object>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long boardId = Long.parseLong((String) pathVariables.get("boardId"));
        Long postId = Long.parseLong((String) pathVariables.get("postId"));

        if (userId == null || postId == null || postService.findById(postId).getUser().getId() != userId) {
            response.sendRedirect("/boards/" + boardId + "/" + postId);
            return false;
        }

        return true;
    }

}
