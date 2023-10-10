package tech.devuxious.bot.command.impl.roles;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * @author remig
 * @project Discord-Utility-Bot
 * @date 10/8/2023
 */
public class ReactionButtonClickEvent extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String buttonValue = event.getComponentId();
        Member member = event.getMember();

        switch (buttonValue) {
            case "reaction-female" -> {
                Role giveawayRole = member.getGuild().getRoleById("1160390413870432426");

                if (giveawayRole != null && member.getRoles().contains(giveawayRole)) {
                    event.getGuild().removeRoleFromMember(member, event.getGuild().getRoleById("1160390413870432426")).queue();
                    event.reply("You no longer have the <@&1160390413870432426> role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().addRoleToMember(member, event.getGuild().getRoleById("1160390413870432426")).queue();
                    event.reply("You now have the <@&1160390413870432426> role!").setEphemeral(true).queue();
                }
            }
            case "reaction-male" -> {
                Role giveawayRole = member.getGuild().getRoleById("1160390375459012670");

                if (giveawayRole != null && member.getRoles().contains(giveawayRole)) {
                    event.getGuild().removeRoleFromMember(member, event.getGuild().getRoleById("1160390375459012670")).queue();
                    event.reply("You no longer have the <@&1160390375459012670> role!").setEphemeral(true).queue();
                } else {
                    event.getGuild().addRoleToMember(member, event.getGuild().getRoleById("1160390375459012670")).queue();
                    event.reply("You now have the <@&1160390375459012670> role!").setEphemeral(true).queue();
                }
            }
        }
    }
}
