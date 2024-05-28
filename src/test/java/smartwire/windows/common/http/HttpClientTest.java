package smartwire.windows.common.http;

import org.junit.jupiter.api.Test;
import smartwire.windows.domain.Member;

import java.io.IOException;

import static smartwire.windows.common.SingletonBean.objectMapper;

class HttpClientTest {

//    @Test
    void post() {
    }

//    @Test
    void get() throws IOException {
        String jsonBody = "{\"id\" : 1, \"loginEmail\" : \"wjsdj2008@gmail.com\", \"companyName\" : \"sit\", \"createdDateTime\" : \"2024-01-01 00:00:00\", \"machines\" : [{\"id\": 1, \"machineName\":\"1FIRST\", \"machineModel\": null, \"dateManufactured\": \"2024-01-01 00:00:00\", \"sequence\": 0, \"machineUUID\": \"123\"}, {\"id\": 2, \"machineName\":\"2SECOND\", \"machineModel\": null, \"dateManufactured\": null, \"sequence\":1, \"machineUUID\": \"456\"}]}";
        Member member = objectMapper.readValue(jsonBody, Member.class);
        System.out.println("member = " + member);
    }

    @Test
    void test() {}
}