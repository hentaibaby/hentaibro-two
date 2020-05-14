package hentaibro.command.faptrack.hentai;

import hentaibro.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RandomDoujinCommand extends Command
{
    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        /*
        if (args.length != 2)
        {
            printInvalidSyntax();
            return;
        }
        */
        System.out.println(Arrays.toString(args));
        if (args[1].equals("gyaru"))
        {
            args[1] = "25050";
        }
        else
        {
            args[1] = "19440";

        }
        System.out.println(Arrays.toString(args));

        channel.sendMessage(HentaiSearcher.getRandomDoujin(args).getEmbed()).queue();

    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "random");

    }
}
