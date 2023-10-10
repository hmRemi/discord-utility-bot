package tech.devuxious.bot.launch;

import lombok.Getter;
import tech.devuxious.bot.Bot;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */

@Getter
public class Launch {

    /**
     * The main entry point of the Bot application.
     * Initializes the launch, sets up JDA, and registers event listeners.
     * Starts the launch and logs its version upon successful startup.
     * Exits the application with an error code if the launch's token is invalid.
     * @param args Command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        new Bot().init();
    }
}
