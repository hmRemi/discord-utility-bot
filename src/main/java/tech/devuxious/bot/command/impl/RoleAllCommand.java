package tech.devuxious.bot.command.impl;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */
public class RoleAllCommand extends SlashCommand {

    public RoleAllCommand() {
        this.name = "roleall";
        this.help = "Give everyone a specific role";
        this.guildOnly = true;

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.ROLE, "role", "Role to grant", true));
        this.options = options;

        this.userPermissions = new Permission[] { Permission.ADMINISTRATOR };
        this.userMissingPermMessage = "You are missing the `ADMINISTRATOR`` permission required to execute this command";

    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        Role role = slashCommandEvent.getOption("role").getAsRole();

        if (slashCommandEvent.getGuild().getMembers().stream().filter(member -> !member.getRoles().contains(role)).toList().isEmpty()) {
            slashCommandEvent.reply("Everyone already has the role " + role.getName()).queue();
            return;
        }

       for (Member member : slashCommandEvent.getGuild().getMembers().stream().filter(member -> !member.getRoles().contains(role)).toList()) {
           slashCommandEvent.getChannel().sendMessage("Giving " + member.getAsMention() + " the role " + role.getName()).queue();
          slashCommandEvent.getGuild().addRoleToMember(member, role).queue();
       }

    }
}
