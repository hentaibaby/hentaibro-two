package hentaibro.command.faptrack;

import hentaibro.command.Command;
import hentaibro.database.DAOFactory;
import hentaibro.database.HentaiDAO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class DegenTopCommand extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        if (isPrivateMessage())
        {
            printNotAvailableForPM();
            return;
        }
        //assume it's very unlikely that 2 members will have the exact same dgi
        TreeMap<Double, Member> map = new TreeMap<>();
        HentaiDAO dao = DAOFactory.getHentaiDao();
        Guild g = event.getGuild();
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(new Color(16711680))
                .setThumbnail(g.getIconUrl())
                .setAuthor("Most Degenerate Users in " + g.getName(),
                        null, g.getIconUrl());

        for (Member m : event.getGuild().getMembers())
        {
            double dgi = dao.getMeanDGI(m.getUser().getId());
            if (dgi > 0)
            {
                map.put(dgi, m);
            }
        }

        int count = 1;
        for (Double d : map.descendingKeySet())
        {
            if (count > 5)
                break;

            int numDoujins = dao.getNumberOfDoujinsRecorded(map.get(d).getUser().getId());
            String name = "**" + count + ".** " + map.get(d).getUser().getName();
            String content = "`DGI:`**" + d + "**\n" +
                    "`Doujins:`**" + numDoujins + "**";
            eb.addField(name, content, false);
            count++;
        }

        channel.sendMessage(eb.build()).queue();
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "degentop");
    }

    @Override
    public String getDescription()
    {
        return "```" + getAliases().get(0) + "\n\n" +
                "Returns the top 5 most degenerate users in the guild.```";
    }
}
