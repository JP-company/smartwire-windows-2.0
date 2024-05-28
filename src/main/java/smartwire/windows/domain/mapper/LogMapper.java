package smartwire.windows.domain.mapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class LogMapper {
    private final Map<String, String> logs = new LinkedHashMap<>();

    public LogMapper() {
        logs.put("작업 재시작", "start_작업 재시작");
        logs.put("* Angle", "start_작업 시작");
        logs.put("가공감지", "start_가공 재시작");
        logs.put("Reset", "reset_리셋");
        logs.put("M코드정지", "stop_M코드 정지");
        logs.put("작업 끝", "done_작업 완료");
        logs.put("Wire Contact Auto Stop", "stop_와이어 접촉");
        logs.put("작업중 30sec 접촉 정지", "stop_와이어 30초 접촉");
        logs.put("작업중 단선", "stop_작업중 단선");
        logs.put("보빈 와이어 단선", "stop_보빈 와이어 단선");
        logs.put("M20-삽입실패",  "stop_자동결선 삽입실패(M20)");
        logs.put("M21-절단실패", "stop_자동결선 절단실패(M21)");
        logs.put("M21-잔여와이어 처리실패", "stop_자동결선 잔여와이어 처리실패(M21)");
        logs.put("와이어 미동작", "stop_와이어 미동작");
        logs.put("가공액 미동작", "stop_가공액 미동작");
        logs.put("자동결선 FEED MOTOR ALARM", "stop_자동결선 FEED MOTOR ALARM");
        logs.put("자동결선 절단 공정 실패", "stop_자동결선 절단 공정 실패");
        logs.put("자동결선 잔여 WIRE 처리 실패", "stop_자동결선 잔여 WIRE 처리 실패");
        logs.put("자동결선 하부 뭉치 WIRE CONTACT", "stop_자동결선 하부 뭉치 WIRE CONTACT");
        logs.put("자동결선 상부 센서 WIRE CONTACT", "stop_자동결선 상부 센서 WIRE CONTACT");
        logs.put("회수부 와이어 이탈", "stop_회수부 와이어 이탈");
        logs.put("AWF 명령끝날때까지 센서감지", "stop_AWF 명령끝날때까지 센서감지");
        logs.put("작업중 정지", "stop_작업중 정지");
        logs.put("Work Tank Fluid Sensor Abnormal", "stop_오일센서 이상 감지");
        logs.put("Auto Door Sensor Abnormal", "stop_자동문센서 이상 감지");
        logs.put("Ready On", "stop_Ready On");
        logs.put("Emergency Stop", "stop_비상정지");
        logs.put("READY Off", "stop_READY Off");
        logs.put("Initialization", "stop_와이어 기계 연결 완료");
        logs.put("SPM Device Closed", "stop_와이어 기계 전원 종료됨");
        logs.put("SPM Device DisConnected", "stop_와이어 기계 연결 끊어짐");
        logs.put("SPM Device Open Succeeded", "stop_와이어 기계 전원 켜짐");

        logs.put("ReStart Working", "start_작업 재시작");
        logs.put("Stop Working(User)", "stop_작업중 정지(사용자)");
        logs.put("Mcode", "stop_M코드 정지");
        logs.put("End Working", "done_작업 완료");
        logs.put("30sec Contact Stop", "stop_와이어 30초 접촉");
        logs.put("Bobbin Wire breakage", "stop_보빈 와이어 단선");
        logs.put("M20-Insert Error", "stop_자동결선 삽입실패(M20)");
        logs.put("M21-Cutting Error", "stop_자동결선 절단실패(M21)");
        logs.put("Air Pressure abnormal", "stop_Air Pressure abnormal");
        logs.put("AWF Feed Motor Stop", "stop_AWF Feed Motor Stop");
        logs.put("Auto Wire Feeding  Upper Sensor Wire Contact", "stop_자동결선 상부 센서 WIRE CONTACT");
        logs.put("Auto Start Fail", "stop_자동 재시작 실패");
        logs.put("Stop Working", "stop_작업중 정지");

        logs.put("none", "");
    }

    public Optional<String> findLogKey(String line) {
        return logs.keySet().stream()
                .filter(line::contains)
                .findFirst();
    }

    public String findLog(String logKey) {
        return logs.get(logKey);
    }

    public boolean matches(String line) {
        return logs.keySet().stream()
                .anyMatch(line::contains);
    }

    public String noneMatches() {
        return "none";
    }
}
