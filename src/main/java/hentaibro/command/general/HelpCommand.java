package hentaibro.command.general;

import hentaibro.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class HelpCommand extends Command
{

    private final TreeMap<String, Command> commands;

    private MessageEmbed helpEmbed;

    public HelpCommand()
    {
        commands = new TreeMap<>();
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        if (args.length == 1)
        {
            channel.sendMessage(helpEmbed).queue();
            return;
        }

        String key = args[1];

        if (commands.containsKey(key))
        {
            Command cmd = commands.get(key);
            channel.sendMessage(cmd.getDescription()).queue();
            return;
        }

        channel.sendMessage("Command not found!").queue();
    }

    public void build()
    {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(3073808))
                .setAuthor("Command list for hentaibro:", "https://discord.gg/PvTkFfH",
                        "https://cdn.discordapp.com/avatars/461065972439318528/f98647f29f0084155cc4eea7d921e741.png");

        StringBuilder general = new StringBuilder();
        StringBuilder hentai = new StringBuilder();
        StringBuilder nightcore = new StringBuilder();

        commands.forEach((k, v) ->
        {
            String pkg = v.getClass().getPackage().getName();
            if (pkg.contains("command.general") || pkg.contains("command.meme"))
                general.append("`").append(k).append("` ");
            else if (pkg.contains("command.hentai"))
                hentai.append("`").append(k).append("` ");
            else if (pkg.contains("command.nightcore"))
                nightcore.append("`").append(k).append("` ");
        });


        String helpStr = "**General** - " + general.toString() + "\n" +
                "**Hentai** - " + hentai.toString() + "\n" +
                "**Nightcore** - " + nightcore.toString() + "\n";

        eb.setDescription(helpStr);
        helpEmbed = eb.build();
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "help");
    }

    @Override
    public String getDescription()
    {
        return "```*help\n\n" +
                "Returns all available commands for the hentaibro™ Discord® Bot```";
    }

    public Command registerCommand(Command command)
    {
        String cmdName = command.getAliases().get(0).substring(1);
        commands.put(cmdName, command);
        return command;
    }
}
