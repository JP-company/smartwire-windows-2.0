package smartwire.windows.dto.response;

import lombok.Getter;
import smartwire.windows.domain.Machine;

@Getter
public class MachineResponse extends ApiResponse {
    private Machine body;
}
