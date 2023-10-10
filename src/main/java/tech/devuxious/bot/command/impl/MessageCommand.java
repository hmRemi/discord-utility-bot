package tech.devuxious.bot.command.impl;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/11/2023
 */
public class MessageCommand extends SlashCommand {

    public MessageCommand() {
        this.name = "message";
        this.help = "Send a custom message to a user";
        this.guildOnly = true;

        List<OptionData> options = List.of(
                new OptionData(OptionType.USER, "user", "User to send the message to", true),
                new OptionData(OptionType.STRING, "message", "Message to send to the user", true)
        );
        this.options = options;

        this.userPermissions = new Permission[] { Permission.ADMINISTRATOR };
        this.userMissingPermMessage = "You are missing the `ADMINISTRATOR` permission required to execute this command.";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        User user = event.getOption("user").getAsUser();
        String messageContent = event.getOption("message").getAsString();

        try {
            user.openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(messageContent).queue(
                        success -> event.reply("Message sent successfully!").setEphemeral(true).queue(),
                        error -> event.reply("Failed to send the message. Please try again later.").setEphemeral(true).queue()
                );
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}