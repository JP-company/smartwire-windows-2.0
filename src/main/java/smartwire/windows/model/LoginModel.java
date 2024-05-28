package smartwire.windows.model;

import okhttp3.Response;
import smartwire.windows.common.exception.CustomException;
import smartwire.windows.domain.Member;
import smartwire.windows.dto.response.MemberResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static smartwire.windows.common.SingletonBean.*;
import static smartwire.windows.common.constant.Constants.AUTHORIZATION;
import static smartwire.windows.common.constant.Constants.MEMBER;
import static smartwire.windows.common.exception.ErrorCode.LOGIN_FAILED;
import static smartwire.windows.common.exception.ErrorCode.UNKNOWN_ERROR;
import static smartwire.windows.common.file.FileAccess.AUTH_TOKEN_PATH;

public class LoginModel {
    public void login(String loginFormJson) {
        Response response = httpClient.post("/api/v1/login", loginFormJson);
        if (response.code() != 200) {
            response.close();
            throw new CustomException(LOGIN_FAILED);
        }
        String authToken = response.header(AUTHORIZATION);
        simpleCache.put(AUTHORIZATION, authToken);
        fileAccess.write(AUTH_TOKEN_PATH, authToken);
        response.close();
    }

    public void requestMachineInfo() {
        String authToken = (String) simpleCache.get(AUTHORIZATION);
        Map<String, String> headers = new HashMap<>();
        headers.put(AUTHORIZATION, authToken);

        Response response = httpClient.get("/api/v1/member-with-machines", headers);

        if (response.code() != 200) {
            response.close();
            throw new CustomException(UNKNOWN_ERROR);
        }

        try {
            String jsonBody = response.body().string();
            MemberResponse memberResponse = objectMapper.readValue(jsonBody, MemberResponse.class);
            Member member = memberResponse.getBody();
            member.setMemberToMachines();
            simpleCache.put(MEMBER, member);
        } catch (IOException e) {
            throw new CustomException(UNKNOWN_ERROR, e);
        } finally {
            response.close();
        }
    }
}
