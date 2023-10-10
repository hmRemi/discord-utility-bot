package tech.devuxious.bot.command.impl;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

/**
 * @author remig
 * @project Bot
 * @date 9/8/2023
 */
public class EmbedCommand extends SlashCommand {

    public EmbedCommand() {
        this.name = "embed";
        this.guildOnly = false;

        this.userPermissions = new Permission[] { Permission.ADMINISTRATOR };
        this.userMissingPermMessage = "You are missing the `ADMINISTRATOR`` permission required to execute this command";
    }

    @Override
    protected void execute(SlashCommandEvent p_slashCommandEvent) {
        TextInput channelID = TextInput.create("channelId", "ID of desired channel", TextInputStyle.SHORT)
                .setPlaceholder("1149298236541108254")
                .setValue("1149298236541108254")
                .setRequired(true)
                .build();
        TextInput title = TextInput.create("title", "Title of embed", TextInputStyle.SHORT)
                .setPlaceholder("This is a title")
                .setRequired(true)
                .build();
        TextInput description = TextInput.create("description", "Description of embed", TextInputStyle.PARAGRAPH)
                .setPlaceholder("This is a description")
                .setRequired(true)
                .build();
        TextInput color = TextInput.create("color", "Color of embed", TextInputStyle.SHORT)
                .setPlaceholder("#FF4670")
                .setValue("#FF4670")
                .setRequired(true)
                .build();
        TextInput image = TextInput.create("image", "Image of embed", TextInputStyle.PARAGRAPH)
                .setPlaceholder("This a image url")
                .setValue("https://media.discordapp.net/attachments/1143901621231833140/1149374015027294228/standard_1.gif")
                .setRequired(false)
                .build();

        Modal modal = Modal.create("custom_embed", "Custom Embed")
                .addComponents(ActionRow.of(channelID), ActionRow.of(title), ActionRow.of(description), ActionRow.of(color), ActionRow.of(image))
                .build();
        p_slashCommandEvent.replyModal(modal).queue();
    }
}
