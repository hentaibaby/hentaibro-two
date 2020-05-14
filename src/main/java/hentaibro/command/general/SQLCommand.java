package hentaibro.command.general;

import hentaibro.command.Command;
import hentaibro.database.Database;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class SQLCommand extends Command
{

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        if (isNotWhitelisted(event))
        {
            if (!args[1].contains("select"))
            {
                channel.sendMessage("You may only issue SQLite SELECT statements.").queue();
                return;
            }
        }

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        StringJoiner s = new StringJoiner(" ");
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < args.length; i++)
        {
            s.add(args[i]);
        }

        String sql = s.toString();

        try
        {
            conn = Database.getInstance().connectToDB();
            statement = conn.prepareStatement(sql);

            if (args[1].contains("select"))
            {
                rs = statement.executeQuery();
                ResultSetMetaData metadata = rs.getMetaData();
                int columnCount = metadata.getColumnCount();
                while (rs.next())
                {
                    for (int i = 1; i <= columnCount; i++)
                    {
                        sb.append(rs.getString(i)).append(", ");
                    }
                    sb.append("\n");
                }
                channel.sendMessage("```" + sb.toString() + "```").queue();
            }
            else
            {
                int x = statement.executeUpdate();
                channel.sendMessage("success!!!!!!!!!\nreturn code: " + x).queue();
            }
        }
        catch (SQLException e)
        {
            channel.sendMessage("ebin fail!!!:DDD\n" + e.getMessage()).queue();
        }
        finally
        {
            DbUtils.closeQuietly(conn, statement, rs);
        }
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList("*sql");
    }

    @Override
    public String getDescription()
    {
        return "";
    }
}
