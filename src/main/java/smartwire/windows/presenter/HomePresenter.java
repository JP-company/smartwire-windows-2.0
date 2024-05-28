package smartwire.windows.presenter;

import lombok.RequiredArgsConstructor;
import smartwire.windows.model.HomeModel;

@RequiredArgsConstructor
public class HomePresenter {
    private final HomeModel homeModel;

    public void startRealTimeDetection() {
        homeModel.startDetection();
    }
}
