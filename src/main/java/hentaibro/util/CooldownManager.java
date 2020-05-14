package hentaibro.util;

import hentaibro.database.Database;
import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CooldownManager
{
    public static long getCooldownRemaining(long guild, int cooldown, String command)
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "SELECT time FROM Cooldowns WHERE guild = ? AND command = ?";
            statement = conn.prepareStatement(sql);
            statement.setLong(1, guild);
            statement.setString(2, command);
            rs = statement.executeQuery();

            if (rs.next())
            {
                return cooldown * 1000 - (System.currentTimeMillis() - rs.getLong("time"));
            }

            statement.close();
            rs.close();
            sql = "INSERT OR REPLACE INTO Cooldowns (guild, command, time) VALUES (?,?,?)";
            statement = conn.prepareStatement(sql);
            statement.setLong(1, guild);
            statement.setString(2, command);
            statement.setLong(3, System.currentTimeMillis());
            statement.executeUpdate();

            return 0;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return 0;
        }
        finally
        {
            DbUtils.closeQuietly(conn, statement, rs);
        }
    }

    public static void clearCooldowns() throws SQLException
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "DELETE from Cooldowns";
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
        }
        finally
        {
            DbUtils.closeQuietly(conn, statement, rs);
        }
    }
}


