package tech.devuxious.bot.util.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import javax.annotation.Nullable;
import java.awt.*;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */
@SuppressWarnings("unused")
public class BotEmbedBuilder {

    private final MessageCreateBuilder messageCreateBuilder;
    private final EmbedBuilder embedBuilder;
    private final ArrayList<Button> buttons = new ArrayList<>();

    public BotEmbedBuilder() {
        messageCreateBuilder = new MessageCreateBuilder();
        embedBuilder = new EmbedBuilder();
    }

    public BotEmbedBuilder setTitle(@Nullable String title) {
        embedBuilder.setTitle(title);
        return this;
    }

    public BotEmbedBuilder setDescription(@Nullable CharSequence description) {
        embedBuilder.setDescription(description);
        return this;
    }

    public BotEmbedBuilder setFooter(@Nullable String footer) {
        embedBuilder.setFooter(footer);
        return this;
    }

    public BotEmbedBuilder setFooter(@Nullable String footer, @Nullable String imageUrl) {
        embedBuilder.setFooter(footer);
        return this;
    }

    public BotEmbedBuilder setImage(@Nullable String image) {
        embedBuilder.setImage(image);
        return this;
    }

    public BotEmbedBuilder setThumbnail(@Nullable String image) {
        embedBuilder.setThumbnail(image);
        return this;
    }


    public BotEmbedBuilder setColor(Color color) {
        embedBuilder.setColor(color);
        return this;
    }

    public BotEmbedBuilder setColor(int color) {
        embedBuilder.setColor(color);
        return this;
    }

    public BotEmbedBuilder setTimeStamp(TemporalAccessor temporalAccessor) {
        embedBuilder.setTimestamp(temporalAccessor);
        return this;
    }

    public BotEmbedBuilder addField(String name, String value, boolean inline) {
        embedBuilder.addField(name, value, inline);
        return this;
    }

    public BotEmbedBuilder addButton(ButtonStyle buttonStyle, String id, String label, @Nullable Emoji emoji) {
        switch (buttonStyle) {
            case PRIMARY -> buttons.add(Button.primary(id, label).withEmoji(emoji));
            case SECONDARY -> buttons.add(Button.secondary(id, label).withEmoji(emoji));
            case SUCCESS -> buttons.add(Button.success(id, label).withEmoji(emoji));
            case DANGER -> buttons.add(Button.danger(id, label).withEmoji(emoji));
            case LINK -> buttons.add(Button.link(id, label).withEmoji(emoji));
        }
        return this;
    }

    public BotEmbedBuilder addDisabledButton(ButtonStyle buttonStyle, String id, String label, @Nullable Emoji emoji) {
        switch (buttonStyle) {
            case PRIMARY -> buttons.add(Button.primary(id, label).withEmoji(emoji).asDisabled());
            case SECONDARY -> buttons.add(Button.secondary(id, label).withEmoji(emoji).asDisabled());
            case SUCCESS -> buttons.add(Button.success(id, label).withEmoji(emoji).asDisabled());
            case DANGER -> buttons.add(Button.danger(id, label).withEmoji(emoji).asDisabled());
        }
        return this;
    }

    public BotEmbedBuilder addLinkButton(String url, String label, @Nullable Emoji emoji) {
        buttons.add(Button.link(url, label).withEmoji(emoji));
        return this;
    }

    public BotEmbedBuilder addFile(FileUpload fileUpload) {
        messageCreateBuilder.addFiles(fileUpload);
        return this;
    }

    public MessageCreateData build() {
        messageCreateBuilder.setEmbeds(embedBuilder.build());
        if (buttons.size() > 0) messageCreateBuilder.setActionRow(buttons);
        return messageCreateBuilder.build();
    }
}
