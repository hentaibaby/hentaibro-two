package hentaibro.command.meme;

import hentaibro.command.Command;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class NCommand extends Command
{

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        List<User> mentioned = event.getMessage().getMentionedUsers();
        User usr = (mentioned.size() == 0) ? event.getAuthor() : mentioned.get(0);

        Runnable r = new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 0; i < 5; i++)
                {
                    channel.sendMessage(usr.getAsMention() + " NIGGER").queue();
                    try
                    {
                        Thread.sleep(1200);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "n");

    }

    @Override
    public String getDescription()
    {
        return "N";
    }
}
