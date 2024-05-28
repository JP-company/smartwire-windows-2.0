package smartwire.windows.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Setter;
import smartwire.windows.common.file.FileAccess;
import smartwire.windows.config.HomeConfig;
import smartwire.windows.config.LoginConfig;
import smartwire.windows.domain.Log;
import smartwire.windows.domain.Machine;
import smartwire.windows.presenter.HomePresenter;

import java.io.IOException;

import static smartwire.windows.common.SingletonBean.fileAccess;
import static smartwire.windows.common.SingletonBean.simpleCache;
import static smartwire.windows.common.constant.Constants.MACHINE;
import static smartwire.windows.common.constant.Constants.MEMBER;

public class HomeView {
    @FXML private ListView<Log> logListView;
    @FXML private Label companyNameLabel;
    @FXML private Label machineNameLabel;
    @FXML private Label connectStatusLabel;
    @FXML private ImageView machineStatusImage;
    @Setter private HomePresenter homePresenter;
    private boolean connection = true;
    private final Image connected = new Image("image/connected.gif");
    private final Image disconnected = new Image("image/disconnected.png");

    @FXML
    public void initialize() {
        new HomeConfig(this);
        Machine machine = (Machine) simpleCache.get(MACHINE);
        companyNameLabel.setText(machine.getMember().getCompanyName());
        machineNameLabel.setText(machine.getMachineName());

        homePresenter.startRealTimeDetection();
        machineStatusImage.setImage(connected);
        connectStatusLabel.setText("현재 연결 상태 : 정상");
    }

    @FXML
    public void logoutButtonClick(ActionEvent event) {
        simpleCache.delete(MEMBER);
        simpleCache.delete(MACHINE);
        fileAccess.delete(FileAccess.AUTH_TOKEN_PATH);
        fileAccess.delete(FileAccess.MACHINE_INFO_PATH);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent newRoot = loader.load();
            LoginView loginView = loader.getController();
            new LoginConfig(loginView);

            Node node = (Node) event.getSource();
            Scene newScene = new Scene(newRoot);
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(newScene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addLog(Log log) {
        logListView.getItems().add(log);
    }

    public void internetDisconnected() {
        if (!connection) return;
        connectStatusLabel.setText("현재 연결 상태 : 실패\n인터넷 연결을 확인해주세요.");
        machineStatusImage.setImage(disconnected);
        connection = false;
    }

    public void internetConnected() {
        if (connection) return;
        connectStatusLabel.setText("현재 연결 상태 : 정상");
        machineStatusImage.setImage(connected);
        connection = true;
    }
}
