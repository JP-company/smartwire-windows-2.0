package smartwire.windows.model;

import lombok.extern.slf4j.Slf4j;
import smartwire.windows.view.HomeView;

import java.io.IOException;

@Slf4j
public class HomeModel {
    private final HomeView homeView;

    public HomeModel(HomeView homeView) {
        this.homeView = homeView;
    }

    public void startDetection() {
        try {
            DetectionThread detectionThread = new DetectionThread(homeView);
            Thread thread = new Thread(detectionThread);
            thread.setDaemon(true);
            thread.start();
        } catch(IOException e) {
            log.error("쓰레드 실행 실패", e);
            e.printStackTrace();
        }
    }
}
