package study.board.common.exception;

public class DeletedUserLoginException extends CustomRuntimeException {

    public DeletedUserLoginException() {
        super();
    }

    public DeletedUserLoginException(String message) {
        super(message);
    }

}
