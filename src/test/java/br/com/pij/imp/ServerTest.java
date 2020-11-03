package br.com.pij.imp;

import br.com.pij.data.FrameData;
import br.com.pij.util.MByte;
import junit.framework.TestCase;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.util.logging.Level;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServerTest extends TestCase {
    static int port = 8880;
    static Server server;

    static {
        LogFile.makeInstance(ServerTest.class.getName());
        LogFile.setLevel(Level.ALL);
        server = new Server(port);
        server.init();
    }

    @Override
    protected void setUp() throws Exception {
        Thread.sleep(1000);
    }

    public void test0GetPort() {
        assertEquals("Port from server was changed", port, server.getPort());
    }

    public void test1Init() {
        assertTrue("Server didn't start", server.isThreadRunning());
    }

    public void test2ATestRun() throws InterruptedException {
        byte[] dataResponse;
        String timeZone = "America/Sao_Paulo";
        var client = new Client("test2ATestRun", "127.0.0.1", port);
        if (client.init()) {
            var data = "Hello World".getBytes();
            var initial = new byte[]{(byte) 0x0A, (byte) (data.length + 4), (byte) FrameData.MESSAGE};
            var last = new byte[]{(byte) 0xDC, (byte) 0x0D};
            client.sendBytes(MByte.concatAll(initial, data, last));
            dataResponse = client.readBytes();
            assertTrue("Response didn't sent", dataResponse != null && dataResponse.length > 0);
            Thread.sleep(1000);
        }
    }

    public void test2BTestRun() throws InterruptedException {
        Thread.sleep(2000);
        byte[] dataResponse;
        String timeZone = "America/Sao_Paulo";
        var client = new Client("test2BTestRun", "127.0.0.1", port);
        if (client.init()) {
            var name = "Raphael Silva";
            var data = MByte.concatAll(new byte[]{(byte) 32, (byte) 75, (byte) 180, (byte) name.length()}, name.getBytes());
            var initial = new byte[]{(byte) 0x0A, (byte) (data.length + 4), (byte) FrameData.INFO};
            var last = new byte[]{(byte) 0xDC, (byte) 0x0D};
            client.sendBytes(MByte.concatAll(initial, data, last));
            dataResponse = client.readBytes();
            assertTrue("Response didn't sent", dataResponse != null && dataResponse.length > 0);
            Thread.sleep(1000);
        }
    }

    public void test2CTestRun() throws InterruptedException {
        Thread.sleep(2000);
        byte[] dataResponse;
        String timeZone = "America/Sao_Paulo";
        var client = new Client("test2CTestRun", "127.0.0.1", port);
        if (client.init()) {
            var data = timeZone.getBytes();
            var initial = new byte[]{(byte) 0x0A, (byte) (data.length + 4), (byte) FrameData.DATETIME};
            var last = new byte[]{(byte) 0xDC, (byte) 0x0D};
            client.sendBytes(MByte.concatAll(initial, data, last));
            dataResponse = client.readBytes();
            assertTrue("Response didn't sent", dataResponse != null && dataResponse.length > 0);
            Thread.sleep(1000);
        }
    }

    public void test3GetServer() throws InterruptedException {
        Thread.sleep(2000);
        var s = server.getServer();
        assertNotNull(s);
    }

    public void test4StopServer() throws InterruptedException {
        Thread.sleep(3000);
        server.StopServer();
        Thread.sleep(1000);
        assertFalse("Server didn't stop", server.isThreadRunning());
    }

}