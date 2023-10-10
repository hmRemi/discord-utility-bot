package tech.devuxious.bot.command.impl.hypixel;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.GuildReply;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.reply.StatusReply;
import tech.devuxious.bot.Bot;
import tech.devuxious.bot.util.embed.BotEmbedBuilder;
import tech.devuxious.bot.util.hypixel.HypixelWrapper;
import tech.devuxious.bot.util.hypixel.RankWrapper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * @author remig
 * @project Bot
 * @date 9/8/2023
 */
public class StatsCommand extends SlashCommand {

    public StatsCommand() {
        this.name = "stats";
        this.guildOnly = false;

        List<OptionData> options = List.of(
                new OptionData(OptionType.STRING, "user", "Enter username", true)
        );
        this.options = options;

        this.userPermissions = new Permission[] { Permission.ADMINISTRATOR };
        this.userMissingPermMessage = "You are missing the `ADMINISTRATOR`` permission required to execute this command";
    }

    @Override
    protected void execute(SlashCommandEvent p_slashCommandEvent) {
        HypixelAPI hypixelAPI = Bot.getHypixelAPI();
        String username = p_slashCommandEvent.getOption("user").getAsString();

        try {
            PlayerReply.Player player = HypixelWrapper.getPlayerByName(hypixelAPI, username, p_slashCommandEvent.getTextChannel());
            GuildReply.Guild guild = hypixelAPI.getGuildByPlayer(player.getUuid()).get().getGuild();

            String rank = player.getHighestRank();
            String guild_name = "No Guild";
            if (guild != null) guild_name = guild.getName();

            double networkLevel = player.getNetworkLevel();
            long karma = player.getKarma();

            boolean online = player.isOnline();
            String onlineStatus = online ? "\uD83D\uDFE2" : "\uD83D\uDD34";

            p_slashCommandEvent.reply(sendEmbed(
            "- **Username:** " + username + "\n" +
                    "- **Rank:** " + "[" + RankWrapper.valueOf(rank).getTranslation() + "] " + "\n" +
                    "- **Guild:** " + guild_name + "\n" +
                    "- **Karma:** " + karma + "\n" +
                    "- **Network Level:** " + Math.round(networkLevel) + "\n" +
                    "- **Online:** " + onlineStatus + "\n", player.getName()))
            .queue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MessageCreateData sendEmbed(String message, String username) {
        return new BotEmbedBuilder()
                .setTitle("Hypixel Stats")
                .setDescription(message)
                .setThumbnail("https://minotar.net/avatar/" + username)
                .setImage("https://cdn.discordapp.com/attachments/1140382016295149730/1141048428353294436/wCsYPzMWHRipAAAAABJRU5ErkJggg.png")
                .setColor(new Color(0, 0, 0))
                .setTimeStamp(Instant.now())
                .build();
    }
}
