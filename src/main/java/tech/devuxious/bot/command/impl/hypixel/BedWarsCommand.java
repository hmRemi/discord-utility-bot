package tech.devuxious.bot.command.impl.hypixel;

import com.google.gson.JsonObject;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.GuildReply;
import net.hypixel.api.reply.PlayerReply;
import tech.devuxious.bot.Bot;
import tech.devuxious.bot.util.embed.BotEmbedBuilder;
import tech.devuxious.bot.util.hypixel.HypixelWrapper;
import tech.devuxious.bot.util.hypixel.RankWrapper;

import java.awt.*;
import java.time.Instant;
import java.util.List;

/**
 * @author remig
 * @project Bot
 * @date 9/8/2023
 */
public class BedWarsCommand extends SlashCommand {

    public BedWarsCommand() {
        this.name = "bedwars";
        this.guildOnly = false;
        this.help = "View a player's bedwars stats";

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

            JsonObject bwStats = player.getRaw().getAsJsonObject("stats").getAsJsonObject("Bedwars");
            JsonObject achievements = player.getRaw().getAsJsonObject("achievements");

            int bedwarsLevel = achievements.get("bedwars_level").getAsInt();

            int coins = bwStats.get("coins").getAsInt();
            int played_times = bwStats.get("games_played_bedwars").getAsInt();
            int losses = bwStats.get("losses_bedwars").getAsInt();

            int wins = bwStats.has("wins_bedwars") ? bwStats.get("wins_bedwars").getAsInt() : 0;
            int winstreak = bwStats.has("winstreak") ? bwStats.get("winstreak").getAsInt() : 0;

            int final_kills = bwStats.get("final_kills_bedwars").getAsInt();
            int final_deaths = bwStats.get("final_deaths_bedwars").getAsInt();
            int kills = bwStats.get("kills_bedwars").getAsInt();
            int deaths = bwStats.get("deaths_bedwars").getAsInt();

            p_slashCommandEvent.reply(sendEmbed(
                "- **Username:** " + username + "\n" +
                "- **Total Games:** " + played_times + "\n" +
                "- **Coins:** " + coins + "\n" +
                "- **Star:** " + bedwarsLevel + "\n\n" +

                "**Wins and Losses**\n" +
                "- **Wins:** " + wins + "\n" +
                "- **Losses:** " + losses + "\n" +
                "- **Winstreak:** " + winstreak + "\n\n" +

                "**Ratios**\n" +
                "- **K/D:** " + String.format("%.2f", (double)kills / deaths) + "\n" +
                "- **W/L:** " + String.format("%.2f", (double)wins / losses) + "\n" +
                "- **FK/D:** " + String.format("%.2f", (double)final_kills / final_deaths) + "\n\n" +

                "**Kills and Deaths**\n" +
                "- **Final Deaths:** " + final_deaths + "\n" +
                "- **Final Kills:** " + final_kills + "\n" +
                "- **Deaths:** " + deaths + "\n" +
                "- **Kills:** " + kills + "\n", String.valueOf(player.getUuid())))
            .queue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MessageCreateData sendEmbed(String message, String uuid) {
        return new BotEmbedBuilder()
                .setTitle("Hypixel Stats")
                .setDescription(message)
                .setThumbnail("https://crafatar.com/avatars/" + uuid)
                .setImage("https://cdn.discordapp.com/attachments/1140382016295149730/1141048428353294436/wCsYPzMWHRipAAAAABJRU5ErkJggg.png")
                .setColor(new Color(0, 0, 0))
                .setTimeStamp(Instant.now())
                .build();
    }
}
