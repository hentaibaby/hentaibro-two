package hentaibro.command.meme;

import hentaibro.command.Command;
import hentaibro.util.TextUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class NameChangeCommand extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        List<Member> members = event.getGuild().getMembers();

        boolean undo = false;
        if (args.length > 1 && args[1].equals("-undo"))
            undo = true;


        for (Member m : members)
        {
            try
            {
                if (!undo)
                    event.getGuild().modifyNickname(m, TextUtils.edgify(m.getEffectiveName())).queue();
                else
                    event.getGuild().modifyNickname(m, m.getUser().getName()).queue();
            }
            catch (Exception e)
            {
                //channel.sendMessage("failed: " + m.getEffectiveName()).queue();
            }
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
        return Collections.singletonList(pf + "edgify");
    }

    @Override
    public String getDescription()
    {
        return null;
    }
}
