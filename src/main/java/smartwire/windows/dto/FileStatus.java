package smartwire.windows.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.time.LocalDate;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class FileStatus {
    private String processName;
    private String fileName;
    private int fileLine;

    public static FileStatus create() {
        return FileStatus.builder()
                .fileName(LocalDate.now() + ".log")
                .fileLine(0)
                .build();
    }
    public static FileStatus create(File file) {
        return create(file, 0);
    }

    public static FileStatus create(File file, int fileLength) {
        return FileStatus.builder()
                .fileName(file.getName())
                .fileLine(fileLength)
                .build();
    }

    public boolean isEarlierThan(File file) {
        int criteriaOfDate = Integer.parseInt(
                fileName
                .replace(".log", "")
                .replace("-", "")
        );
        int comparisonTargetDate = Integer.parseInt(
                file.getName()
                        .replace(".log", "")
                        .replace("-", "")
        );
        return criteriaOfDate <= comparisonTargetDate;
    }

    public void updateFileLine(int fileLine) {
        this.fileLine = fileLine;
    }

    public boolean isSameWith(File file) {
        return fileName.equals(file.getName());
    }
}
