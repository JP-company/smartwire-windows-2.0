package smartwire.windows.domain;

import smartwire.windows.dto.FileStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static smartwire.windows.common.SingletonBean.logMapper;
import static smartwire.windows.common.SingletonBean.simpleCache;
import static smartwire.windows.common.constant.Constants.MACHINE;

public class LogFile {
    private final List<String> lines = new ArrayList<>();
    private final FileStatus fileStatus;

    private LogFile(List<String> logFileLines, FileStatus fileStatus) {
        this.lines.addAll(logFileLines);
        this.fileStatus = fileStatus;
    }

    public static LogFile create(List<String> logFileLines, FileStatus fileStatus) {
        return new LogFile(logFileLines, fileStatus);
    }

    public Log getRecentLog() {
        Collections.reverse(lines);
        int fileLine = lineNumber() - fileStatus.getFileLine();

        for (int index = 0; index < fileLine; index++) {
            String line = lines.get(index);
            Optional<String> logNameOptional = logMapper.findLogKey(line);
            if (!logNameOptional.isPresent()) continue;

            String logKey = logNameOptional.get();
            LocalDateTime logDateTime = extractDateTime(line);

            Machine machine = (Machine) simpleCache.get(MACHINE);
            Log log = Log.builder()
                    .logName(logMapper.findLog(logKey))
                    .logDateTime(logDateTime)
                    .fileName(extractFileName(logKey, line))
                    .thickness(extractThickness(logKey, line))
                    .machineId(machine.getId())
                    .build();

            fileStatus.updateFileLine(lineNumber());
            Collections.reverse(lines);
            return log;
        }
        fileStatus.updateFileLine(lineNumber());
        Collections.reverse(lines);
        return null;
    }

    private int lineNumber() {
        return lines.size() - 1;
    }

    private String extractFileName(String logKey, String line) {
        if (!logKey.equals("* Angle")) return null;

        line = getNextLine(line, 8);
        int fileNameIndex = line.lastIndexOf('-') + 1;
        int ncFileIndex = line.contains(".NC") ?
                line.indexOf(".NC") + 3 :
                line.indexOf(".nc") + 3;

        return line.substring(fileNameIndex, ncFileIndex);
    }

    private Float extractThickness(String logKey, String line) {
        if (!logKey.equals("* Angle")) return null;
        line = getNextLine(line, 2);
        return Float.parseFloat(line.substring(line.indexOf('.') - 3, line.indexOf('.') + 1));
    }

    private LocalDateTime extractDateTime(String line) {
        String pattern = "(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}\\s+(am|pm))";
        Matcher matcher = Pattern.compile(pattern).matcher(line);
        if (matcher.find()) {
            String dateString = matcher.group(1);
            String amPm = matcher.group(2);

            String[] dateTime = dateString.split(" ");

            String[] date = dateTime[0].split("-");
            int year = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);
            int day = Integer.parseInt(date[2]);

            String[] time = dateTime[2].split(":");
            int hour = Integer.parseInt(time[0]);
            int min = Integer.parseInt(time[1]);
            int sec = Integer.parseInt(time[2]);

            LocalDate datePart = LocalDate.of(year, month, day);
            LocalTime timePart = LocalTime.of(hour, min, sec);

            if ("pm".equals(amPm) && timePart.getHour() < 12) {
                timePart = timePart.plusHours(12);
            } else if ("am".equals(amPm) && timePart.getHour() == 12) {
                timePart = timePart.minusHours(12);
            }
            return LocalDateTime.of(datePart, timePart);
        }
        return extractDateTime(getNextLine(line));
    }

    private String getNextLine(String line) {
        return getNextLine(line, 1);
    }
    private String getNextLine(String line, int sequence) {
        return lines.get(lines.indexOf(line) + sequence);
    }
}
