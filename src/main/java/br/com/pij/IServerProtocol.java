package br.com.pij;

import java.net.Socket;

public interface IServerProtocol extends Runnable {

    void Decode(byte b);
}
