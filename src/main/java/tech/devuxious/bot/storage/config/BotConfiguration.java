package tech.devuxious.bot.storage.config;

import lombok.Getter;
import tech.devuxious.bot.util.JSONFormatter;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class BotConfiguration {

    private final File configFile = new File("config.json");
    private @Getter JSONObject config;

    /**
     * Constructor for the FileDatabase class.
     * Initializes the database by creating the file if it does not exist.
     * Loads the database from the file using JSONTokener.
     * Logs the successful initialization.
     */
    public BotConfiguration() {
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
                config = new JSONObject();
                loadConfig(getDefaultConfig());
                System.out.println("Created default configuration file. Please edit the config and then restart.");
                System.exit(0);
            }
            config = new JSONObject(new JSONTokener(new FileReader(configFile)));
            System.out.println("Successfully initialized configuration file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject getDefaultConfig() {
        JSONObject defaultConfig = new JSONObject();
        JSONObject guildSetup = new JSONObject();
        JSONObject settings = new JSONObject();
        JSONObject startup = new JSONObject();

        JSONObject channels = new JSONObject();
        JSONObject roles = new JSONObject();

        startup.put("token", "YOUR_TOKEN");
        startup.put("owner", "YOUR_DISCORD_ID");
        settings.put("startup", startup);
        settings.put("game-activity", "Introducing a one-stop marketplace for all your needs!\n");
        settings.put("status", "IDLE");

        channels.put("transcript-channel-id", "TRANSCRIPT-CHANNEL-ID");
        channels.put("welcome-channel-id", "WELCOME_CHANNEL_ID");

        guildSetup.put("channels", channels);

        roles.put("verify-role", "VERIFY_ROLE_ID");
        guildSetup.put("roles", roles);
        guildSetup.put("ticket-category-id", "TICKET_CATEGORY_ID");

        defaultConfig.put("settings", settings);
        defaultConfig.put("guild-setup", guildSetup);
        return defaultConfig;
    }

    private void loadConfig(JSONObject config) {
        try {
            FileWriter fileWriter = new FileWriter(configFile);
            fileWriter.write(JSONFormatter.format(config));
            fileWriter.flush();
            fileWriter.close();
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    public String getBotToken() {
        if (!config.has("settings") || !config.getJSONObject("settings").has("startup") || !config.getJSONObject("settings").getJSONObject("startup").has("token")) {
            System.out.println("Your configuration is outdated or has been corrupted, the config has been reset.");
            loadConfig(getDefaultConfig());
            System.out.println("Created default config file. Please edit the config after the restart.");
            System.exit(0);
            return null;
        }
        return config.getJSONObject("settings").getJSONObject("startup").getString("token");
    }

    public String getBotOwner() {
        if (!config.has("settings") || !config.getJSONObject("settings").has("startup") || !config.getJSONObject("settings").getJSONObject("startup").has("owner")) {
            System.out.println("Your configuration is outdated or has been corrupted, the config has been reset.");
            loadConfig(getDefaultConfig());
            System.out.println("Created default config file. Please edit the config after the restart.");
            System.exit(0);
            return null;
        }
        return config.getJSONObject("settings").getJSONObject("startup").getString("owner");
    }

    public String getBotGameActivity() {
        if (!config.has("settings") || !config.getJSONObject("settings").has("game-activity")) {
            System.out.println("Your configuration is outdated or has been corrupted, the config has been reset.");
            loadConfig(getDefaultConfig());
            System.out.println("Created default config file. Please edit the config after the restart.");
            System.exit(0);
            return null;
        }
        return config.getJSONObject("settings").getString("game-activity");
    }

    public String getBotStatus() {
        if (!config.has("settings") || !config.getJSONObject("settings").has("status")) {
            System.out.println("Your configuration is outdated or has been corrupted, the config has been reset.");
            loadConfig(getDefaultConfig());
            System.out.println("Created default config file. Please edit the config after the restart.");
            System.exit(0);
            return null;
        }
        return config.getJSONObject("settings").getString("status");
    }

    public String getWelcomeChannelID() {
        if (!config.has("guild-setup") || !config.getJSONObject("guild-setup").has("channels") || !config.getJSONObject("guild-setup").getJSONObject("channels").has("welcome-channel-id")) {
            System.out.println("Your configuration is outdated or has been corrupted, the config has been reset.");
            loadConfig(getDefaultConfig());
            System.out.println("Created default config file. Please edit the config after the restart.");
            System.exit(0);
            return null;
        }
        return config.getJSONObject("guild-setup").getJSONObject("channels").getString("welcome-channel-id");
    }

    public String getTicketCategoryID() {
        if (!config.has("guild-setup") || !config.getJSONObject("guild-setup").has("ticket-category-id")) {
            System.out.println("Your configuration is outdated or has been corrupted, the config has been reset.");
            loadConfig(getDefaultConfig());
            System.out.println("Created default config file. Please edit the config after the restart.");
            System.exit(0);
            return null;
        }
        return config.getJSONObject("guild-setup").getString("ticket-category-id");
    }

    public String getTranscriptChannelID() {
        if (!config.has("guild-setup") || !config.getJSONObject("guild-setup").has("channels") || !config.getJSONObject("guild-setup").getJSONObject("channels").has("transcript-channel-id")) {
            System.out.println("Your configuration is outdated or has been corrupted, the config has been reset.");
            loadConfig(getDefaultConfig());
            System.out.println("Created default config file. Please edit the config after the restart.");
            System.exit(0);
            return null;
        }
        return config.getJSONObject("guild-setup").getJSONObject("channels").getString("transcript-channel-id");
    }

    public String getVerifyRoleID() {
        if (!config.has("guild-setup") || !config.getJSONObject("guild-setup").has("roles") || !config.getJSONObject("guild-setup").getJSONObject("roles").has("verify-role")) {
            System.out.println("Your configuration is outdated or has been corrupted, the config has been reset.");
            loadConfig(getDefaultConfig());
            System.out.println("Created default config file. Please edit the config after the restart.");
            System.exit(0);
            return null;
        }
        return config.getJSONObject("guild-setup").getJSONObject("roles").getString("verify-role");
    }
}
