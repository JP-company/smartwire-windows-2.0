package smartwire.windows.dto.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiResponse {
    private boolean success;
    private String message;
    private Object body;
}
