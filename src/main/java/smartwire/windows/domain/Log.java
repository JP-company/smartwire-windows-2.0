package smartwire.windows.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Log {
    private String logName;
    private LocalDateTime logDateTime;
    private Integer logFileLine;

    private String fileName;
    private Float thickness;

    private Long machineId;
    private String machineUUID;

    @Override
    public String toString() {
        String logDateTime = (this.logDateTime.withNano(0) + "").replace("T", " ");
        String logName = this.logName.contains("_") ? " | " + this.logName.split("_")[1] : " | " + this.logName;
        String fileName = this.fileName != null ? " | " + this.fileName : "";
        String thickness = this.thickness != null ? " | " + this.thickness + "T" : "";
        return logDateTime + logName + fileName + thickness;
    }
}
