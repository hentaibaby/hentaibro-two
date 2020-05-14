package hentaibro.command.meme;

import hentaibro.Bot;
import hentaibro.command.Command;
import hentaibro.database.Database;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.dbutils.DbUtils.closeQuietly;

public class NiggerTopCommand extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "SELECT * from NIGGER order by niggerCount DESC    ";
            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery();
            StringBuilder sb = new StringBuilder();
            sb.append("**boomertop**```");
            while (rs.next())
            {
                try
                {
                    String name = Bot.api.getUserById(rs.getString("uid")).getName();
                    sb.append(name).append("   ").append(rs.getInt("niggerCount")).append("\n");
                }
                catch (Exception e)
                {

                }
            }
            sb.append("```");
            channel.sendMessage(sb.toString()).queue();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeQuietly(conn, statement, rs);
        }
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "ntop");
    }

    @Override
    public String getDescription()
    {
        return "```niggertop lol```";
    }
}
