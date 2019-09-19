package de.ur.mi.pluginhelper.logger;

import java.io.File;
import java.util.UUID;

public class LogManager {

    private static final String DEFAULT_LOG_FOLDER = ".OOP-Plugin-Logdaten";

    public static Log createNewLog() {
        String id = UUID.randomUUID().toString();
        File logPath = getLogPath();
        File logFile = new File(logPath, id+".log");
        File dataFile = new File(logPath, id+".data");
        return new Log(id, logFile, dataFile);
    }

    public static Log openLog(String id) {
        File logPath = getLogPath();
        File logFile = new File(logPath, id+".log");
        File dataFile = new File(logPath, id+".data");
        if(logFile.exists() && dataFile.exists()) {
            return new Log(id, logFile, dataFile);
        }
        return null;
    }

    private static File getLogPath() {
        File userDir = new File(System.getProperty("user.home"));
        File logPath = new File(userDir, DEFAULT_LOG_FOLDER);
        if(!logPath.exists()) {
            logPath.mkdir();
        }
        return logPath;
    }

}
