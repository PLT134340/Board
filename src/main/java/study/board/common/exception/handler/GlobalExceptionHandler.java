package study.board.common.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import study.board.common.exception.BoardNameDuplicateException;
import study.board.common.exception.CustomIllegalArgumentException;
import study.board.common.exception.LikeDuplicateException;
import study.board.common.exception.UsernameDuplicateException;

@ControllerAdvice
public class GlobalExceptionHandler {

/*
//    FailureHandler 처리 필요
    @ExceptionHandler(DeletedUserLoginException.class)
    protected String handlerDeletedUserLoginException(DeletedUserLoginException ex) {
        return "redirect:/sign-up?error=delete";
    }
*/

    @ExceptionHandler(UsernameDuplicateException.class)
    protected String handleUsernameDuplicateException(UsernameDuplicateException ex) {
        return "redirect:/sign-up?error=duplicate";
    }

    @ExceptionHandler(BoardNameDuplicateException.class)
    protected String handleBoardNameDuplicateException(BoardNameDuplicateException ex) {
        return "redirect:/boards/create?error=duplicate";
    }

    @ExceptionHandler(LikeDuplicateException.class)
    protected String handleLikeDuplicateException(LikeDuplicateException ex,
                                                  HttpServletRequest request) {
        String refer = request.getHeader(HttpHeaders.REFERER);
        if (refer.contains("error"))
            return "redirect:" + refer;
        return "redirect:" + refer + "?error=duplicate";
    }

    @ExceptionHandler(CustomIllegalArgumentException.class)
    protected String handleCustomIllegalArgumentException(CustomIllegalArgumentException ex) {
        System.out.println("예외");
        return "error/400";
    }

}
