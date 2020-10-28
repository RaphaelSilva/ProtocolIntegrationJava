package br.com.pij.imp;

import junit.framework.TestCase;

public class ServerTest extends TestCase {

    public Server server;
    public int port = 8484;

    public void setUp() throws Exception {
        server = new Server(this.port);
    }

    public void tearDown() throws Exception {
    }

    public void testFlow() throws InterruptedException {
        assertNotNull(server);
        server.init();
        Thread.sleep(10000);
        assertTrue(server.isThreadRunning());
        Thread.sleep(10000);
        Thread.sleep(10000);
        server.StopServer();
        assertFalse(server.isThreadRunning());
    }
}