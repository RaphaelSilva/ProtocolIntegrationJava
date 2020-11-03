package br.com.pij.data;

import br.com.pij.imp.LogFile;

public class FrameDataAck extends FrameData {
    private byte[] dataResponse = new byte[]{(byte) 0x0A, (byte) 0x05, (byte) 0x28, (byte) 0x0D};

    @Override
    public boolean Process() {
        return true;
    }

    @Override
    public byte[] getResponse() {
        LogFile.getInstance().log(this.getClass().getName(), dataResponse);
        return dataResponse;
    }
}
