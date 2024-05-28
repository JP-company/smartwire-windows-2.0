package smartwire.windows.common.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleCache {
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        cache.put(key, value);
    }

    public Object get(String key) {
        return cache.get(key);
    }

    public void delete(String key) {
        cache.remove(key);
    }
}
