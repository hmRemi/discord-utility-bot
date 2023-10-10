package tech.devuxious.bot.command.impl.hypixel;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.GuildReply;
import net.hypixel.api.reply.PlayerReply;
import org.json.JSONObject;
import tech.devuxious.bot.Bot;
import tech.devuxious.bot.util.embed.BotEmbedBuilder;
import tech.devuxious.bot.util.hypixel.HypixelWrapper;
import tech.devuxious.bot.util.hypixel.RankWrapper;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.time.Instant;
import java.util.List;

/**
 * @author remig
 * @project Bot
 * @date 9/8/2023
 */
public class GuildCommand extends SlashCommand {

    public GuildCommand() {
        this.name = "guild";
        this.guildOnly = false;

        List<OptionData> options = List.of(
                new OptionData(OptionType.STRING, "guild", "Enter guild", true)
        );
        this.options = options;

        this.userPermissions = new Permission[] { Permission.ADMINISTRATOR };
        this.userMissingPermMessage = "You are missing the `ADMINISTRATOR`` permission required to execute this command";
    }

    @Override
    protected void execute(SlashCommandEvent p_slashCommandEvent) {
        HypixelAPI hypixelAPI = Bot.getHypixelAPI();
        String guild = p_slashCommandEvent.getOption("guild").getAsString();

        try {
            GuildReply.Guild guild_reply = hypixelAPI.getGuildByName(guild).get().getGuild();

            if (guild_reply == null) {
                p_slashCommandEvent.reply(sendEmbed("Guild not found")).queue();
                return;
            }

            boolean isJoinable = guild_reply.isJoinable();
            String joinable = isJoinable ? "Yes" : "No";

            p_slashCommandEvent.reply(
                sendEmbed(
                "- **Name:** " + guild_reply.getName() + "\n" +
                "- **Experience:** " + guild_reply.getExperience() + "\n" +
                "- **Tag:** " + guild_reply.getTag() + "\n" +
                "- **Joinable:** " + joinable + "\n" +
                "- **Description:** " + guild_reply.getDescription()))
            .queue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MessageCreateData sendEmbed(String message) {
        return new BotEmbedBuilder()
                .setTitle("Hypixel Stats")
                .setDescription(message)
                .setImage("https://cdn.discordapp.com/attachments/1140382016295149730/1141048428353294436/wCsYPzMWHRipAAAAABJRU5ErkJggg.png")
                .setColor(new Color(0, 0, 0))
                .setTimeStamp(Instant.now())
                .build();
    }
}
