package smartwire.windows.presenter;

import lombok.RequiredArgsConstructor;
import smartwire.windows.model.LoginModel;
import smartwire.windows.view.LoginView;

@RequiredArgsConstructor
public class LoginPresenter {
    private final LoginModel loginModel;
    private final LoginView loginView;

    public void login(String loginFormJson) {
        loginModel.login(loginFormJson);
        loginModel.requestMachineInfo();
    }
}