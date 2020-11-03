package br.com.pij.imp;

import br.com.pij.util.MByte;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public String name;
    public String address;
    public int port;
    private Socket socket;

    public Client(String name, String address, int port) {
        this.port = port;
        this.address = address;
        this.name = name;
    }

    public Boolean init() {
        try {
            socket = new Socket(address, port);
            LogFile.getInstance().log("Client:: Was created");
            return true;
        } catch (IOException e) {
            LogFile.getInstance().log(e);
            return false;
        }
    }

    public void justSend(byte[] bytes) {
        try (var socket = new Socket(address, port)) {
            var out = new PrintStream(socket.getOutputStream(), true);
            out.writeBytes(bytes);
            LogFile.getInstance().log("Client::Write bytes ", bytes);
        } catch (IOException e) {
            LogFile.getInstance().log(e);
        }
    }

    public boolean sendBytes(byte[] bytes) {
        try {
            if (socket.isConnected()) {
                var out = new PrintStream(socket.getOutputStream(), true);
                out.writeBytes(bytes);
                LogFile.getInstance().log("Client::Write bytes ", bytes);
                return true;
            }
        } catch (IOException e) {
            LogFile.getInstance().log(e);
        }
        return false;
    }

    public byte[] readBytes() {
        try {
            if (socket.isConnected()) {
                var in = socket.getInputStream();
                int init;
                do {
                    init = in.read();
                }
                while (init != 0x0A && init != -1);
                var len = in.read();
                var data = in.readNBytes(len-2);
                data = MByte.concatAll(new byte[]{(byte) 0x0A, (byte) len}, data);
                LogFile.getInstance().log("Client::Write bytes ", data);
                return data;
            }
        } catch (IOException e) {
            LogFile.getInstance().log(e);
        }
        return null;
    }
}
