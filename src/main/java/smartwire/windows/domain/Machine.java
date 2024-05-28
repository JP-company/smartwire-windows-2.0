package smartwire.windows.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import smartwire.windows.dto.FileStatus;

@Getter
public class Machine {
    private Long id;
    private String machineName;
    private String machineModel;
    private String dateManufactured;
    private Integer sequence;
    private boolean selected;

    @Setter
    @JsonProperty("memberResponse")
    private Member member;

    private FileStatus fileStatus;

    @Override
    public String toString() {
        String machineModelText = (machineModel != null) ? " / " + machineModel : "";
        String dateManufacturedText = (dateManufactured != null) ? " / " + dateManufactured : "";
        return machineName + machineModelText + dateManufacturedText;
    }

}
