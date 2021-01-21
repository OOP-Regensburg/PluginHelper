package main.java.de.ur.mipluginhelper.logger;

import main.java.de.ur.mipluginhelper.User.User;
import main.java.de.ur.mipluginhelper.com.UploadClient;
import main.java.de.ur.mipluginhelper.com.UploadClientListener;
import main.java.de.ur.mipluginhelper.config.PluginConfiguration;

import java.io.File;
import java.util.UUID;

public class LogManager {

    public static Log createLog(String experiment) {
        String id = UUID.randomUUID().toString();
        return getLog(id, experiment);
    }

    public static Log openLog(String id, String experiment) {
        return getLog(id, experiment);
    }

    public static void syncLog(Log log, User user, String serverURL, SyncProgressListener listener) {
        UploadClient client = new UploadClient(serverURL);
        client.uploadFile(log.getLogFile(), log.getExperimentName(), user.getID(), user.getSessionID(), new UploadClientListener() {
            @Override
            public void onUploadFinished() {
                listener.onFinished();
            }

            @Override
            public void onUploadFailed() {
                listener.onFailed();
            }
        });
    }

    // TODO: Remove experiment parameter from method
    private static Log getLog(String id, String experiment) {
        File logPath = getLogPath();
        File logFile = new File(logPath, id + ".log");
        File dataFile = new File(logPath, id + ".data");
        return new Log(id, experiment, logFile, dataFile);
    }

    private static File getLogPath() {
        File userDir = new File(System.getProperty("user.home"));
        File dataPath = new File(userDir, PluginConfiguration.DEFAULT_DATA_FOLDER);
        if (!dataPath.exists()) {
            dataPath.mkdir();
        }
        return dataPath;
    }


}
