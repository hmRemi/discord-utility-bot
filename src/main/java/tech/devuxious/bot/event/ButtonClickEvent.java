package tech.devuxious.bot.event;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.devuxious.bot.Bot;
import tech.devuxious.bot.command.impl.tickets.enums.TICKET_TYPE;
import tech.devuxious.bot.command.impl.tickets.manager.TicketManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */
public class ButtonClickEvent extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String buttonValue = event.getComponentId();
        Member member = event.getMember();

        switch (buttonValue) {
            case "application" -> {
                new TicketManager(event).createTicket(TICKET_TYPE.APPLICATION);
            }
            case "support" -> {
                new TicketManager(event).createTicket(TICKET_TYPE.SUPPORT);
            }
            case "order" -> {
                new TicketManager(event).createTicket(TICKET_TYPE.ORDER);
            }
            case "close" -> {
                new TicketManager(event).closeTicket();
            }
            case "confirm" -> {
                if (!event.getChannel().getName().contains("closed")) {
                    new TicketManager(event).closeTicketConfirm();
                }
            }
            case "deletetranscript" -> {
                event.getMessage().delete().queue();
            }
            case "claim" -> {
                new TicketManager(event).claimTicket();
            }
            case "verify" -> {
                if (Bot.getConfiguration().getVerifyRoleID() == null) {
                    event.getHook().sendMessage("Verification role is not setup").setEphemeral(true).queue();
                    return;
                }
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(Bot.getConfiguration().getVerifyRoleID())).queue();
                event.reply("You have been verified!").setEphemeral(true).queue();
            }
        }
    }
}
