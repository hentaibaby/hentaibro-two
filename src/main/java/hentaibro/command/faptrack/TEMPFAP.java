package hentaibro.command.faptrack;

import hentaibro.command.Command;
import hentaibro.database.Database;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;

public class TEMPFAP extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        int id = Integer.parseInt(args[1]);

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "INSERT INTO tempDoujins (uid, id, date) VALUES (?,?,?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, event.getAuthor().getId());
            statement.setInt(2, id);
            statement.setLong(3, System.currentTimeMillis());
            statement.executeUpdate();
            channel.sendMessage("success boomer").queue();
        }
        catch (Exception e)
        {
            channel.sendMessage(e.getMessage()).queue();
        }
        finally
        {
            DbUtils.closeQuietly(conn, statement, rs);
        }
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "tft");
    }

    @Override
    public String getDescription()
    {
        return "temp faptrack";
    }
}
