package hentaibro.command.general;

import hentaibro.command.Command;
import hentaibro.util.TextUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class SendMessage extends Command
{

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < args.length; i++)
        {
            sb.append(args[i]);
            sb.append(" ");
        }

        channel.sendMessage(TextUtils.emojify(sb.toString())).queue();
    }

    @Override
    public int getCooldown()
    {
        return 3;
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "send");

    }
}