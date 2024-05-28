package smartwire.windows.common.file;

import smartwire.windows.common.exception.CustomException;
import smartwire.windows.common.exception.ErrorCode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static smartwire.windows.common.exception.ErrorCode.FILE_DELETE_FAILURE;

public class FileAccess {
    public static final String AUTH_TOKEN_PATH = "setting/authorization.txt";
    public static final String MACHINE_INFO_PATH = "setting/machine.txt";

    public void write(String filePath, String content) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (FileNotFoundException fileNotFoundException) {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            write(filePath, content);
        } catch (IOException ioException) {
            throw new CustomException(ErrorCode.FILE_IO_EXCEPTION, ioException);
        }
    }
    public void write(String filePath, String content, boolean isAppend) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, isAppend);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (FileNotFoundException fileNotFoundException) {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            write(filePath, content, isAppend);
        } catch (IOException ioException) {
            throw new CustomException(ErrorCode.FILE_IO_EXCEPTION, ioException);
        }
    }

    public String read(String filePath) {
        try{
            StringBuilder content = new StringBuilder();
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            lines.forEach(content::append);
            return content.toString();
        } catch (IOException ioException) {
            throw new CustomException(ErrorCode.FILE_IO_EXCEPTION, ioException);
        }
    }

    public void delete(String filePath) {
        File file = new File(filePath);
        if (file.delete()) {
            return;
        }
        throw new CustomException(FILE_DELETE_FAILURE);
    }
}
