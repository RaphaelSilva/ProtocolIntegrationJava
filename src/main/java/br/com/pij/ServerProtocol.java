package br.com.pij;

import br.com.pij.imp.LogFile;
import br.com.pij.imp.FrameDataProtocol;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public abstract class ServerProtocol implements IServerProtocol {

    protected Socket client;

    public static IServerProtocol factory(Socket client) {
        FrameDataProtocol frameData = new FrameDataProtocol(client);
        return frameData;
    }

    public ServerProtocol(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(client.getInputStream());
            while (in.hasNextByte()) {
                this.Decode(in.nextByte());
            }
            this.client.close();
        } catch (IOException e) {
            LogFile.getInstance().log(e);
        }
    }
}
