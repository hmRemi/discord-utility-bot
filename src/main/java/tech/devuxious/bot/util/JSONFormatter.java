package tech.devuxious.bot.util;

import org.json.JSONObject;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */
public class JSONFormatter {
    public static String format(String p_unformattedJSONString) {
        char[] chars = p_unformattedJSONString.toCharArray();
        String newline = System.lineSeparator();
        StringBuilder ret = new StringBuilder();
        boolean begin_quotes = false;

        for (int i = 0, indent = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\"') {
                ret.append(c);
                begin_quotes = !begin_quotes;
                continue;
            }

            if (!begin_quotes) {
                switch (c) {
                    case '{', '[' -> {
                        ret.append(c).append(newline).append(String.format("%" + (indent += 4) + "s", ""));
                        continue;
                    }
                    case '}', ']' -> {
                        ret.append(newline).append((indent -= 4) > 0 ? String.format("%" + indent + "s", "") : "").append(c);
                        continue;
                    }
                    case ':' -> {
                        ret.append(c).append(" ");
                        continue;
                    }
                    case ',' -> {
                        ret.append(c).append(newline).append(indent > 0 ? String.format("%" + indent + "s", "") : "");
                        continue;
                    }
                    default -> {
                        if (Character.isWhitespace(c)) continue;
                    }
                }
            }

            ret.append(c).append(c == '\\' ? String.valueOf(chars[++i]) : "");
        }
        return ret.toString();
    }

    public static String format(JSONObject p_unFormattedJSONObject) {
        char[] chars = p_unFormattedJSONObject.toString().toCharArray();
        String newline = System.lineSeparator();
        StringBuilder ret = new StringBuilder();
        boolean begin_quotes = false;

        for (int i = 0, indent = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\"') {
                ret.append(c);
                begin_quotes = !begin_quotes;
                continue;
            }

            if (!begin_quotes) {
                switch (c) {
                    case '{', '[' -> {
                        ret.append(c).append(newline).append(String.format("%" + (indent += 4) + "s", ""));
                        continue;
                    }
                    case '}', ']' -> {
                        ret.append(newline).append((indent -= 4) > 0 ? String.format("%" + indent + "s", "") : "").append(c);
                        continue;
                    }
                    case ':' -> {
                        ret.append(c).append(" ");
                        continue;
                    }
                    case ',' -> {
                        ret.append(c).append(newline).append(indent > 0 ? String.format("%" + indent + "s", "") : "");
                        continue;
                    }
                    default -> {
                        if (Character.isWhitespace(c)) continue;
                    }
                }
            }

            ret.append(c).append(c == '\\' ? String.valueOf(chars[++i]) : "");
        }
        return ret.toString();
    }
}
