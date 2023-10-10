package tech.devuxious.bot.command.impl;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import tech.devuxious.bot.util.embed.BotEmbedBuilder;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */
public class PurgeCommand extends SlashCommand {

    public PurgeCommand() {
        this.name = "purge";
        this.help = "Purge x amount of messages from the chat";
        this.guildOnly = true;

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "amount", "Amount of messages to purge", true));
        this.options = options;

        this.userPermissions = new Permission[] { Permission.MESSAGE_MANAGE };
        this.userMissingPermMessage = "You are missing the `MESSAGE_MANAGE`` permission required to execute this command";

    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        int amount = slashCommandEvent.getOption("amount").getAsInt();

        if (amount <= 0 || amount > 100) {
            slashCommandEvent.reply("Invalid amount. Please provide a value between 1 and 100.").setEphemeral(true).queue();
            return;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(descriptions.length);

        slashCommandEvent.deferReply().queue(reply -> {
            slashCommandEvent.getTextChannel().getHistory().retrievePast(amount + 1).queue(messages -> {
                messages.remove(0);
                slashCommandEvent.getChannel().purgeMessages(messages);
                slashCommandEvent.getChannel().sendMessage(getPurgedEmbed(messages.size(), randomIndex)).queue();
            });
        });

    }

    private final String[] descriptions = {
            "Well, well, well... looks like a mischievous moderator decided to flex their power and give the chat a makeover! " +
                    "Yep, you guessed it, poof went a grand total of **%d** messages",

            "Hold onto your pixels, folks! Our fearless moderator just gave the chat a makeover with a dazzling **%d** messages swept away like confetti!",

            "Brace yourselves, it's like a digital tornado just breezed through! A daring mod unleashed their powers and wiped **%d** messages off the face of the chat.",

            "Surprise! It's a magical disappearing act, brought to you by our very own moderator. Watch **%d** messages vanish before your eyes, like they were never there!"
    };


    public MessageCreateData getPurgedEmbed(int amount, int descriptionIndex) {
        String description = String.format(descriptions[descriptionIndex], amount);

        return new BotEmbedBuilder()
                .setTitle("Moderation")
                .setDescription(description)
                .setImage("https://media.discordapp.net/attachments/1138582327656722658/1138801197709529088/image_1.png")
                .setColor(new Color(0, 0, 0))
                .setTimeStamp(Instant.now())
                .build();
    }

}
