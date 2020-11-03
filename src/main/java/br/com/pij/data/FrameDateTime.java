package br.com.pij.data;

import br.com.pij.imp.LogFile;
import br.com.pij.util.MByte;

import java.util.Calendar;
import java.util.TimeZone;

public class FrameDateTime extends FrameData {

    private String timeZone;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private int second;

    @Override
    public boolean Process() {
        timeZone = new String(this.getData());
        var c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        LogFile.getInstance().log(this.toString());
        return true;
    }

    @Override
    public byte[] getResponse() {
        byte[] dataResponse = MByte.concatAll(
                new byte[]{(byte) 0x0A, (byte) 0x0B, (byte) this.getFrame()},
                new byte[]{(byte) day, (byte) month, (byte) year, (byte) hour, (byte) minute, (byte) second},
                new byte[]{(byte) 0x9C, (byte) 0x0D}
        );
        LogFile.getInstance().log(this.getClass().getName(), dataResponse);
        return dataResponse;
    }

    @Override
    public String toString() {
        return "FrameDataTime{" +
                "timeZone='" + timeZone + '\'' +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                '}';
    }
}
