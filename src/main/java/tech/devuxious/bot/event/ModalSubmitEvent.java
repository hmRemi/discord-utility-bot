package tech.devuxious.bot.event;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.devuxious.bot.util.embed.BotEmbedBuilder;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author remig
 * @project Bot
 * @date 9/8/2023
 */
public class ModalSubmitEvent extends ListenerAdapter {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        String modalId = event.getModalId();
        Member member = event.getMember();

        switch (modalId) {
            case "custom_embed" -> {
                String channelId = null;
                if (event.getValue("channelId") != null) {
                    channelId =  event.getValue("channelId").getAsString();
                }

                String title = null;
                if (event.getValue("title") != null) {
                    title =  event.getValue("title").getAsString();
                }

                String description = null;
                if (event.getValue("description") != null) {
                    description =  event.getValue("description").getAsString();
                }

                String colorHex = null;
                if (event.getValue("color") != null) {
                    colorHex =  event.getValue("color").getAsString();
                }
                Color color = hexToColor(colorHex);

                String image = null;
                if (event.getValue("image") != null) {
                    image =  event.getValue("image").getAsString();
                }

                // Create the embed
                BotEmbedBuilder embedBuilder = new BotEmbedBuilder()
                        .setTitle(title)
                        .setDescription(description)
                        .setColor(color);

                assert image != null;
                if(!image.isEmpty()) {
                    embedBuilder.setImage(image);
                }

                TextChannel channel = event.getGuild().getTextChannelById(channelId);
                if(channel != null) {
                    channel.sendMessage(embedBuilder.build()).queue();
                    event.reply("Embed sent!").setEphemeral(true).queue();
                } else {
                    event.reply("Invalid channel ID!").setEphemeral(true).queue();
                }
            }
        }
    }

    public static Color hexToColor(String hex) {
        hex = hex.replace("#", ""); // Remove the "#" symbol if present
        int intValue = Integer.parseInt(hex, 16); // Parse the hexadecimal string to an integer
        return new Color(intValue); // Create a Color object from the integer value
    }
}
