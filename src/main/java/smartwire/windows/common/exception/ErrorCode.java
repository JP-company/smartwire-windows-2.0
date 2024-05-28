package smartwire.windows.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    FILE_IO_EXCEPTION("파일 접근에 실패했습니다."),
    LOGIN_FAILED("아이디 혹은 비밀번호가 일치하지 않습니다."),
    EMAIL_EMPTY("이메일을 입력해주세요."),
    LOG_EMPTY("로그가 존재하지 않습니다."),
    EMAIL_REGEX("올바른 이메일 형식을 입력해주세요."),
    PASSWORD_EMPTY("비밀번호를 입력해주세요."),
    NETWORK_CONNECTION_FAILED("인터넷 연결에 실패했습니다."),
    UNKNOWN_ERROR("알 수 없는 오류입니다."),
    INVALID_MACHINE_NAME("유효하지 않은 기계 이름입니다."),
    FILE_DELETE_FAILURE("파일 삭제에 실패했습니다."),
    ;

    private final String reason;

    ErrorCode(String reason) {
        this.reason = reason;
    }


    public boolean equals(ErrorCode errorCode) {
        return this.reason.equals(errorCode.getReason());
    }
}
