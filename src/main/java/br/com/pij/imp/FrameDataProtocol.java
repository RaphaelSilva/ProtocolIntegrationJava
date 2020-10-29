package br.com.pij.imp;

import br.com.pij.ServerProtocol;

import java.net.Socket;

public class FrameDataProtocol extends ServerProtocol {

    public FrameDataProtocol(Socket client) {
        super(client);
    }

    @Override
    public void Decode(byte b) {
        LogFile.getInstance().log("Receive a byte %h", b);
    }
}
