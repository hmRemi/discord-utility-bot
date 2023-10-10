package tech.devuxious.bot.event;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/12/2023
 */
public class DMListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE) && !event.getAuthor().isBot()) {
            System.out.println("Received DM from " + event.getAuthor().getAsTag() + ": " + event.getMessage().getContentDisplay());
        }
    }
}
