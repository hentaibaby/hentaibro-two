package hentaibro.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO
{
    public List<String> getUsersInGuild(String guild)
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "SELECT uid FROM Users WHERE guild = ? LIMIT 5";
            statement = conn.prepareStatement(sql);
            statement.setString(1, guild);
            rs = statement.executeQuery();
            List<String> uids = new ArrayList<>();

            while (rs.next())
            {
                uids.add(rs.getString("uid"));
            }
            return uids;
        }
        catch (SQLException e)
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
}
