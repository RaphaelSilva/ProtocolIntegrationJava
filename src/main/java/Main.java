import br.com.pij.ServerProtocol;
import br.com.pij.imp.Client;
import br.com.pij.imp.LogFile;
import br.com.pij.imp.Server;

import java.io.IOException;

public class Main {

    private static Thread thMain = new Thread("ThreadMain"){
        @Override
        public void run() {
            try {
                var client = new Client("PC1", "localhost", 8787);
                client.sendBytes(new byte[] {(byte) 0xaf, (byte) 0xb1});
            } catch (IOException e) {
                LogFile.getInstance().log(e);
            }
        }
    };

    public static void main(String [] args) throws InterruptedException {
        var server = new Server(8787);
        server.init();
        thMain.start();
    }
}
