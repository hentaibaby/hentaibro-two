package hentaibro.command.faptrack.hentai;

import hentaibro.command.Command;
import hentaibro.database.Database;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class RecalculateDegeneracyCommand extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        recalculate();
        channel.sendMessage("Done!").queue();
    }

    private void recalculate()
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "SELECT id FROM Doujins";
            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery();

            while (rs.next())
            {
                try
                {
                    Doujin d = new Doujin(rs.getString("id"));
                    sql = "UPDATE Doujins SET degen = ? WHERE id = ? ";
                    PreparedStatement update = conn.prepareStatement(sql);
                    update.setDouble(1, d.getDegeneracyIndex());
                    update.setInt(2, d.getId());
                    update.executeUpdate();
                    update.close();
                }
                catch (Exception e)
                {
                    System.out.println("failed " + rs.getInt("id"));
                    e.printStackTrace();
                }
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
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
    protected boolean requireWhitelist()
    {
        return true;
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "recalculate");
    }
}
