package br.com.pij.imp;

import java.io.IOException;
import java.net.Socket;

public class Client {

    public String name;
    public String address;
    public int port;
    private Socket socket;

    public Client(String name, String address, int port) {
        this.port = port;
        this.address = address;
        this.name = name;
        try {
            this.socket = new Socket("localhost", this.port);
        } catch (IOException e) {
            LogFile.getInstance().log(e);
        }
    }

    public boolean sendBytes(byte [] bytes) throws IOException {
        var out = this.socket.getOutputStream();
        var in = socket.getInputStream();
        LogFile.getInstance().log("Send bytes %s", new String(bytes, "UTF-8"));
        out.write(bytes);
        in.read(bytes);
        return true;
    }
}
