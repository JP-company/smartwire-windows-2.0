package smartwire.windows.common.alert;

import javafx.scene.control.Alert;

public class AlertWindow {
    public static final String TITLE_LOGIN_FAILED = "로그인 실패";
    public static final String TITLE_MACHINE_SELECT_FAILED = "기계 설정 실패";
    public void error(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void info(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("알림");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
