package smartwire.windows.dto.response;

import lombok.Getter;
import smartwire.windows.dto.MachineConnectionInfo;

@Getter
public class MachineConnectionResponse extends ApiResponse {
    private MachineConnectionInfo body;
}
