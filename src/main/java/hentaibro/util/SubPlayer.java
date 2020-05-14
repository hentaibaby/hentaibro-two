package hentaibro.util;

import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

public class SubPlayer extends Thread
{

    private final MessageChannel channel;
    private final List<Subtitle> subs;

    public SubPlayer(MessageChannel channel, List<Subtitle> subs)
    {
        this.channel = channel;
        this.subs = subs;
    }

    @Override
    public void run()
    {
        try
        {
            Thread.sleep(1470);

            for (Subtitle sub : subs)
            {
                Thread.sleep(sub.getDelay());
                channel.sendMessage(TextUtils.emojify(sub.getContent())).queue();
            }
        }
        catch (Exception ignored)
        {

        }
    }
}
