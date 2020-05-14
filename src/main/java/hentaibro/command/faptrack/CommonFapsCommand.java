package hentaibro.command.faptrack;

import hentaibro.command.Command;
import hentaibro.command.faptrack.hentai.Doujin;
import hentaibro.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommonFapsCommand extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        if (args.length < 3 || event.getMessage().getMentionedMembers().size() < 2)
        {
            printInvalidSyntax();
            return;
        }

        List<User> mentioned = event.getMessage().getMentionedUsers();
        User u1 = mentioned.get(0);
        User u2 = mentioned.get(1);

        List<Doujin> common = getCommonDoujins(u1.getId(), u2.getId());
        System.out.println(common);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription("Common faps between " + u1.getAsMention() + " and " + u2.getAsMention())
                .setColor(new Color(16777215))
                .setTitle("Query Results");

        for (Doujin d : common)
        {
            eb.addField(d.getArtist(), "[" + d.getTitle() + "](" + "https://nhentai.net/g/" + d.getId() + ")", false);
        }
        channel.sendMessage(eb.build()).queue();

    }

    //sort by date
    public List<Doujin> getCommonDoujins(String uid1, String uid2)
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        String name1 = "Doujins_" + uid1;
        String name2 = "Doujins_" + uid2;

        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "SELECT " + name1 + ".id FROM " + name1 +
                    " INNER JOIN " + name2
                    + " ON " + name1 + ".id=" + name2 + ".id";

            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery();
            List<Doujin> resultList = new ArrayList<>();

            while (rs.next())
            {
                System.out.println("hs next");
                resultList.add(new Doujin("https://nhentai.net/g/" + rs.getInt("id") + "/"));
            }

            return resultList;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (SQLException e)
                { /* ignored */}
            }
            if (statement != null)
            {
                try
                {
                    statement.close();
                }
                catch (SQLException e)
                { /* ignored */}
            }
            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e)
                { /* ignored */}
            }
        }
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "common");

    }

    @Override
    public String getDescription()
    {
        return "```*common <@user1> <@user2>\n\n" +
                "Returns all doujins that both users have recorded with *faptrack.```";
    }
}
