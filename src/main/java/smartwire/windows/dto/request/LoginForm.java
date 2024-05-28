package smartwire.windows.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import smartwire.windows.common.exception.CustomException;
import smartwire.windows.common.exception.ErrorCode;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class LoginForm {
    private String loginEmail;
    private String loginPassword;

    public static LoginForm create(String loginEmail, String loginPassword) {
        validate(loginEmail, loginPassword);
        return LoginForm.builder()
                .loginEmail(loginEmail)
                .loginPassword(loginPassword)
                .build();
    }

    private static void validate(String loginEmail, String loginPassword) {
        if (loginEmail.isEmpty()) {
            throw new CustomException(ErrorCode.EMAIL_EMPTY);
        }
        if (loginPassword.isEmpty()) {
            throw new CustomException(ErrorCode.PASSWORD_EMPTY);
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,40}$";
        if (!loginEmail.matches(emailRegex)) {
            throw new CustomException(ErrorCode.EMAIL_REGEX);
        }
    }
}
