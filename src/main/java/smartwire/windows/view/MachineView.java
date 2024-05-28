package smartwire.windows.view;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import lombok.Setter;
import smartwire.windows.common.SingletonBean;
import smartwire.windows.common.file.FileAccess;
import smartwire.windows.config.LoginConfig;
import smartwire.windows.config.MachineConfig;
import smartwire.windows.domain.Machine;
import smartwire.windows.domain.Member;
import smartwire.windows.presenter.MachinePresenter;

import java.io.IOException;

import static smartwire.windows.common.SingletonBean.*;
import static smartwire.windows.common.alert.AlertWindow.TITLE_MACHINE_SELECT_FAILED;
import static smartwire.windows.common.constant.Constants.MEMBER;

public class MachineView {
    @Setter
    private MachinePresenter machinePresenter;
    @FXML
    private ListView<Machine> machineListView;
    @FXML
    private Label companyNameLabel;
    @FXML
    private Button machineButton;
    @FXML
    private ProgressIndicator progressIndicator;

    private Node node;

    @FXML
    public void initialize() {
        new MachineConfig(this);
        Member member = (Member) simpleCache.get(MEMBER);
        machineButton.setDefaultButton(true);

        companyNameLabel.setText(member.getCompanyName());
        machineListView.getItems().addAll(member.getMachines());
    }

    @FXML
    public void handleMachineSettingButtonClick(ActionEvent event) {
        node = (Node) event.getSource();
        Machine selectedMachine = machineListView.getSelectionModel().getSelectedItem();
        if (selectedMachine == null) {
            SingletonBean.alertWindow.error(TITLE_MACHINE_SELECT_FAILED, "기계를 선택해주세요.");
            return;
        }
        progressIndicator.setVisible(true);
        machineSelect(selectedMachine).start();
    }

    private Thread machineSelect(Machine selectedMachine) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                machinePresenter.initMachine(selectedMachine);
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                progressIndicator.setVisible(false);
                selectMachineComplete();
            }

            @Override
            protected void failed() {
                super.failed();
                progressIndicator.setVisible(false);
                alertWindow.error(TITLE_MACHINE_SELECT_FAILED, getException().getMessage());
            }
        };
        return new Thread(task);
    }

    @FXML
    public void logoutButtonClick(ActionEvent event) {
        simpleCache.delete(MEMBER);
        fileAccess.delete(FileAccess.AUTH_TOKEN_PATH);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent newRoot = loader.load();
            LoginView loginView = loader.getController();
            new LoginConfig(loginView);

            node = (Node) event.getSource();
            Scene newScene = new Scene(newRoot);
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(newScene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void selectMachineComplete() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent newRoot = loader.load();
            Scene newScene = new Scene(newRoot);
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(newScene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
