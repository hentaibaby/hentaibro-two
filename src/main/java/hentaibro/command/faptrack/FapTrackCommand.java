package hentaibro.command.faptrack;

import hentaibro.command.Command;
import hentaibro.command.faptrack.hentai.Doujin;
import hentaibro.command.faptrack.porno.Porno;
import hentaibro.database.DAOFactory;
import hentaibro.database.HentaiDAO;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class FapTrackCommand extends Command
{

    private static final int INSERTION_OK = -1;
    private static final int INSERTION_FAILED = -2;

    @Override
    public String getDescription()
    {
        return "```*faptrack <NHentai link>\n\n" +
                "Records a doujin and updates your stats viewable with *info.```";
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        Doujin doujin;

        try
        {
            String url = args[1];
            if (url.contains("p"))
                doujin = new Porno(url);
            else
                doujin = new Doujin(url);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            channel.sendMessage("Invalid Link!").queue();
            return;
        }

        String uid = event.getAuthor().getId();
        HentaiDAO dao = DAOFactory.getHentaiDao();
        dao.checkUser(uid, event.getGuild().getId());
        double degenBefore = dao.getMeanDGI(uid);
        int result = addDoujinToDB(doujin, uid);

        if (result == INSERTION_FAILED)
        {
            channel.sendMessage(
                    "An error occurred while logging doujin.").queue();
            return;
        }


        if (result > 0)
        {
            String msg = "You've already fapped to this doujin " +
                    result + " time(s)! Incremented fapcount.";
            channel.sendMessage(msg).queue();
            return;
        }

        double degenAfter = dao.getMeanDGI(uid);
        double difference = degenAfter - degenBefore;
        String msg = "Successfully logged doujin.\n";
        msg += "You have become " + String.format("%.3f", Math.abs(difference)) + " points more " +
                (difference > 0 ? "degenerate" : "pure");

        channel.sendMessage(msg).queue();
    }

    private int addDoujinToDB(Doujin d, String uid)
    {
        HentaiDAO dao = DAOFactory.getHentaiDao();

        return dao.addDoujin(d, uid);
    }


    @Override
    public List<String> getAliases()
    {
        return Arrays.asList(pf + "faptrack", pf + "ft");
    }
}
