package de.ur.mi.pluginhelper;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class TaskConfiguration {

    private static final String HANDOUT_URL_KEY = "HANDOUT_URL";
    private static final String HANDOUT_URL_DEFAULT_VALUE = "https://github.com/esolneman/OOP-Helper-Handout-Template";
    private static final String DEFAULT_CONFIGURATION_NAME = ".task";

    private String handoutURL;

    private TaskConfiguration() {
    }

    public void setHandoutURL(String handoutURL) {
        this.handoutURL = handoutURL;
    }

    public String getHandoutURL() {
        return handoutURL;
    }

    public static TaskConfiguration loadFrom() {
        return loadFrom(DEFAULT_CONFIGURATION_NAME);
    }

    public static TaskConfiguration loadFrom(String fileName) {
        File configFile = getConfigurationFile(fileName);
        TaskConfiguration config = createConfiguration(configFile);
        return config;
    }

    private static File getConfigurationFile(String fileName) {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        Path configFilePath = Paths.get(project.getBasePath(), fileName);
        return configFilePath.toFile();
    }

    private static TaskConfiguration createConfiguration(File input) {
        Properties properties = new Properties();
        TaskConfiguration config = new TaskConfiguration();
        try {
            properties.load(new FileInputStream((input)));
            String handoutURL = properties.getProperty(HANDOUT_URL_KEY, HANDOUT_URL_DEFAULT_VALUE);
            config.setHandoutURL(handoutURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    @Override
    public String toString() {
        String out = "TaskConfiguration:\n" +
                "Handout-URL:\t" + this.getHandoutURL();
        return out;
    }
}
