package smartwire.windows.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import smartwire.windows.common.alert.AlertWindow;
import smartwire.windows.common.cache.SimpleCache;
import smartwire.windows.common.file.FileAccess;
import smartwire.windows.common.http.HttpClient;
import smartwire.windows.domain.mapper.LogMapper;

public class SingletonBean {
    public static ObjectMapper objectMapper = new ObjectMapper();
    public static HttpClient httpClient = new HttpClient();
    public static AlertWindow alertWindow = new AlertWindow();
    public static FileAccess fileAccess = new FileAccess();
    public static SimpleCache simpleCache = new SimpleCache();
    public static LogMapper logMapper = new LogMapper();
}
