package hentaibro.command.faptrack;

import hentaibro.command.Command;
import hentaibro.util.GraphingUtils;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class HentaiGraphCommand extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        List<User> mentioned = event.getMessage().getMentionedUsers();

        try
        {
            if (mentioned.size() > 1)
            {
                byte[] imgData = GraphingUtils.getDGIChartForMultipleUsers(mentioned, 15);
                channel.sendFile(imgData, "_DGIgraph.jpg").queue();
            }
            else
            {
                User usr = (mentioned.size() == 0) ? event.getAuthor() : mentioned.get(0);
                XYChart chart = GraphingUtils.getDGIChartForSingleUser(usr, 15);
                byte[] imgData = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.JPG);
                channel.sendFile(imgData, usr.getName() + "_DGIgraph.jpg").queue();
                imgData = GraphingUtils.getDoujinHistogram(usr);
                channel.sendFile(imgData, usr.getName() + "_histo.jpg").queue();
            }
        }
        catch (IOException | SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "hgraph");
    }

    @Override
    public String getDescription()
    {
        return null;
    }
}
