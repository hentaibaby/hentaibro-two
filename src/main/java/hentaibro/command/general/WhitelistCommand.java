package hentaibro.command.general;

import hentaibro.SettingsManager;
import hentaibro.command.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class WhitelistCommand extends Command
{
    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        Member mentioned;
        try
        {
            mentioned = event.getMessage().getMentionedMembers().get(0);
        }
        catch (NullPointerException e)
        {
            channel.sendMessage("User not found.").queue();
            return;
        }
        SettingsManager.addToWhiteList(mentioned.getUser().getId());
        channel.sendMessage("*" + mentioned.getEffectiveName() +
                mentioned.getUser().getDiscriminator() + "* has been added to the whitelist.").queue();

    }

    @Override
    protected boolean requireWhitelist()
    {
        return true;
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "whitelist");

    }
}
