import br.com.pij.data.FrameData;
import br.com.pij.imp.Client;
import br.com.pij.imp.LogFile;
import br.com.pij.util.MByte;

import java.util.logging.Level;

public class MainClient {

    public static void main(String[] args) throws InterruptedException {
        LogFile.makeInstance(MainClient.class.getName());
        LogFile.setLevel(Level.ALL);
        String timeZone = "America/Sao_Paulo";
        var client = new Client("PC", "127.0.0.1", MainServer.getPort());
        if (client.init()) while (true) {
            var data = "Hello World".getBytes();
            var initial = new byte[]{(byte) 0x0A, (byte) (data.length + 4), (byte) FrameData.MESSAGE};
            var last = new byte[]{(byte) 0xDC, (byte) 0x0D};
            client.sendBytes(MByte.concatAll(initial, data, last));
            var dataResp = client.readBytes();
            Thread.sleep(1000);


            var name = "Raphael Silva";
            data = MByte.concatAll(new byte[]{(byte) 32, (byte) 75, (byte) 180, (byte) name.length()}, name.getBytes());
            initial = new byte[]{(byte) 0x0A, (byte) (data.length + 4), (byte) FrameData.INFO};
            last = new byte[]{(byte) 0xDC, (byte) 0x0D};
            client.sendBytes(MByte.concatAll(initial, data, last));
            Thread.sleep(1000);


            data = timeZone.getBytes();
            initial = new byte[]{(byte) 0x0A, (byte) (data.length + 4), (byte) FrameData.DATETIME};
            last = new byte[]{(byte) 0xDC, (byte) 0x0D};
            client.sendBytes(MByte.concatAll(initial, data, last));
            Thread.sleep(1000);

        }
    }
}
