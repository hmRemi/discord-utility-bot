package tech.devuxious.bot.command.impl;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import tech.devuxious.bot.util.embed.BotEmbedBuilder;

import java.awt.*;
import java.time.Instant;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */
public class VerificationCommand extends SlashCommand {

    public VerificationCommand() {
        this.name = "verify";
        this.help = "Send the verification embed";
        this.guildOnly = true;

        this.userPermissions = new Permission[] { Permission.ADMINISTRATOR };
        this.userMissingPermMessage = "You are missing the `ADMINISTRATOR` permission required to execute this command.";
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        TextChannel verification = slashCommandEvent.getTextChannel();

        if (verification != null) {
            verification.sendMessage(getVerificationEmbed()).queue();
        }
    }

    public static MessageCreateData getVerificationEmbed() {
        return new BotEmbedBuilder()
                .setTitle(":key: Verification :key:")
                .setDescription(
                        "Welcome to our discord server! \n\n" +
                                "\n - If the verification does not work, please get in contact with an online <@&1149299557134504059> or above for manual verification."
                )
                .setImage("https://cdn.discordapp.com/attachments/1140382016295149730/1141048428353294436/wCsYPzMWHRipAAAAABJRU5ErkJggg.png")
                .setTimeStamp(Instant.now())
                .setColor(new Color(0, 0, 0))
                .addButton(ButtonStyle.LINK, "https://restorecord.com/verify/", " ", Emoji.fromUnicode("U+2764"))
                .build();
    }
}
