package tech.devuxious.bot.command;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import com.jagrosh.jdautilities.command.SlashCommand;
import lombok.Getter;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import tech.devuxious.bot.Bot;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */
public class CommandManager {

    @Getter
    public CommandClient client;

    public CommandManager() {
        List<SlashCommand> commands = scanCommands("tech.devuxious.bot.command.impl");

        client = new CommandClientBuilder()
                .setOwnerId(Bot.getConfiguration().getBotOwner())
                .setStatus(OnlineStatus.valueOf(Bot.getConfiguration().getBotStatus()))
                .setActivity(Activity.playing(Bot.getConfiguration().getBotGameActivity()))
                .addSlashCommands(commands.toArray(new SlashCommand[0]))
                .build();
    }

    private List<SlashCommand> scanCommands(String packageName) {
        List<SlashCommand> commands = new ArrayList<>();

        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends SlashCommand>> commandClasses = reflections.getSubTypesOf(SlashCommand.class);

        for (Class<? extends SlashCommand> commandClass : commandClasses) {
            try {
                commands.add(commandClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return commands;
    }
}
