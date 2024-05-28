package smartwire.windows.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Setter;
import smartwire.windows.common.exception.CustomException;
import smartwire.windows.config.LoginConfig;
import smartwire.windows.presenter.LoginPresenter;
import smartwire.windows.dto.request.LoginForm;

import java.io.IOException;

import static smartwire.windows.common.SingletonBean.alertWindow;
import static smartwire.windows.common.SingletonBean.objectMapper;
import static smartwire.windows.common.alert.AlertWindow.TITLE_LOGIN_FAILED;
import static smartwire.windows.common.exception.ErrorCode.UNKNOWN_ERROR;

public class LoginView {
    @Setter private LoginPresenter loginPresenter;
    @FXML private TextField loginEmailTextField;
    @FXML private TextField loginPasswordTextField;
    @FXML private ImageView logo;
    @FXML private Button loginButton;
    @FXML private ProgressIndicator progressIndicator;
    private Node node;

    @FXML
    public void initialize() {
        new LoginConfig(this);
        Image statusImage = new Image("image/logo.png");
        logo.setImage(statusImage);
        loginButton.setDefaultButton(true);
    }

    @FXML
    private void handleLoginButtonClick(ActionEvent event) {
        try {
            LoginForm loginForm = LoginForm.create(loginEmailTextField.getText(), loginPasswordTextField.getText());
            String loginFormJson = objectMapper.writeValueAsString(loginForm);
            node = (Node) event.getSource();

            progressIndicator.setVisible(true);
            getThread(loginFormJson).start();
        } catch (JsonProcessingException je) {
            alertWindow.error(TITLE_LOGIN_FAILED, UNKNOWN_ERROR.getReason());
        } catch (CustomException ce) {
            alertWindow.error(TITLE_LOGIN_FAILED, ce.getErrorReason());
        }
    }

    private Thread getThread(String loginFormJson) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                loginPresenter.login(loginFormJson);
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                progressIndicator.setVisible(false);
                loginComplete();
            }

            @Override
            protected void failed() {
                super.failed();
                progressIndicator.setVisible(false);
                alertWindow.error(TITLE_LOGIN_FAILED, getException().getMessage());
            }
        };
        return new Thread(task);
    }


    public void loginComplete() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("machines.fxml"));
            Parent newRoot = loader.load();

            Scene newScene = new Scene(newRoot);
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(newScene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
