package smartwire.windows.common.exception;

public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorReason, Throwable cause) {
        super(cause);
        this.errorCode = errorReason;
    }

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getReason());
        this.errorCode = errorCode;
    }

    public String getErrorReason() {
        return errorCode.getReason();
    }


    public boolean equals(ErrorCode errorCode) {
        return this.errorCode.equals(errorCode);
    }


}