package smartwire.windows.common.http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import smartwire.windows.common.exception.CustomException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static smartwire.windows.common.SingletonBean.simpleCache;
import static smartwire.windows.common.constant.Constants.AUTHORIZATION;
import static smartwire.windows.common.exception.ErrorCode.NETWORK_CONNECTION_FAILED;

@Slf4j
public class HttpClient {
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final String URL = "http://smartwire.shop";

    public static Map<String, String> createAuthTokenHeader() {
        String authToken = (String) simpleCache.get(AUTHORIZATION);
        Map<String, String> headers = new HashMap<>();
        headers.put(AUTHORIZATION, authToken);
        return headers;
    }

    public Response post(String path, String jsonBody) {
        RequestBody body = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                jsonBody
        );
        Request request = new Request.Builder()
                .url(URL + path)
                .post(body)
                .build();
        try {
            return okHttpClient.newCall(request).execute();
        } catch (IOException ie) {
            log.error("네트워크 통신 오류", ie);
            throw new CustomException(NETWORK_CONNECTION_FAILED, ie);
        }
    }

    public Response post(String path, Map<String, String> header) {
        Request.Builder requestBuilder = new Request.Builder()
                                                .url(URL + path);
        if (header != null) {
            header.keySet().forEach(
                    key -> requestBuilder.addHeader(key, header.get(key))
            );
        }

        RequestBody body = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                ""
        );
        Request request = requestBuilder.post(body).build();

        try {
            return okHttpClient.newCall(request).execute();
        } catch (IOException ie) {
            log.error("네트워크 통신 오류", ie);
            throw new CustomException(NETWORK_CONNECTION_FAILED, ie);
        }
    }

    public Response post(String path, String jsonBody, Map<String, String> header) {
        Request.Builder requestBuilder = new Request.Builder()
                                                .url(URL + path);
        RequestBody body = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                jsonBody
        );

        if (header != null) {
            header.keySet().forEach(
                    key -> requestBuilder.addHeader(key, header.get(key))
            );
        }
        Request request = requestBuilder
                            .post(body)
                            .build();

        try {
            return okHttpClient.newCall(request).execute();
        } catch (IOException ie) {
            log.error("네트워크 통신 오류", ie);
            throw new CustomException(NETWORK_CONNECTION_FAILED, ie);
        }
    }

    public Response get(String path, Map<String, String> header) {
        // header
        Request.Builder requestBuilder = new Request.Builder().url(URL + path);
        if (header != null) {
            header.keySet().forEach(
                    key -> requestBuilder.addHeader(key, header.get(key))
            );
        }
        Request request = requestBuilder.build();

        try {
            return okHttpClient.newCall(request).execute();
        } catch (IOException ie) {
            log.error("네트워크 통신 오류", ie);
            throw new CustomException(NETWORK_CONNECTION_FAILED, ie);
        }
    }

    public Response get(String path, Map<String, String> header, Map<String, String> queryString) {
        // queryString
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL + path).newBuilder();
        if (queryString != null) {
            queryString.keySet().forEach(
                    key -> urlBuilder.addQueryParameter(key, queryString.get(key))
            );
        }
        String fullURL = urlBuilder.build().toString();

        // header
        Request.Builder requestBuilder = new Request.Builder().url(fullURL);
        if (header != null) {
            header.keySet().forEach(
                    key -> requestBuilder.addHeader(key, header.get(key))
            );
        }
        Request request = requestBuilder.build();

        try {
            return okHttpClient.newCall(request).execute();
        } catch (IOException ie) {
            log.error("네트워크 통신 오류", ie);
            throw new CustomException(NETWORK_CONNECTION_FAILED, ie);
        }
    }

    public Response get(String path, Map<String, String> header, String pathVariable) {
        String fullURL = URL + path + "/" + pathVariable;

        // header
        Request.Builder requestBuilder = new Request.Builder().url(fullURL);
        if (header != null) {
            header.keySet().forEach(
                    key -> requestBuilder.addHeader(key, header.get(key))
            );
        }
        Request request = requestBuilder.build();

        try {
            return okHttpClient.newCall(request).execute();
        } catch (IOException ie) {
            log.error("네트워크 통신 오류", ie);
            throw new CustomException(NETWORK_CONNECTION_FAILED, ie);
        }
    }

    public Response get(String path, Map<String, String> header, Map<String, String> queryString, String pathVariable) {
        // queryString
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL + path + "/" + pathVariable).newBuilder();
        if (queryString != null) {
            queryString.keySet().forEach(
                    key -> urlBuilder.addQueryParameter(key, queryString.get(key))
            );
        }
        String fullURL = urlBuilder.build().toString();

        // header
        Request.Builder requestBuilder = new Request.Builder().url(fullURL);
        if (header != null) {
            header.keySet().forEach(
                    key -> requestBuilder.addHeader(key, header.get(key))
            );
        }
        Request request = requestBuilder.build();

        try {
            return okHttpClient.newCall(request).execute();
        } catch (IOException ie) {
            log.error("네트워크 통신 오류", ie);
            throw new CustomException(NETWORK_CONNECTION_FAILED, ie);
        }
    }

    private void throwNetworkError(Exception e) {
        log.error("네트워크 통신 오류", e);
        throw new CustomException(NETWORK_CONNECTION_FAILED, e);
    }
}
