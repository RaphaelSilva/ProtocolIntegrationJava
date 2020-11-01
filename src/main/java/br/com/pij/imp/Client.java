package br.com.pij.imp;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Struct;

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

    public Boolean init(){
        try {
            socket = new Socket(address, port);
            LogFile.getInstance().log("Client:: Was created");
            return true;
        } catch (IOException e) {
            LogFile.getInstance().log(e);
            return false;
        }
    }

    public void justSend(byte [] bytes){
        try(var socket = new Socket(address, port)){
            var out = new PrintStream(socket.getOutputStream(), true);
            out.writeBytes(bytes);
            LogFile.getInstance().log("Client::Write bytes ", bytes);
        } catch (UnknownHostException e) {
            LogFile.getInstance().log(e);
        } catch (IOException e) {
            LogFile.getInstance().log(e);
        }
    }

    public boolean sendBytes(byte [] bytes) {
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
}
