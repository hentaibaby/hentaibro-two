package hentaibro.command.faptrack;

import hentaibro.SettingsManager;
import hentaibro.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class AddDegenTagCommand extends Command
{

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        StringJoiner s = new StringJoiner(" ");

        for (int i = 1; i < args.length; i++)
        {
            s.add(args[i]);
        }

        SettingsManager.addDegenTag(s.toString());

        channel.sendMessage("added " + s.toString()).queue();
    }

    @Override
    protected boolean requireWhitelist()
    {
        return true;
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "addtag");
    }

    @Override
    public String getDescription()
    {
        return null;
    }
}
