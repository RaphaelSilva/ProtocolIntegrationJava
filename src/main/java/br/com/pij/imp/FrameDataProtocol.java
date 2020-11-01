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

    private State CurrentState;
    private int byteLength;

    public FrameDataProtocol(Socket client) {
        super(client);
    }

    @Override
    public void Decode(InputStream in, OutputStream out) throws IOException {
        this.CurrentState = State.INIT;
        FrameData frameData = null;
        do {
            switch (this.CurrentState) {
                case INIT:
                    var read = in.read();
                    if (read == 0x0A) {
                        CurrentState = State.BYTES_FRAME;
                    } else if (read == -1) {
                        CurrentState = State.CLOSE;
                        return;
                    }
                    break;
                case BYTES_FRAME:
                    byteLength = in.read();
                    frameData = FrameData.factory(byteLength, in.read());
                    CurrentState = State.DATA;
                    break;
                case DATA:
                    frameData.setData(in.readNBytes(byteLength-5));
                    CurrentState = State.CRC;
                    break;
                case CRC:
                    frameData.checkCrc(in.read());
                    CurrentState = State.END;
                    break;
                case END:
                    if(in.read() == 0x0D){
                        var pout = new PrintStream(out, true);
                        pout.write(frameData.getResponse());
                        frameData.Save();
                        CurrentState = State.INIT;
                    } else {
                        CurrentState = State.CLOSE;
                        throw new IOException("Expected END!");
                    }
                    break;

            }
        } while (CurrentState != State.CLOSE);
    }
}
