package br.com.pij.imp;

import br.com.pij.ServerProtocol;
import br.com.pij.data.FrameData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class FrameDataProtocol extends ServerProtocol {

    private enum State {
        INIT, BYTES_FRAME, DATA, CRC, END, CLOSE
    }

    private int init;
    private State CurrentState;
    private int byteLength;

    public FrameDataProtocol(Socket client) {
        super(client);
    }

    @Override
    public void Decode(InputStream in, OutputStream out) throws IOException {
        this.CurrentState = State.INIT;
        FrameData frameData = null;
        do switch (this.CurrentState) {
            case INIT -> {
                do {
                    init = in.read();
                }
                while (init != 0x0A && init != -1);
                if (init == 0x0A) {
                    CurrentState = State.BYTES_FRAME;
                } else {
                    CurrentState = State.CLOSE;
                    return;
                }
            }
            case BYTES_FRAME -> {
                byteLength = in.read();
                frameData = FrameData.factory(byteLength, in.read());
                CurrentState = State.DATA;
            }
            case DATA -> {
                frameData.setData(in.readNBytes(byteLength - 4));
                CurrentState = State.CRC;
            }
            case CRC -> {
                frameData.checkCrc(in.read());
                CurrentState = State.END;
            }
            case END -> {
                var r = in.read();
                if (r == 0x0D) {
                    frameData.Process();
                    var pout = new PrintStream(out, true);
                    var dataResp = frameData.getResponse();

                    LogFile.getInstance().log("Response to client ", dataResp);

                    pout.write(dataResp);
                    CurrentState = State.INIT;
                } else {
                    CurrentState = State.CLOSE;
                    throw new IOException("Expected END!");
                }
            }
        } while (true);
    }
}
