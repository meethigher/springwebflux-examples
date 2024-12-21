package top.meethigher.exception;

public class DIYException extends Exception{
    public DIYException() {
    }

    public DIYException(String message) {
        super(message);
    }

    public DIYException(String message, Throwable cause) {
        super(message, cause);
    }

    public DIYException(Throwable cause) {
        super(cause);
    }

    public DIYException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
