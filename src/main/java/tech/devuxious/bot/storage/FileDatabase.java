package tech.devuxious.bot.storage;

import lombok.Getter;
import lombok.SneakyThrows;
import tech.devuxious.bot.util.JSONFormatter;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileDatabase {

    private final @Getter File databaseFile = new File("database.json");
    private JSONObject databaseObject;


    /**
     * Constructor for the FileDatabase class.
     * Initializes the database by creating the file if it does not exist.
     * Loads the database from the file using JSONTokener.
     * Logs the successful initialization.
     */
    @SneakyThrows
    public FileDatabase() {
        if (!databaseFile.exists()) {
            databaseFile.createNewFile();
            databaseObject = new JSONObject();
            loadDatabase();
            System.out.println("Created the database file and efficiently stored data in the file.");
        }
        databaseObject = new JSONObject(new JSONTokener(new FileReader(databaseFile)));
    }

    /**
     * Loads the current database object into the file
     * Uses FileWriter to write JSON Data in a formatted way.
     */
    @SneakyThrows
    public void loadDatabase() {
        FileWriter writer = new FileWriter(databaseFile);
        writer.write(JSONFormatter.format(databaseObject));
        writer.flush();
        writer.close();
    }

    /**
     * Gets the current database object
     * Before returning, it loads the latest data from the file.
     *
     * @return The JSONObject representing the current database
     */
    public JSONObject getDatabaseObject() {
        loadDatabase();
        return databaseObject;
    }
}
