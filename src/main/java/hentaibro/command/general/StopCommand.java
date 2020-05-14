package hentaibro.command.general;

import hentaibro.Bot;
import hentaibro.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class StopCommand extends Command
{

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        if (args.length < 2)
        {
            return;
        }

        if (args[1].toLowerCase().equals("-stop"))
        {
            System.exit(Bot.NORMAL_SHUTDOWN);
        }

        if (args[1].toLowerCase().equals("-restart"))
        {
            Bot.restartBot();
        }

    }

    @Override
    protected boolean requireWhitelist()
    {
        return true;
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "sys");
    }

    @Override
    public String getDescription()
    {
        return null;
    }
}
