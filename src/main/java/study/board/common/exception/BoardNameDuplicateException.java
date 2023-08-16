package study.board.common.exception;

public class BoardNameDuplicateException extends CustomRuntimeException {

    public BoardNameDuplicateException() {
        super();
    }

    public BoardNameDuplicateException(String message) {
        super(message);
    }

}
