package tech.devuxious.bot.command.impl;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import tech.devuxious.bot.Bot;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */
public class VersionCommand extends SlashCommand {

    public VersionCommand() {
        this.name = "version";
        this.help = "Shows the current version of the Discord Bot.";
        this.guildOnly = false;
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        slashCommandEvent.reply("The current version of the Discord Bot is v" + Bot.getVersion()).queue();
    }
}
