package smartwire.windows.common.file;

import smartwire.windows.common.SingletonBean;

class FileAccessTest {

//    @Test
    void write() {
    }

//    @Test
    void read() {
        String read = SingletonBean.fileAccess.read(FileAccess.AUTH_TOKEN_PATH);
        System.out.println("read = " + read);
    }

//    @Test
    void test() {

    }
}