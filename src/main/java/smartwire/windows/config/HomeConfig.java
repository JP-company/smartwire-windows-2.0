package smartwire.windows.config;

import smartwire.windows.model.HomeModel;
import smartwire.windows.presenter.HomePresenter;
import smartwire.windows.view.HomeView;

public class HomeConfig {
    public HomeConfig(HomeView homeView) {
        homePresenter(homeView);
    }

    private HomeModel homeModel(HomeView homeView) {
        return new HomeModel(homeView);
    }

    private void homePresenter(HomeView homeView) {
        HomePresenter homePresenter = new HomePresenter(homeModel(homeView));
        homeView.setHomePresenter(homePresenter);
    }
}
