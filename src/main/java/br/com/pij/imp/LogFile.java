package br.com.pij.imp;

public class LogFile {
    private static LogFile logFile;

    public static LogFile getInstance(){
        if(logFile != null){
            logFile = new LogFile();
        }
        return logFile;
    }

    private LogFile(){

    }

    public void log(String msg){
        System.out.println(msg);
    }

    public void log(String format, Object... args){
        log(String.format(format, args));
    }

    public void log(Throwable e){
        log(e.getMessage());
        throw new RuntimeException(e);
    }
}
