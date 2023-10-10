package tech.devuxious.bot.command.impl.tickets.enums;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */
public enum TICKET_TYPE {

    SUPPORT("Support", "support"),
    APPLICATION("Application", "application"),
    ORDER("Order", "order")

    ;

    private final String name;
    private final String id;

    TICKET_TYPE(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
