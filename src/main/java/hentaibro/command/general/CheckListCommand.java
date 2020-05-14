package hentaibro.command.general;

import hentaibro.Settings;
import hentaibro.command.Command;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class CheckListCommand extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {

        String id = event.getMessage().getMentionedMembers().get(0).getUser().getId();
        MessageChannel channel = event.getChannel();

        if (Settings.blacklist.contains(id))
        {
            channel.sendMessage("User is on the blacklist.").queue();
        }
        if (Settings.whitelist.contains(id))
        {
            channel.sendMessage("User is on the whitelist.").queue();
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
        return Collections.singletonList(pf + "checklist");
    }

    @Override
    public String getDescription()
    {
        return null;
    }
}
