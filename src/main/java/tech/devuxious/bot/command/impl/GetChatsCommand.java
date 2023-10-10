package tech.devuxious.bot.command.impl;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/12/2023
 */
public class GetChatsCommand extends SlashCommand {

    public GetChatsCommand() {
        this.name = "getchats";
        this.help = "Log all chats with a specific user";
        this.guildOnly = true;

        List<OptionData> options = List.of(
                new OptionData(OptionType.USER, "user", "User to log chats with", true)
        );
        this.options = options;

        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
        this.userMissingPermMessage = "You are missing the `ADMINISTRATOR` permission required to execute this command.";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        User mentionedUser = event.getOption("user").getAsUser();
        PrivateChannel privateChannel = mentionedUser.openPrivateChannel().complete();
        List<Message> messages = privateChannel.getHistory().retrievePast(100).complete();

        List<String> messageChunks = new ArrayList<>();
        StringBuilder currentChunk = new StringBuilder();

        for (Message message : messages) {
            if (message.getAuthor().getAsTag().equals(mentionedUser.getAsTag())) {
                currentChunk.append("**" + message.getAuthor().getAsTag()).append("** - ").append(message.getContentRaw()).append("\n");
            } else {
                currentChunk.append(message.getAuthor().getAsTag()).append(" - ").append(message.getContentRaw()).append("\n");
            }
            if (currentChunk.toString().split("\n").length >= 10) {
                messageChunks.add(currentChunk.toString());
                currentChunk = new StringBuilder();
            }
        }

        if (!currentChunk.toString().isEmpty()) {
            messageChunks.add(currentChunk.toString());
        }

        sendChunks(event.getUser(), mentionedUser.getAsTag(), messageChunks);
        event.reply("Messages have been sent to your DMs!").setEphemeral(true).queue();
    }

    private void sendChunks(User senderUser, String mentionedUserTag, List<String> chunks) {
        if (chunks.isEmpty()) {
            senderUser.openPrivateChannel().queue(channel -> channel.sendMessage("No messages found in DM with " + mentionedUserTag).queue());
            return;
        }

        for (String chunk : chunks) {
            senderUser.openPrivateChannel().queue(channel -> channel.sendMessage(chunk).queue());
        }
    }
}