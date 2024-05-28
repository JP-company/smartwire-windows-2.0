package smartwire.windows;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import okhttp3.Response;
import smartwire.windows.common.exception.CustomException;
import smartwire.windows.domain.Machine;
import smartwire.windows.dto.response.MachineResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static smartwire.windows.common.SingletonBean.*;
import static smartwire.windows.common.constant.Constants.*;
import static smartwire.windows.common.file.FileAccess.AUTH_TOKEN_PATH;
import static smartwire.windows.common.file.FileAccess.MACHINE_INFO_PATH;

//@Slf4j
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        Font.loadFont("file:resources/font/Pretendard-Regular.ttf", 14);
        Font.loadFont("file:resources/font/Pretendard-Bold.ttf", 14);

        primaryStage.setTitle("SMART WIRE");
        primaryStage.getIcons().add(new Image("image/logo.png"));
        primaryStage.setResizable(false);

        String viewFile = autoLogin() ? "view/home.fxml" : "view/login.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewFile));

        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private boolean autoLogin() {
        try {
            String authToken = fileAccess.read(AUTH_TOKEN_PATH);
            String machineTokens = fileAccess.read(MACHINE_INFO_PATH);
            if (machineTokens == null || authToken == null) {
                return false;
            }

            String[] machineInfo = machineTokens.split(" ");
            String machineId = machineInfo[1];
            String machineUUID = machineInfo[0];

            Map<String, String> headers = new HashMap<>();
            headers.put(AUTHORIZATION, authToken);

            Map<String, String> queryStrings = new HashMap<>();
            queryStrings.put("machineId", machineId);
            queryStrings.put("machineUUID", machineUUID);

            Response response = httpClient.get("/api/v1/machines/" + machineId + "/" + machineUUID + "/connect", headers, queryStrings);

            if (response.code() != 200) {
                response.close();
                return false;
            }

            String jsonBody = response.body().string();
            MachineResponse machineResponse = objectMapper.readValue(jsonBody, MachineResponse.class);
            Machine machine = machineResponse.getBody();

            simpleCache.put(AUTHORIZATION, authToken);
            simpleCache.put(MACHINE_UUID, machineUUID);
            simpleCache.put(MACHINE, machine);

            response.close();
            return true;
        } catch (CustomException | IOException ce) {
            ce.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args)  {
        try {
            launch(args);
        } catch (Exception e) {
//            log.error("메인 쓰레드 에러 발생", e);
            e.printStackTrace();
        }
    }
}