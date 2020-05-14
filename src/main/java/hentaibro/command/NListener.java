package hentaibro.command;

import hentaibro.database.Database;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.apache.commons.dbutils.DbUtils.closeQuietly;

public class NListener extends ListenerAdapter
{
    private static boolean checkNigger(String s, String uid)
    {
        if (s.toLowerCase().contains("nigger"))
        {
            Connection conn = null;
            PreparedStatement statement = null;

            try
            {
                conn = Database.getInstance().connectToDB();
                String sql = "INSERT OR IGNORE INTO NIGGER (uid, niggerCount) VALUES (?,0)";
                statement = conn.prepareStatement(sql);
                statement.setString(1, uid);
                statement.executeUpdate();
                statement.close();
                sql = "UPDATE NIGGER SET niggerCount = niggerCount + 1 WHERE uid = ?";
                statement = conn.prepareStatement(sql);
                statement.setString(1, uid);
                statement.executeUpdate();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            finally
            {
                closeQuietly(statement);
                closeQuietly(conn);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e)
    {
        if (e.getAuthor().isBot())
            return;
        checkNigger(e.getMessage().getContentRaw(), e.getAuthor().getId());
        //    e.getChannel().sendMessage("BOOMER").queue();
    }
}
