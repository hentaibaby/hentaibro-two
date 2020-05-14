package hentaibro.command;

import hentaibro.Settings;
import hentaibro.util.CooldownManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.StringJoiner;

public abstract class Command extends ListenerAdapter
{
    protected static final String pf = Settings.prefix;
    private static final Logger logger = LoggerFactory.getLogger(Command.class);
    protected MessageChannel channel;

    public abstract void onCommand(MessageReceivedEvent event, String[] args);

    public abstract List<String> getAliases();

    public abstract String getDescription();

    @Override
    public void onMessageReceived(MessageReceivedEvent e)
    {
        if (e.getAuthor().isBot() || isBlacklisted(e))
        {
            return;
        }

        if (containsCommand(e.getMessage()))
        {
            channel = e.getChannel();
            channel.sendTyping().queue();

            if (requireWhitelist() && isNotWhitelisted(e))
            {
                printInsufficientPerms();
                return;
            }

            try
            {
                if (getCooldown() > 0)
                {
                    long timeRemaining = CooldownManager.getCooldownRemaining(
                            e.getGuild().getIdLong(), getCooldown(), getClass().getSimpleName());
                    if (timeRemaining > 0)
                    {
                        double inSeconds = (double) timeRemaining / 1000;
                        String formatted = String.format("%1.2f", inSeconds);
                        channel.sendMessage("This command is on cooldown. Try again in **" + formatted + "** seconds.").queue();
                        return;
                    }
                }
                logger.warn("(" + e.getAuthor().getName() + ") " + e.getMessage().getContentDisplay());
                onCommand(e, commandArgs(e.getMessage()));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                String[] arr = exceptionAsString.split("\n", -1);
                EmbedBuilder eb = new EmbedBuilder()
                        .setColor(new Color(16711680))
                        .setTitle("An error has occurred")
                        .setDescription("```java\n" + arr[0] + "\n" + arr[1] + "```");
                channel.sendMessage(eb.build()).queue();
            }
        }
    }

    protected String getMessageAsString(String[] args)
    {
        if (args.length < 2)
        {
            return "";
        }
        StringJoiner sj = new StringJoiner(" ");
        for (int i = 1; i < args.length; i++)
        {
            sj.add(args[i]);
        }
        return sj.toString();
    }

    private boolean containsCommand(Message message)
    {
        return getAliases().contains(commandArgs(message)[0].toLowerCase());
    }

    protected boolean requireWhitelist()
    {
        return false;
    }

    public int getCooldown()
    {
        return 0;
    }

    private void printInsufficientPerms()
    {
        channel.sendMessage("You are not allowed to use this command.").queue();
    }

    protected void printInvalidSyntax()
    {
        channel.sendMessage("Invalid Syntax! Use *help <command> for more info.").queue();
    }

    protected boolean isPrivateMessage()
    {
        return channel.getType() == ChannelType.PRIVATE;
    }

    protected void printNotAvailableForPM()
    {
        channel.sendMessage("This command is not available for use in DMs!").queue();
    }

    private boolean isBlacklisted(MessageReceivedEvent event)
    {
        return (Settings.blacklist.contains(event.getAuthor().getId()));
    }

    protected boolean isNotWhitelisted(MessageReceivedEvent event)
    {
        return (!Settings.whitelist.contains(event.getAuthor().getId()));
    }

    private String[] commandArgs(Message message)
    {
        return commandArgs(message.getContentRaw());
    }

    private String[] commandArgs(String string)
    {
        return string.split(" ");
    }

}
