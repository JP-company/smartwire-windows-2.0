package smartwire.windows.config;

import smartwire.windows.model.LoginModel;
import smartwire.windows.presenter.LoginPresenter;
import smartwire.windows.view.LoginView;

public class LoginConfig {
    public LoginConfig(LoginView loginView) {
        loginPresenter(loginView);
    }

    private LoginModel loginModel() {
        return new LoginModel();
    }

    private void loginPresenter(LoginView loginView) {
        LoginPresenter loginPresenter = new LoginPresenter(loginModel(), loginView);
        loginView.setLoginPresenter(loginPresenter);
    }
}
