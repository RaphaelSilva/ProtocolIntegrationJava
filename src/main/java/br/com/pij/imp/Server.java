package br.com.pij.imp;

import br.com.pij.IServerProtocol;
import br.com.pij.IServer;
import br.com.pij.ServerProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

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
        this.thread = new Thread(this, "Thread.App.Server");
        this.thread.start();
    }

    public void StopServer() {
        this.ThreadControl = false;
        // this.thread.interrupt();
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
        var pool = Executors.newFixedThreadPool(5);
        try {
            this.server = new ServerSocket(this.port);
            LogFile.getInstance().log("Server::ServerSocket on %d", this.port);
            LogFile.getInstance().log("Server::Server at %s:%s", this.server.getInetAddress(), this.server.getLocalPort());
            LogFile.getInstance().log("Server::%s", this.toString());
            while (this.ThreadControl) {
                this.ThreadRunning = true;
                var client = this.server.accept();
                pool.execute(ServerProtocol.factory(client));
            }
            this.server.close();
            this.ThreadRunning = false;
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
}
