package tech.devuxious.bot.command.impl.tickets;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
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
@SuppressWarnings({"ConstantConditions", "SpellCheckingInspection"})
public class TicketCommand extends SlashCommand {

    public TicketCommand() {
        this.name = "ticket";
        this.help = "Manage tickets because that is very cool.";
        this.guildOnly = true;

        this.userPermissions = new Permission[] { Permission.ADMINISTRATOR };
        this.userMissingPermMessage = "You are missing the `ADMINISTRATOR` permission required to execute this command.";
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        slashCommandEvent.getChannel().sendMessage(getTicketEmbed()).queue();
    }

    public static MessageCreateData getTicketEmbed() {
        return new BotEmbedBuilder()
                .setTitle("Tickets")
                .setDescription("If you require assistance, wish to submit a job application, or would like to place an order, kindly initiate a ticket.")
                .setTimeStamp(Instant.now())
                .setColor(new Color(0, 0, 0))
                .addButton(ButtonStyle.DANGER, "support", "Support", Emoji.fromUnicode("U+270B"))
                .addButton(ButtonStyle.DANGER, "order", "Order", Emoji.fromUnicode("U+1F6D2"))
                .addButton(ButtonStyle.DANGER, "application", "Applications", Emoji.fromUnicode("U+1F4DD"))
                .setImage("https://media.discordapp.net/attachments/1143901621231833140/1149374015027294228/standard_1.gif")
                .build();
    }
}
