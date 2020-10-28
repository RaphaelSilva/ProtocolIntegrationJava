package br.com.pij.imp;

import br.com.pij.IServerProtocol;
import br.com.pij.IServer;
import br.com.pij.ServerProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server implements IServer, Runnable {
    private ServerSocket server;
    private int port;
    private Thread thread;
    private boolean ThreadControl = false;
    private boolean ThreadRunning = false;

    public Server(int port) {
        this.port = port;
    }

    public void init() {
        this.ThreadControl = true;
        this.thread = new Thread(this, "Thread.Main.Server");
        this.thread.start();
    }

    public void StopServer() {
        this.ThreadControl = false;
        var thGroup = this.thread.getThreadGroup();
        Thread [] all = new Thread[thGroup.activeCount()];
        thGroup.enumerate(all, true);
        try {
            this.server.close();
            for(var th: all){
                th.join();
            }
        } catch (InterruptedException | IOException e) {
            LogFile.getInstance().log(e);
        }finally {
            this.ThreadRunning = false;
        }
    }

    @Override
    public void run() {
        try {
            this.server = new ServerSocket(this.port);
            while (ThreadControl) {
                ThreadRunning = true;
                var client = this.server.accept();
                var protocol = ServerProtocol.factory(client);
                Thread th = new Thread(this.thread.getThreadGroup(), protocol);
                th.start();
            }
            this.server.close();
        } catch (IOException e) {
            LogFile.getInstance().log(e);
        }
    }

    public int getPort() {
        return port;
    }

    public ServerSocket getServer() {
        return server;
    }

    public boolean isThreadRunning() {
        return ThreadRunning;
    }

    public void writeLog() {
        LogFile.getInstance().log("Server at {0}:{1}", this.server.getInetAddress(), this.server.getLocalPort());
        LogFile.getInstance().log(this.toString());
    }
}
