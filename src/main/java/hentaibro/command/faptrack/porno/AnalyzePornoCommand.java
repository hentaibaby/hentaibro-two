package hentaibro.command.faptrack.porno;

import hentaibro.command.Command;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class AnalyzePornoCommand extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        MessageChannel channel = event.getChannel();
        try
        {
            Porno p = new Porno(args[1]);
            if (args.length == 3 && args[2].equals("--dbg"))
            {
                channel.sendMessage(p.calcDegen()).queue();
            }
            else
            {
                channel.sendMessage(p.getEmbed()).queue();
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
        return Collections.singletonList(pf + "ph");
    }

    @Override
    public String getDescription()
    {
        return "```Analyze a porno!```";
    }
}
