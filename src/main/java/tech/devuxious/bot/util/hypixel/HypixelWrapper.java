package tech.devuxious.bot.util.hypixel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import tech.devuxious.bot.util.embed.BotEmbedBuilder;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author remig
 * @project Discord-Utility-Bot
 * @date 10/8/2023
 */
public class HypixelWrapper {

    public static PlayerReply.Player getPlayerByName(HypixelAPI api, String username, TextChannel p_message) {

        String result;

        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(3000);
            connection.connect();
            int code = connection.getResponseCode();
            StringBuilder msg = new StringBuilder();
            if (code == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    msg.append(line).append("\n");
                }
                reader.close();
                result = msg.toString();
            } else {
                return null;
            }
            connection.disconnect();
        } catch (IOException e) {
            return null;
        }

        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
        String rawUUID = jsonObject.get("id").getAsString();
        CompletableFuture<PlayerReply> playerFuture = api.getPlayerByUuid(rawUUID);

        if (playerFuture == null) {
            p_message.sendMessage(new BotEmbedBuilder()
                    .setTitle("Hypixel Stats")
                    .setDescription("Failed to get player data. Please try again later.")
                    .setImage("https://media.discordapp.net/attachments/1138582327656722658/1138801197709529088/image_1.png")
                    .setColor(new Color(0, 0, 0))
                    .setTimeStamp(Instant.now())
                    .build()).queue();
            return null;
        }

        PlayerReply reply;
        try {
            reply = playerFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }

        if (!reply.isSuccess()) {
            p_message.sendMessage(new BotEmbedBuilder()
                    .setTitle("Hypixel Stats")
                    .setDescription("Failed to get player data. Please try again later.")
                    .setImage("https://media.discordapp.net/attachments/1138582327656722658/1138801197709529088/image_1.png")
                    .setColor(new Color(0, 0, 0))
                    .setTimeStamp(Instant.now())
                    .build()).queue();
            return null;
        }

        PlayerReply.Player player = reply.getPlayer();

        if (!player.exists()) {
            p_message.sendMessage(new BotEmbedBuilder()
                    .setTitle("Hypixel Stats")
                    .setDescription("Player does not exist.")
                    .setImage("https://media.discordapp.net/attachments/1138582327656722658/1138801197709529088/image_1.png")
                    .setColor(new Color(0, 0, 0))
                    .setTimeStamp(Instant.now())
                    .build()).queue();
        }

        return player;
    }

    public MessageCreateData sendEmbed(String message) {
        return new BotEmbedBuilder()
                .setTitle("Hypixel Stats")
                .setDescription(message)
                .setImage("https://media.discordapp.net/attachments/1138582327656722658/1138801197709529088/image_1.png")
                .setColor(new Color(0, 0, 0))
                .setTimeStamp(Instant.now())
                .build();
    }
}
