package tech.devuxious.bot.command.impl.tickets.manager;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import tech.devuxious.bot.Bot;
import tech.devuxious.bot.command.impl.tickets.enums.TICKET_TYPE;
import tech.devuxious.bot.util.embed.BotEmbedBuilder;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Devuxious
 * @project Bot
 * @date 8/9/2023
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions", "SpellCheckingInspection"})
public class TicketManager {

    private final ButtonInteractionEvent event;

    public TicketManager(ButtonInteractionEvent event) {
        this.event = event;
    }

    public void createTicket(TICKET_TYPE typeOfTicket) {
        if (event.getGuild().getCategoryById(Bot.getConfiguration().getTicketCategoryID()) == null) {
            event.reply("The ticket category does not exist!").setEphemeral(true).queue();
            return;
        }

        if (event.getGuild().getTextChannelsByName(typeOfTicket.getId() + "-" + event.getUser().getId(), true).size() > 0) {
            event.reply("You already have a " + typeOfTicket.getName() +  " ticket open, close the ticket and try again!").setEphemeral(true).queue();
            return;
        }

        AtomicInteger count = new AtomicInteger();
        event.getGuild().getTextChannels().forEach(channel -> {
            if (channel.getName().contains(event.getUser().getId())) {
                count.getAndIncrement();
            }
        });

        if (count.get() >= 2) {
            event.reply("You already have 2 tickets open, close one of them and try again!").setEphemeral(true).queue();
            return;
        }

        event.getGuild().createTextChannel(typeOfTicket.getName() + "-" + event.getUser().getId()).setParent(event.getGuild().getCategoryById(Bot.getConfiguration().getTicketCategoryID())).queue(textChannel -> {
            Role staffRole = event.getGuild().getRoleById("1138582326251618383");
            if (staffRole != null) {
                textChannel.sendMessage(staffRole.getAsMention()).queue(pingMessage -> {
                    // Delete the ping message after a delay
                    pingMessage.delete().queueAfter(1, TimeUnit.SECONDS);
                });
            }

            event.deferReply().setEphemeral(true).queue(interactionHook -> interactionHook.deleteOriginal().queue());

            textChannel.upsertPermissionOverride(event.getMember()).setAllowed(Permission.VIEW_CHANNEL).queue();
            textChannel.sendMessage(event.getUser().getAsMention()).queue(message -> message.delete().queue());
            textChannel.sendMessage(newTicketEmbed(event.getMember())).queue();
            textChannel.getManager().setTopic(new JSONObject().put("claimed", false).toString()).queue();
            System.out.println("A new " + typeOfTicket.getName() +  " ticket has been created for " + event.getUser().getName() + " (" + event.getUser().getId() + ")");
        });
    }

    public void closeTicket() {
        if (event.getChannel().getName().contains("closed")) {
            event.reply("This ticket is already being closed!").setEphemeral(true).queue();
            return;
        }

        event.reply(confirmTicketClose()).setEphemeral(true).queue();
    }

    public void closeTicketConfirm() {
        Message message = (Message) event.getMessage().getChannel().getIterableHistory().reverse().stream().toArray()[0];
        //Button claimTicket = Button.primary("claim", "Claim Ticket").withEmoji(Emoji.fromUnicode("U+1F4E3")).asDisabled();
        Button closeTicket = Button.danger("close", "Close Ticket").withEmoji(Emoji.fromUnicode("U+26A1")).asDisabled();
        Button management = Button.secondary("management", "Ticket Manager").withEmoji(Emoji.fromUnicode("U+1F4BC")).asDisabled();
        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder()
                .setActionRow(closeTicket, management)
                .setEmbeds(message.getEmbeds().get(0));
        message.editMessage(MessageEditData.fromCreateData(messageCreateBuilder.build())).queue();

        TextChannel channel = event.getChannel().asTextChannel();
        String userID = channel.getName().split("-")[1];
        channel.getManager().setName("closed-" + userID).queue();

        generateAndSendTranscript();
        event.getGuild().getTextChannelById(channel.getId()).sendMessage(getTicketClosedEmbed(event.getMember())).queue(ticket -> Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getTextChannelById(ticket.getChannel().getId())).delete().queueAfter(10, TimeUnit.SECONDS));
        event.deferReply().setEphemeral(true).queue(interactionHook -> interactionHook.deleteOriginal().queue());
    }

    public void generateAndSendTranscript() {
        try {
            TextChannel textChannel = event.getChannel().asTextChannel();
            TextChannel transcriptChannel = event.getGuild().getTextChannelById(Bot.getConfiguration().getTranscriptChannelID());
            String userID = textChannel.getName().split("-")[1];
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss . MM/dd/yyyy");
            AtomicBoolean empty = new AtomicBoolean(true);

            File file = new File("transcript" + userID + ".txt");
            FileWriter fileWriter = new FileWriter(file);
            ArrayList<String> messages = new ArrayList<>();
            fileWriter.write("---> (Ticket Transcript) <---\n");
            fileWriter.write("If you have any questions or concerns, please contact a member of the staff team.\n");
            fileWriter.write("\n");

            textChannel.getIterableHistory().forEach(message -> {
                if (message.getAuthor().isBot()) return;
                ArrayList<String> strings = new ArrayList<>(Arrays.asList(message.getContentRaw().split("\n")));
                Collections.reverse(strings);
                empty.set(false);

                if (message.getContentRaw().isEmpty()) {
                    strings.remove(0);
                    strings.add("No message content was found in this message.");
                }

                if (!message.isEdited()) {
                    if (strings.size() > 1) {
                        int currentStage = strings.size();
                        for (String string : strings) {
                            messages.add("[" + message.getTimeCreated().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")) + "] [" + message.getId() + "] " + message.getAuthor().getName() + ": " + string + " (" + currentStage + "/" + strings.size() + ")\n");
                            currentStage--;
                        }
                        return;
                    }

                    strings.forEach(string -> messages.add("[" + message.getTimeCreated().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")) + "] [" + message.getId() + "] " + message.getAuthor().getName() + ": " + string + "\n"));
                    return;
                }

                if (strings.size() > 1) {
                    int currentStage = strings.size();
                    for (String string : strings) {
                        messages.add("[" + message.getTimeCreated().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")) + "] [" + message.getId() + "] " + message.getAuthor().getName() + ": " + string + " ("+currentStage + "/" + strings.size() + ") (edited)\n");
                        currentStage--;
                    }
                    return;
                }

                strings.forEach(string -> messages.add("[" + message.getTimeCreated().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")) + "] [" + message.getId() + "] " + message.getAuthor().getName() + ": " + message.getContentRaw() + " (edited)\n"));
            });

            Collections.reverse(messages);
            messages.forEach(message -> {
                try {
                    fileWriter.write(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            if (empty.get()) {
                fileWriter.write("Oh, there apparently was no conversation in this ticket, guess someones shy.\n");
                fileWriter.write("Well, it seems like someone figured out the issue, that's good for you bro.\n");
                fileWriter.write("Either way, this transcript is completely empty and it should probably get deleted.\n");
                fileWriter.write("I couldn't just leave the transcript empty, so I had to write something in this transcript.\n");
                fileWriter.write("Yknow, I could've just left it empty or wrote some basic text, that's sad that I wasted my time.\n");
                fileWriter.write("I'm just gonna stop writing now, this is getting too long and I am getting tired of making up stuff.\n");
                fileWriter.write("Well I guess this is the end of the transcript, I hope you enjoyed reading it because I didn't enjoy making it.\n");
            }

            fileWriter.write("\n");
            fileWriter.write("Transcript was generated at " + dateTimeFormatter.format(LocalDateTime.now()).replace(".", "on") + ", time may not be accurate because of different timezones.\n");
            fileWriter.flush();
            fileWriter.close();

            MessageCreateData messageCreateData = new BotEmbedBuilder()
                    .setTitle("Ticket Transcript")
                    .setDescription("This is the transcript for " + textChannel.getName() + ". This transcript was automatically generated, it will not contain any delete messages and the previous state of edited messages.")
                    .addField("Ticket Creator", "<@" + userID + ">", false)
                    .addField("Ticket Closer", "<@" + event.getUser().getId() + ">", false)
                    .setFooter("Transcripts", event.getJDA().getSelfUser().getAvatarUrl())
                    .setTimeStamp(Instant.now())
                    .setColor(new Color(0, 0, 0))
                    .addButton(ButtonStyle.DANGER, "deletetranscript", "Delete Transcript", Emoji.fromUnicode("U+1F512"))
                    .addFile(FileUpload.fromData(file, file.getName()))
                    .build();

            transcriptChannel.sendMessage(messageCreateData).queue();
            event.getGuild().getMemberById(userID).getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(messageCreateData).queue());
            file.delete();
        } catch (IOException exception) {
            System.out.println("An error occurred while generating a transcript for a ticket.");
            exception.printStackTrace();
        }
    }

    public void claimTicket() {
        TextChannel textChannel = event.getChannel().asTextChannel();
        Member member = event.getMember();
        JSONObject jsonObject = new JSONObject(textChannel.getTopic());

        if (textChannel.getName().contains("closed-")) {
            event.reply("This ticket has been closed, therefor you cannot claim it.").setEphemeral(true).queue();
            return;
        }

        if (jsonObject != null && jsonObject.getBoolean("claimed")) {
            event.reply("This ticket has already been claimed by <@" + jsonObject.getString("claimer") + ">").setEphemeral(true).queue();
            return;
        }

        if (!member.getPermissions().contains(Permission.MODERATE_MEMBERS)) {
            event.reply("You do not have permission to claim tickets.").setEphemeral(true).queue();
            return;
        }

        jsonObject.remove("claimed");
        jsonObject.put("claimed", true);
        jsonObject.put("claimer", member.getId());
        textChannel.getManager().setTopic(jsonObject.toString()).queue();

        textChannel.upsertPermissionOverride(event.getGuild().getPublicRole()).setDenied(Permission.VIEW_CHANNEL).queue();
        textChannel.upsertPermissionOverride(event.getGuild().getPublicRole()).setDenied(Permission.MESSAGE_SEND).queue();
        textChannel.upsertPermissionOverride(event.getMember()).setAllowed(Permission.VIEW_CHANNEL).queue();
        textChannel.upsertPermissionOverride(event.getMember()).setAllowed(Permission.MESSAGE_SEND).queue();

        MessageCreateData messageCreateData = new BotEmbedBuilder()
                .setTitle("Ticket Claimed")
                .setDescription("This ticket has been claimed by " + member.getAsMention() + ", staff members can no longer claim this ticket, other staff members may assist if needed.")
                .setFooter("Tickets", event.getJDA().getSelfUser().getAvatarUrl())
                .setTimeStamp(Instant.now())
                .setColor(new Color(0, 0, 0))
                .build();
        textChannel.sendMessage(messageCreateData).queue();
        event.deferReply().setEphemeral(true).queue(interactionHook -> interactionHook.deleteOriginal().queue());

        Button claimTicket = Button.primary("claim", "Claim Ticket").withEmoji(Emoji.fromUnicode("U+1F4E3")).asDisabled();
        Button closeTicket = Button.danger("close", "Close Ticket").withEmoji(Emoji.fromUnicode("U+26A1"));
        Button management = Button.secondary("management", "Ticket Manager").withEmoji(Emoji.fromUnicode("U+1F4BC"));
        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder()
                .setActionRow(claimTicket, closeTicket, management)
                .setEmbeds(event.getMessage().getEmbeds().get(0));
        event.getMessage().editMessage(MessageEditData.fromCreateData(messageCreateBuilder.build())).queue();
    }

    public static MessageCreateData getTicketClosedEmbed(Member member) {
        return new BotEmbedBuilder()
                .setTitle("Ticket Closed")
                .setDescription("This ticket will be closed in 10 seconds, if you have any further questions please open a new ticket.")
                .setFooter(member.getUser().getName(), member.getUser().getAvatarUrl())
                .setTimeStamp(Instant.now())
                .setColor(new Color(0, 0, 0))
                .build();
    }

    public static MessageCreateData confirmTicketClose() {
        return new BotEmbedBuilder()
                .setTitle("Confirmation Required")
                .setDescription("Please confirm that you want to close this ticket, if you do not want to close this ticket please click `Dismiss message`, if you do want to close this ticket please press the `Confirm` button.")
                .setColor(new Color(0, 0, 0))
                .addButton(ButtonStyle.SUCCESS, "confirm", "Confirm", Emoji.fromUnicode("U+2705"))
                .build();
    }

    public static MessageCreateData newTicketEmbed(Member member) {
        return new BotEmbedBuilder()
                .setTitle("New Ticket - #" + member.getId())
                .setDescription("Hello " + member.getAsMention() + ", please describe your issue in as much detail as possible. A staff member will be with you shortly. If this is an emergency please ping a staff member. Pinging for less serious issues may result in a timeout or a ban from creating tickets.")
                .setTimeStamp(Instant.now())
                .setColor(new Color(0, 0, 0))
                //.addButton(ButtonStyle.PRIMARY, "claim", "Claim Ticket", Emoji.fromUnicode("U+1F4E3"))
                .addButton(ButtonStyle.DANGER, "close", "Close Ticket", Emoji.fromUnicode("U+26A1"))
                .addDisabledButton(ButtonStyle.SECONDARY, "management", "Ticket Manager", Emoji.fromUnicode("U+1F4BC"))
                .build();
    }

}
