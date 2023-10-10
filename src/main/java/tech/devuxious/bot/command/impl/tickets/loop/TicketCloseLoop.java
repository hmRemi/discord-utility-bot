package tech.devuxious.bot.command.impl.tickets.loop;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */
@SuppressWarnings({"SpellCheckingInspection"})
public class TicketCloseLoop {

    private @Getter GuildReadyEvent event;

    public TicketCloseLoop(GuildReadyEvent event) {
        this.event = event;
        run();
    }

    public void run() {
        new Thread(() -> {
            Category category = event.getGuild().getCategoriesByName("Tickets", true).get(0);
            category.getChannels().forEach(guildChannel -> {
                if (guildChannel.getName().contains("support-") || guildChannel.getName().contains("order-") || guildChannel.getName().contains("application-")) {
                    Message message = Objects.requireNonNull(guildChannel.getGuild().getTextChannelById(guildChannel.getId())).getIterableHistory().getLast();
                    long difference = calculateTimeDifference(message.getTimeCreated().toInstant());
                    if (difference >= 259200) guildChannel.delete().queue();
                }
            });
        }, "TicketCloseThread").start();
    }

    public long calculateTimeDifference(Instant instant) {
        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(Instant.now().getEpochSecond() - instant.getEpochSecond());
        return localDateTime.until(localDateTime, java.time.temporal.ChronoUnit.SECONDS);
    }
}
