package tech.devuxious.bot.util.hypixel;

/**
 * @author remig
 * @project Discord-Utility-Bot
 * @date 10/8/2023
 */
public enum RankWrapper {

    VIP("VIP", "VIP"),
    VIP_PLUS("VIP_PLUS", "VIP+"),
    MVP("MVP", "MVP"),
    MVP_PLUS("MVP_PLUS", "MVP+"),
    SUPERSTAR("SUPERSTAR", "MVP++"),
    ADMIN("ADMIN", "ADMIN"),
    GM("GAME_MASTER", "GM"),
    MOD("MODERATOR", "MOD"),
    YT("YOUTUBER", "YOUTUBER"),
    OWNER("OWNER", "OWNER")

    ;

    private final String raw;
    private final String translation;

    RankWrapper(String raw, String translation) {
        this.raw = raw;
        this.translation = translation;
    }

    public String getRaw() {
        return raw;
    }

    public String getTranslation() {
        return translation;
    }
}
