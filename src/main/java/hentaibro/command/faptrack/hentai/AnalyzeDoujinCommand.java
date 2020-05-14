package hentaibro.command.faptrack.hentai;

import hentaibro.command.Command;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class AnalyzeDoujinCommand extends Command
{

    @Override
    public String getDescription()
    {
        return "```*analyze <NHentai link>\n\n" +
                "Returns basic information about a doujin, including its Degeneracy Index. " +
                "Read more about the Degeneracy Index here: https://github.com/hentaibaby/hentaibro/wiki/Degeneracy-Index```";
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        MessageChannel channel = event.getChannel();
        try
        {
            Doujin d = new Doujin(args[1]);
            if (args.length == 3 && args[2].equals("--dbg"))
            {
                channel.sendMessage(d.calcDegen()).queue();
            }
            else
            {
                channel.sendMessage(d.getEmbed()).queue();
            }
        }
        catch (Exception e)
        {
            channel.sendMessage("Invalid Link!").queue();
            e.printStackTrace();
        }
    }


    @Override
    public List<String> getAliases()
    {
        return Arrays.asList(pf + "analyze", pf + "a");
    }
}
