package br.com.pij.imp;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogFile {
    private static LogFile logFile;

    public static LogFile getInstance() {
        if (logFile == null) {
            logFile = makeInstance(LogFile.class.getName());
        }
        return logFile;
    }

    public static LogFile makeInstance(String logName) {
        logFile = new LogFile(logName);
        return logFile;
    }

    private final Logger logger;
    private FileHandler fh;

    private LogFile(String logName) {
        logger = Logger.getLogger(logName);
        try {
            fh = new FileHandler(System.getProperty("user.home") + File.separator +logName+".txt");
        } catch (IOException e) {
            log(e);
        }
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        logger.addHandler(fh);
        //logger.setUseParentHandlers(false);
    }

    public static void setLevel(Level level) {
        if(logFile != null){
            logFile.logger.setLevel(level);
        }
    }

    public void log(String msg) {
        logger.info(msg);
    }

    public void log(String format, Object... args) {
        log(String.format(format, args));
    }

    public void log(String text, byte[] bytes) {
        StringBuilder concatenated = new StringBuilder();
        concatenated.append(text);
        concatenated.append("{");
        for (var b : bytes) {
            concatenated.append(String.format(" 0x%x", b));
        }
        concatenated.append(" }");
        logger.info(concatenated.toString());
    }

    public void log(Throwable e) {
        logger.warning(Arrays.toString(e.getStackTrace()));
    }

}
