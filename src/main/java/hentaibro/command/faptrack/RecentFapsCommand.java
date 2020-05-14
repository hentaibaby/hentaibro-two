package hentaibro.command.faptrack;

import hentaibro.command.Command;
import hentaibro.command.faptrack.hentai.Doujin;
import hentaibro.database.DAOFactory;
import hentaibro.database.HentaiDAO;
import hentaibro.util.TextUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class RecentFapsCommand extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        List<User> mentioned = event.getMessage().getMentionedUsers();
        User usr = (mentioned.size() == 0) ? event.getAuthor() : mentioned.get(0);
        HentaiDAO dao = DAOFactory.getHentaiDao();
        List<Doujin> recents = dao.getRecents(usr.getId());

        if (recents == null)
        {
            channel.sendMessage("User has not recorded any doujins!").queue();
            return;
        }

        if (args.length >= 2 && args[1].equals("-l"))
        {
            channel.sendMessage(getRecentsEmbed(recents, usr, true)).queue();
        }
        else
        {
            channel.sendMessage(getRecentsEmbed(recents, usr, false)).queue();
        }
    }

    private MessageEmbed getRecentsEmbed(List<Doujin> list, User usr, boolean l)
    {
        String title = l ? "Most Recent faptrack! Doujins for " + usr.getName() :
                "Most Recent faptrack! Doujin for " + usr.getName();

        EmbedBuilder eb = new EmbedBuilder()
                .setColor(new Color(8311585))
                .setAuthor(title, null, null)
                .setFooter("On faptrack! Official Server", null);

        if (l)
        {
            eb.setThumbnail(usr.getAvatarUrl())
                    .setDescription(TextUtils.formatDoujinAsOWO(list));
        }
        else
        {
            Doujin d = list.get(0);
            eb.setThumbnail(d.getThumbnail())
                    .setDescription(TextUtils.formatDoujinAsOWO(d));
        }

        return eb.build();
    }

    @Override
    public String getDescription()
    {
        return "```*recent <@user>\n\n" +
                "Returns the 5 most recent doujins recorded with *faptrack```";
    }

    @Override
    public List<String> getAliases()
    {
        return Arrays.asList(pf + "recent", pf + "r");
    }
}
