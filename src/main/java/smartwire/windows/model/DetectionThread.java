package smartwire.windows.model;

import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import smartwire.windows.common.SingletonBean;
import smartwire.windows.common.exception.CustomException;
import smartwire.windows.domain.Log;
import smartwire.windows.domain.LogFile;
import smartwire.windows.dto.FileStatus;
import smartwire.windows.view.HomeView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.file.StandardWatchEventKinds.*;
import static smartwire.windows.common.SingletonBean.httpClient;
import static smartwire.windows.common.SingletonBean.simpleCache;
import static smartwire.windows.common.constant.Constants.FILE_STATUS;
import static smartwire.windows.common.http.HttpClient.createAuthTokenHeader;

@Slf4j
public class DetectionThread implements Runnable {

    public static final String LOG_FILE_PATH = "C:/spmEzCut/LogMessage";
    public static final String FILE_EXTENSION1 = ".log";
    public static final String FILE_EXTENSION2 = ".Log";
    public static final String ENCODING = "CP949";
    private final Path directoryPath = Paths.get(LOG_FILE_PATH);
    private final WatchService watchService = FileSystems.getDefault().newWatchService();
    private Path currentLogFIlePath;

    private final HomeView homeView;

    public DetectionThread(HomeView homeView) throws IOException {
        this.homeView = homeView;
        directoryPath.register(watchService, ENTRY_CREATE, ENTRY_MODIFY);
    }

    @Override
    public void run() {
        currentLogFIlePath = initLogFile();
        try {
            log.info("실시간 로그 감지 쓰레드 시작");
            startWatching();
        } catch (Exception e) {
            log.error("실시간 로그 감지 오류=", e);
            log.info("실시간 로그 감지 쓰레드 재시작");
            startWatching();
        }
    }

    private void startWatching() {
        while (true) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == OVERFLOW) continue;

                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path changed = ev.context();
                Path changedAbsolutePath = directoryPath.resolve(ev.context());

                if (kind == ENTRY_CREATE && (changed.toString().endsWith(FILE_EXTENSION1) || changed.toString().endsWith(FILE_EXTENSION2))) {
                    FileStatus fileStatus = (FileStatus) simpleCache.get(FILE_STATUS);
                    fileStatus.updateFileLine(0);
                    currentLogFIlePath = changedAbsolutePath;
                }
                if (kind == ENTRY_MODIFY && changedAbsolutePath.equals(currentLogFIlePath)) {
                    FileStatus fileStatus = (FileStatus) simpleCache.get(FILE_STATUS);

                    LogFile logFile;
                    try {
                        logFile = readLogFile(currentLogFIlePath, fileStatus);
                    } catch (IOException e) {continue;}

                    Log recentLog = logFile.getRecentLog();

                    if (recentLog != null && requestLog(recentLog)){
                        Platform.runLater(() -> homeView.addLog(recentLog));
                        Platform.runLater(homeView::internetConnected);
                    }
                }
            }
            key.reset();
        }
    }


    private Path initLogFile() {
        File[] logFiles = new File(LOG_FILE_PATH)
                .listFiles(file -> file.getName().endsWith(FILE_EXTENSION1) || file.getName().endsWith(FILE_EXTENSION2));

        if (logFiles == null || logFiles.length == 0) {
            delay();
            return initLogFile();
        }

        File currentFile = Arrays.stream(logFiles)
                .max(Comparator.comparingLong(File::lastModified))
                .get();

        FileStatus fileStatus = (FileStatus) simpleCache.get(FILE_STATUS);
        if (fileStatus == null || !currentFile.getName().equals(fileStatus.getFileName())) {
            fileStatus = FileStatus.create(currentFile);
            simpleCache.put(FILE_STATUS, fileStatus);
        }

        LogFile logFile;
        try {
            logFile = readLogFile(currentFile.toPath(), fileStatus);
        } catch (IOException e) {
            return currentFile.toPath();
        }

        Log recentLog = logFile.getRecentLog();
        if (recentLog != null && requestLog(recentLog)){
            Platform.runLater(() -> homeView.addLog(recentLog));
        }
        return currentFile.toPath();
    }

    private void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
    }


    private LogFile readLogFile(Path currentFilePath, FileStatus fileStatus) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(currentFilePath), ENCODING));
        return LogFile.create(
                reader.lines().collect(Collectors.toList()),
                fileStatus
        );
    }

    private boolean requestLog(Log sendLog) {
        try {
            String logBody = SingletonBean.objectMapper.writeValueAsString(sendLog);
            Response response = httpClient.post("/api/v1/logs/save", logBody, createAuthTokenHeader());
            if (response.code() == 200) {
                log.info("로그 전송 성공 = {}", logBody);
                response.close();
                return true;
            }
            log.error("로그 전송 실패");
            response.close();
            return false;
        } catch (CustomException | IOException e) {
            log.error("로그 전송 실패");
            Platform.runLater(homeView::internetDisconnected);
            return false;
        }
    }



    private Path initializeLogFile() throws InterruptedException, IOException {
        File[] logFiles = new File(LOG_FILE_PATH)
                .listFiles(file -> file.getName().endsWith(FILE_EXTENSION1) || file.getName().endsWith(FILE_EXTENSION2));

        if (logFiles == null || logFiles.length == 0) {
            Thread.sleep(1000);
            return initializeLogFile();
        }

//        FileStatus fileStatus = (FileStatus) simpleCache.get(FILE_STATUS);
        FileStatus fileStatus = FileStatus.create();

        List<File> targetLogFiles = Arrays.stream(logFiles)
                .filter(fileStatus::isEarlierThan)
                .collect(Collectors.toList());

        for (File file : targetLogFiles) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), ENCODING));

            List<String> logFileLines = reader.lines().collect(Collectors.toList());

            if (fileStatus.isSameWith(file)) {
                IntStream.range(0, fileStatus.getFileLine())
                        .forEach(line -> logFileLines.set(line, ""));
            }
        }
        return targetLogFiles.get(targetLogFiles.size() - 1).toPath();
    }
}
