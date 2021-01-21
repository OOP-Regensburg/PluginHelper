package test.java.de.ur.miplugin.helper.logger;

import main.java.de.ur.mipluginhelper.logger.Log;

import static org.junit.jupiter.api.Assertions.*;

import main.java.de.ur.mipluginhelper.logger.LogData;
import main.java.de.ur.mipluginhelper.logger.LogDataType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

class LogTest {

    private static final String TEST_ID = "ID";
    private static final String TEST_EXPERIMENT = "EXPERIMENT";
    private static final String TEST_FOLDER = "log/";
    private static final String TEST_LOG_FILE_PATH = "log/test.log";
    private static final String TEST_DATA_FILE_PATH = "log/test.data";

    private Log testLog;

    @BeforeEach
    public void prepare() {
        File logFile = new File(TEST_LOG_FILE_PATH);
        File dataFile = new File(TEST_DATA_FILE_PATH);
        File logFolder = new File(TEST_FOLDER);
        logFolder.mkdirs();
        testLog = new Log(TEST_ID, TEST_EXPERIMENT, logFile, dataFile);
    }


    @AfterEach
    public void clear() {
        testLog = null;
        (new File(TEST_LOG_FILE_PATH)).delete();
        (new File(TEST_DATA_FILE_PATH)).delete();
        (new File(TEST_FOLDER)).delete();
    }


    @Test
    public void testFileCreation() {
        assertTrue((new File(TEST_LOG_FILE_PATH)).exists());
        assertTrue((new File(TEST_DATA_FILE_PATH)).exists());
    }

    @Test
    public void testDataFile() throws FileNotFoundException {
        File dataFile = new File(TEST_DATA_FILE_PATH);
        Scanner scanner = new Scanner(dataFile);
        int lineNumber = 0;
        while (scanner.hasNextLine()) {
            lineNumber++;
            scanner.nextLine();
        }
        assertEquals(lineNumber, 5); // Should be five lines in data file
    }

    @Test
    public void testLogging() throws FileNotFoundException {
        File logFile = new File(TEST_LOG_FILE_PATH);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        testLog.log(now, "SESSION_ID", LogDataType.CUSTOM, "LABEL", "PAYLOAD");
        Scanner scanner = new Scanner(logFile);
        ArrayList<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        assertEquals(lines.get(0), LogData.DATA_HEADER);
        assertEquals(lines.get(1), "\"" + now + "\",\"SESSION_ID\",\"CUSTOM\",\"LABEL\",\"PAYLOAD\"");
    }

}