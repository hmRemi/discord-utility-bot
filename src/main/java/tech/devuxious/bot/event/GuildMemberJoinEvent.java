package tech.devuxious.bot.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import tech.devuxious.bot.Bot;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */
public class GuildMemberJoinEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent event) {
        super.onGuildMemberJoin(event);

        // Get the newly joined member
        Member member = event.getMember();

        // Get the guild the member joined
        Guild guild = event.getGuild();

        // Add the member to the role
        guild.addRoleToMember(member, event.getGuild().getRoleById("1160389999712280697")).queue();

        // Retrieve the channel by its configuration value
        TextChannel channel = guild.getJDA().getTextChannelById(Bot.getConfiguration().getWelcomeChannelID());

        if (channel != null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Welcome " + member.getEffectiveName() + " (@" + member.getUser().getName() + ") to " + guild.getName())
                    .setDescription("Welcome to **off the grind** discord server!!" +
                            "\n\nA wonderful community for those who enjoy to play and socialize!" +
                            "\n\nWe are now at **" + guild.getMembers().size() + "** members!")
                    .setImage("https://cdn.discordapp.com/attachments/1140382016295149730/1141048428353294436/wCsYPzMWHRipAAAAABJRU5ErkJggg.png")
                    .setColor(new Color(0, 0, 0));

            MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
            messageCreateBuilder.setEmbeds(embed.build());

            channel.sendMessageEmbeds(messageCreateBuilder.getEmbeds()).queue();
            event.getGuild().getTextChannelById("1149765823607603263").sendMessage(member.getAsMention()).queue(p_message -> Objects.requireNonNull(Objects.requireNonNull(p_message)).delete().queueAfter(5, TimeUnit.SECONDS));

        }
    }
}
