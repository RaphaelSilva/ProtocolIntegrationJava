package br.com.pij.data;

import java.io.InputStream;

public abstract class FrameData {
    private int byteLength;
    private byte[] data;
    private int frame;

    public static FrameData factory(int byteLength, int frame) {
        FrameData fm = null;
        switch (frame) {
            case 0xA1:
                fm = new FrameDataMessage();
                break;
            case 0xA2:
                fm = new FrameDataInfo();
                break;
            case 0xA3:
                fm = new FrameDataTime();
                break;
            default:
                fm = new FrameDataAck();
        }
        fm.frame = frame;
        fm.byteLength = byteLength;
        return fm;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void checkCrc(int crc) {
    }

    public byte[] getResponse() {
        return new byte[]{(byte) 0x0A, (byte) 0x05, (byte) 0x28, (byte) 0x0D};
    }

    public abstract boolean Save();
}
