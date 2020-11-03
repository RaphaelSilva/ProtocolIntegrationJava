package br.com.pij.data;

import br.com.pij.util.CRC8;
import br.com.pij.util.MByte;

import java.util.Arrays;

public abstract class FrameData {
    public static final int MESSAGE = 0xA1;
    public static final int INFO = 0xA2;
    public static final int DATETIME = 0xA3;
    public static final int ACK = 0xA0;
    private int byteLength;
    private int frame;
    private byte[] data;
    private int crc;

    public static FrameData factory(int byteLength, int frame) {
        new FrameDataAck();
        FrameData fm = switch (frame) {
            case MESSAGE -> new FrameDataMessage();
            case INFO -> new FrameDataInfo();
            case DATETIME -> new FrameDateTime();
            default -> new FrameDataAck();
        };
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

    public int getFrame() {
        return frame;
    }

    public boolean checkCrc(int crc) {
        this.crc = crc;
        CRC8 crc8 = new CRC8();
        crc8.update(byteLength);
        crc8.update(frame);
        crc8.update(data);
        var d = crc8.getValue();
        crc8.reset();
        byte[] bytes_frame_data = MByte.concatAll(new byte[]{(byte) byteLength, (byte) frame}, data);
        crc8.update(bytes_frame_data);
        System.out.println(crc == crc8.getValue());
        return true;
    }

    public byte[] getResponse() {
        var ack = new FrameDataAck();
        return ack.getResponse();
    }

    public abstract boolean Process();

    @Override
    public String toString() {
        return "FrameData{" +
                "byteLength=" + byteLength +
                ", data=" + Arrays.toString(data) +
                ", frame=" + frame +
                ", crc=" + crc +
                '}';
    }
}
