package br.com.pij.imp;

import br.com.pij.IServer;
import br.com.pij.ServerProtocol;

import java.io.IOException;
import java.net.ServerSocket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server implements IServer, Runnable {
    private ServerSocket server;
    private int port;
    private boolean ThreadControl = false;
    private boolean ThreadRunning = false;
    private ExecutorService pool;

    public Server(int port) {
        this.port = port;
    }

    public void init() {
        this.ThreadControl = true;
        pool = Executors.newFixedThreadPool(5);
        pool.execute(new Thread(this, "Thread.App.Server"));
    }

    public void StopServer() {
        this.ThreadControl = false;
        try {
            if (this.server != null) {
                this.server.close();
            }
        } catch (IOException e) {
            LogFile.getInstance().log(e);
        }
        pool.shutdownNow();
        try {
            pool.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LogFile.getInstance().log(e);
        }
        this.ThreadRunning = !pool.isTerminated();
    }

    @Override
    public void run() {
        try (var server = new ServerSocket(this.port)) {
            this.server = server;
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
