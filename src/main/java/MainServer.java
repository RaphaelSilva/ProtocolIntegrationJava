import br.com.pij.ServerProtocol;
import br.com.pij.imp.Client;
import br.com.pij.imp.LogFile;
import br.com.pij.imp.Server;

import java.io.IOException;
import java.util.logging.Level;

public class MainServer {

    public static int getPort (){
        return 8888;
    }

    public static void main(String [] args){
        LogFile.makeInstance(MainServer.class.getName());
        LogFile.setLevel(Level.ALL);
        var server = new Server(getPort());
        server.init();
    }
}
