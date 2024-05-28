package smartwire.windows.model;

import okhttp3.Response;
import smartwire.windows.common.exception.CustomException;
import smartwire.windows.domain.Machine;
import smartwire.windows.dto.FileStatus;
import smartwire.windows.dto.MachineConnectionInfo;
import smartwire.windows.dto.response.MachineConnectionResponse;

import java.io.IOException;

import static smartwire.windows.common.SingletonBean.*;
import static smartwire.windows.common.constant.Constants.*;
import static smartwire.windows.common.exception.ErrorCode.LOGIN_FAILED;
import static smartwire.windows.common.exception.ErrorCode.UNKNOWN_ERROR;
import static smartwire.windows.common.file.FileAccess.MACHINE_INFO_PATH;
import static smartwire.windows.common.http.HttpClient.createAuthTokenHeader;

public class MachineModel {

    public void connectRequest(Machine machine) {
        Long machineId = machine.getId();
        Response response = httpClient.post("/api/v1/machines/" + machineId + "/connect", createAuthTokenHeader());
        try {
            if (response.code() != 200) {
                response.close();
                throw new CustomException(LOGIN_FAILED);
            }
            String jsonBody = response.body().string();
            MachineConnectionResponse machineConnectionResponse = objectMapper.readValue(jsonBody, MachineConnectionResponse.class);
            MachineConnectionInfo machineConnectionInfo = machineConnectionResponse.getBody();
            simpleCache.put(MACHINE, machine);
            simpleCache.put(MACHINE_UUID, machineConnectionInfo.getMachineUUID());
            fileAccess.write(MACHINE_INFO_PATH, machineConnectionInfo.getMachineUUID() + " " + machineId);

            if (machineConnectionInfo.getFileStatus() == null) {
                simpleCache.put(FILE_STATUS, FileStatus.create());
                return;
            }
            simpleCache.put(FILE_STATUS, machineConnectionInfo.getFileStatus());

        } catch (IOException e) {
            throw new CustomException(UNKNOWN_ERROR, e);
        } finally {
            response.close();
        }
    }

}
