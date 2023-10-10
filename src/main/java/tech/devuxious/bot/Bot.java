package tech.devuxious.bot;

import lombok.Getter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reactor.ReactorHttpClient;
import tech.devuxious.bot.command.CommandManager;
import tech.devuxious.bot.command.impl.roles.ReactionButtonClickEvent;
import tech.devuxious.bot.event.ButtonClickEvent;
import tech.devuxious.bot.event.DMListener;
import tech.devuxious.bot.event.GuildMemberJoinEvent;
import tech.devuxious.bot.event.ModalSubmitEvent;
import tech.devuxious.bot.storage.config.BotConfiguration;

import java.util.UUID;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */
public class Bot {

    private static final @Getter String version = "1.0.0";

    private static final @Getter BotConfiguration configuration = new BotConfiguration();
    private static @Getter ShardManager shardManager;

    private static @Getter HypixelAPI hypixelAPI;

    public void init() {
        UUID apiKey = UUID.fromString("548a71a3-94c8-407e-ada3-4289333c7059");
        hypixelAPI = new HypixelAPI(new ReactorHttpClient(apiKey));

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(configuration.getBotToken());
        builder.enableIntents(
                GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_PRESENCES
        );
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS);
        shardManager = builder.build();

        new CommandManager();

        shardManager.addEventListener(
                new CommandManager().getClient(), new ReactionButtonClickEvent(), new GuildMemberJoinEvent(), new ButtonClickEvent(), new DMListener(), new ModalSubmitEvent()
        );

        System.out.println("Successfully started Discord Bot");
    }

}
