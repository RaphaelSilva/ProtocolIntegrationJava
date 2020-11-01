package br.com.pij.imp;

public class LogFile {
    private static LogFile logFile;

    public static LogFile getInstance() {
        if (logFile == null) {
            logFile = new LogFile();
        }
        return logFile;
    }

    private LogFile() {

    }

    public void log(String msg) {
        System.out.println(this.toString() + "#" + msg);
    }

    public void log(String format, Object... args) {
        log(String.format(format, args));
    }

    public void log(String text, byte[] bytes) {
        var concatened = " ";
        for (var b : bytes) {
            concatened += String.format("[%x]", b);
        }
        log(text + concatened);
    }

    public void log(Throwable e) {
        log(e.getMessage());
        System.err.println(e.getStackTrace());
        //throw new RuntimeException(e);
    }
}
