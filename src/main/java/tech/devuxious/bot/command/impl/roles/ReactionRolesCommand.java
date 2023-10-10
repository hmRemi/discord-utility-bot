package tech.devuxious.bot.command.impl.roles;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import tech.devuxious.bot.util.embed.BotEmbedBuilder;

import java.awt.*;
import java.time.Instant;
import java.util.List;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */
public class ReactionRolesCommand extends SlashCommand {

    public ReactionRolesCommand() {
        this.name = "reactionroles";
        this.help = "Send the reaction roles embed";
        this.guildOnly = true;

        java.util.List<OptionData> options = List.of(
                new OptionData(OptionType.STRING, "type", "which embed to send", true)
        );
        this.options = options;

        this.userPermissions = new Permission[] { Permission.ADMINISTRATOR };
        this.userMissingPermMessage = "You are missing the `ADMINISTRATOR` permission required to execute this command.";
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        String type = slashCommandEvent.getOption("type").getAsString();

        switch (type) {
            case "gender":
                slashCommandEvent.getChannel().sendMessage(getGenderEmbed()).queue();
                break;
        }

    }

    public MessageCreateData getGenderEmbed() {
        return new BotEmbedBuilder()
                .setTitle("Select your gender")
                .setDescription("Please click the buttons below to receive the roles you want.")
                .setImage("https://cdn.discordapp.com/attachments/1140382016295149730/1141048428353294436/wCsYPzMWHRipAAAAABJRU5ErkJggg.png")
                .setColor(new Color(0, 0, 0))
                .addButton(ButtonStyle.DANGER, "reaction-female", "Female", Emoji.fromUnicode("U+2640"))
                .addButton(ButtonStyle.DANGER, "reaction-male", "Male", Emoji.fromUnicode("U+2642"))
                .build();
    }
}
