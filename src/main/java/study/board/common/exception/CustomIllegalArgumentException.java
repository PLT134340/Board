package study.board.common.exception;

public class CustomIllegalArgumentException extends CustomRuntimeException {

    public CustomIllegalArgumentException() {
        super();
    }

    public CustomIllegalArgumentException(String message) {
        super(message);
    }

}
