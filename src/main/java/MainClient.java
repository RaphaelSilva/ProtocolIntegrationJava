import br.com.pij.imp.Client;
import br.com.pij.imp.LogFile;

import java.io.IOException;

public class MainClient {

    public static void main(String[] args) throws InterruptedException {
        var client = new Client("PC", "127.0.0.1", MainServer.getPort());
        if (client.init()) {
            while (true) {
                var initial = new byte[]{(byte) 0x0A, (byte) 0x10, (byte) 0xA1};
                var data = "Hello World".getBytes();
                var last = new byte[]{(byte) 0xDC, (byte) 0x0D};
                byte[] send = new byte[initial.length + data.length + last.length];
                concat(send, initial, 0);
                concat(send, data, initial.length);
                concat(send, last, data.length + initial.length);
                client.sendBytes(send);
                Thread.sleep(1000);
            }
        }
    }

    public static void concat(byte[] s1, byte[] s2, int off) {
        for (int i = 0; i < s2.length; i++) {
            s1[off + i] = s2[i];
        }
    }
}
