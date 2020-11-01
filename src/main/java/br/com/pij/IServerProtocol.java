package br.com.pij;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IServerProtocol extends Runnable {

    void Decode(InputStream in, OutputStream out) throws IOException;
}
