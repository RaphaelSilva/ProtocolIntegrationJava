package br.com.pij;

import br.com.pij.imp.LogFile;
import br.com.pij.imp.FrameDataProtocol;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.Scanner;

public abstract class ServerProtocol implements IServerProtocol {

    protected Socket socketHostClient;
    protected Thread th;
    public static IServerProtocol factory(Socket client) {
        FrameDataProtocol frameData = new FrameDataProtocol(client);
        return frameData;
    }

    public ServerProtocol(Socket client) {
        this.socketHostClient = client;
    }

    @Override
    public void run() {
        LogFile.getInstance().log("Ready to listen %d", this.socketHostClient.getLocalPort());
        try {
            var in = socketHostClient.getInputStream();
            var out = socketHostClient.getOutputStream();
            this.Decode(in, out);
            this.socketHostClient.close();
        } catch (IOException e) {
            LogFile.getInstance().log(e);
        }
    }
}
