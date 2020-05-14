package hentaibro.command.faptrack;

import hentaibro.command.Command;
import hentaibro.database.DAOFactory;
import hentaibro.database.HentaiDAO;
import hentaibro.util.TextUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class FapTopCommand extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        List<User> mentioned = event.getMessage().getMentionedUsers();
        User usr = (mentioned.size() == 0) ? event.getAuthor() : mentioned.get(0);
        HentaiDAO dao = DAOFactory.getHentaiDao();

        EmbedBuilder eb = new EmbedBuilder()
                .setColor(new Color(16711876))
                .setFooter("On faptrack! Official Server", null)
                .setThumbnail(usr.getAvatarUrl())
                .setAuthor("Top 5 faptrack! Doujins for " + usr.getName(), null,
                        null);

        eb.setDescription(TextUtils.formatDoujinAsOWO(dao.getMostDegenerateDoujins(usr.getId())));

        channel.sendMessage(eb.build()).queue();
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "faptop");
    }

    @Override
    public String getDescription()
    {
        return "```" + pf + "faptop <@user>\n\n" +
                "Returns the top 5 most degenerate doujins recorded by a user";
    }
}
