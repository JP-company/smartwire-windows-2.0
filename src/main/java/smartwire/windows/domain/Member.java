package smartwire.windows.domain;

import lombok.Getter;
import smartwire.windows.common.exception.CustomException;
import smartwire.windows.common.exception.ErrorCode;

import java.util.List;

@Getter
public class Member {
    private Long id;
    private String loginEmail;
    private String companyName;
    private String createdDateTime;
    private List<Machine> machines;

    public Machine getMachineBy(String machineName) {
        return machines.stream()
                .filter(machine -> machine.getMachineName().equals(machineName))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MACHINE_NAME));
    }

    public void setMemberToMachines() {
        for (Machine machine : machines) {
            machine.setMember(this);
        }
    }
}
