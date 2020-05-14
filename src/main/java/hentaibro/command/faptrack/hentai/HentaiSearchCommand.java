package hentaibro.command.faptrack.hentai;

import hentaibro.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class HentaiSearchCommand extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        if (args.length < 2)
        {
            printInvalidSyntax();
            return;
        }

        List<Doujin> doujins = HentaiSearcher.getSearchResults(args);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("NHentai Search Results")
                .setColor(new Color(16711783));

        for (Doujin d : doujins)
        {
            eb.addField(d.getArtist(), d.toString(), false);
        }

        channel.sendMessage(eb.build()).queue();
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "search");

    }

    @Override
    public String getDescription()
    {
        return "*search <terms...>\n\n" +
                "Returns the first 5 doujins for given terms, sorted by popularity.";
    }
}
