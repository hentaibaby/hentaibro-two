package hentaibro.command.faptrack;

import hentaibro.Settings;
import hentaibro.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class SearchTagCommand extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        StringJoiner sj = new StringJoiner(" ");
        for (int i = 1; i < args.length; i++)
        {
            sj.add(args[i]);
        }

        String s = sj.toString().trim().toLowerCase();

        System.out.println(s);

        if (Settings.degMap.containsKey(s))
        {
            channel.sendMessage(Settings.degMap.get(s).toString()).queue();
        }
        else
        {
            channel.sendMessage("Not found!").queue();
        }
    }

    @Override
    public List<String> getAliases()
    {
        return Arrays.asList("*tagsearch", "*ts");
    }

    @Override
    public String getDescription()
    {
        return null;
    }
}
