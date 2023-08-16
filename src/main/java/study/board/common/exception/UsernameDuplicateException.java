package study.board.common.exception;

public class UsernameDuplicateException extends CustomRuntimeException {

    public UsernameDuplicateException() {
        super();
    }

    public UsernameDuplicateException(String message) {
        super(message);
    }

}
