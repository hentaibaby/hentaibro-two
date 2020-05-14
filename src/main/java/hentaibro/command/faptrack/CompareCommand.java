package hentaibro.command.faptrack;

import hentaibro.command.Command;
import hentaibro.command.faptrack.hentai.Doujin;
import hentaibro.database.DAOFactory;
import hentaibro.database.HentaiDAO;
import hentaibro.util.TimeUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CompareCommand extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        MessageHistory history = new MessageHistory(channel);
        List<Message> list = history.retrievePast(10).complete();

        String doujinMessage = null;

        for (Message m : list)
        {
            if (m.getContentRaw().contains("https://nhentai.net"))
            {
                doujinMessage = m.getContentRaw();
                break;
            }
            try
            {
                doujinMessage = m.getEmbeds().get(0).getUrl();
                break;
            }
            catch (Exception e)
            {
            }
            try
            {
                Integer.parseInt(m.getContentRaw());
                doujinMessage = m.getContentRaw();
                break;
            }
            catch (NumberFormatException e)
            {
            }
        }

        if (doujinMessage == null)
        {
            channel.sendMessage("**" + event.getAuthor().getAsMention() +
                    " no doujins found in recent history.**").queue();
            return;
        }

        Doujin d;

        try
        {
            d = new Doujin(doujinMessage);
        }
        catch (Exception e)
        {
            //channel.sendMessage("Error!").queue();
            return;
        }

        HentaiDAO dao = DAOFactory.getHentaiDao();
        //array index 0 contains uid, index 1 contains date
        Map<String[], Integer> map = dao.getCommonDoujin(d.getId(), event.getGuild().getId());

        if (map.isEmpty())
        {
            channel.sendMessage("**No faps found for " + d.getTitle() + " in " +
                    event.getGuild().getName() + "**").queue();
            return;
        }

        channel.sendMessage(getEmbed(map, d, event.getGuild())).queue();

    }

    private MessageEmbed getEmbed(Map<String[], Integer> map, Doujin d, Guild guild)
    {
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(new Color(16711876))
                .setFooter("On faptrack! Official Server", null)
                .setAuthor("Comparable faps in " + guild.getName() + " for " + d.getTitle(), null,
                        null);

        StringBuilder sb = new StringBuilder();

        int count = 1;
        for (Map.Entry<String[], Integer> entry : map.entrySet())
        {
            sb.append("**").append(count).append(". ").append(guild.getMemberById(entry.getKey()[0]).getUser().getName()).append("**\n");
            sb.append("▸ Faps: ").append(entry.getValue()).append("\n");
            sb.append("▸ Score set ").append(TimeUtils.formatDateDifference(System.currentTimeMillis() -
                    Long.parseLong(entry.getKey()[1])));
            sb.append("\n");
            count++;
        }

        eb.setDescription(sb.toString());

        return eb.build();
    }


    @Override
    public List<String> getAliases()
    {
        return Arrays.asList(pf + "compare", pf + "c");
    }

    @Override
    public String getDescription()
    {
        return "FAMALAMADINGDONG";
    }
}
