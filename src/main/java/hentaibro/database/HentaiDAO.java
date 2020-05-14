package hentaibro.database;

import hentaibro.command.faptrack.Tag;
import hentaibro.command.faptrack.hentai.Doujin;
import hentaibro.command.faptrack.porno.Porno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.dbutils.DbUtils.closeQuietly;

public class HentaiDAO
{
    public Doujin getDoujin(String sql) throws SQLException
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery();

            if (rs.next())
            {
                try
                {
                    Doujin d = new Doujin(rs.getString("id"));
                    d.setDateRecorded(rs.getLong("date"));
                    return d;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            return null;
        }
        finally
        {
            closeQuietly(conn, statement, rs);
        }
    }

    public int getCount(String sql)
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery();

            return rs.getInt("total");
        }
        catch (SQLException e)
        {
            return 0;
        }
        finally
        {
            closeQuietly(conn, statement, rs);
        }
    }

    public List<Doujin> getMultipleDoujins(String sql) throws SQLException
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery();

            List<Doujin> list = new ArrayList<>();

            while (rs.next())
            {
                try
                {
                    Doujin d = new Doujin(rs.getString("id"));
                    d.setDateRecorded(rs.getLong("date"));
                    list.add(d);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            return list;
        }
        finally
        {
            closeQuietly(conn, statement, rs);
        }
    }


    public int addDoujin(Doujin d, String uid)
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "SELECT fapCount FROM Doujins WHERE uid = ? AND id = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, uid);
            statement.setInt(2, d.getId());
            rs = statement.executeQuery();

            //if the doujin already exists, increment the fapcount
            if (rs.next())
            {
                int fc = rs.getInt("fapCount");
                statement.close();

                sql = "UPDATE Doujins SET fapCount = fapCount + 1 WHERE uid = ? AND id = ? ";
                statement = conn.prepareStatement(sql);
                statement.setString(1, uid);
                statement.setInt(2, d.getId());

                statement.executeUpdate();
                return fc;
            }
            else //the doujin doesn't exist so we add it
            {
                statement.close();
                sql = "INSERT INTO Doujins (uid, id, date, degen, artist, numPages) VALUES (?,?,?,?,?,?) ";
                statement = conn.prepareStatement(sql);
                statement.setString(1, uid);
                statement.setInt(2, d.getId());
                statement.setLong(3, System.currentTimeMillis());
                statement.setDouble(4, (d.getDegeneracyIndex()));
                statement.setString(5, d.getArtist());
                statement.setInt(6, d.getPages());
                statement.executeUpdate();
                addTags(conn, d, uid);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -2;
        }
        finally
        {
            closeQuietly(conn, statement, rs);
        }
        return 0;
    }

    private void addTags(Connection conn, Doujin d, String uid)
    {
        List<Tag> tags = d.getAllTags();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            for (Tag tag : tags)
            {
                String sql = "SELECT 1 FROM Tags WHERE uid = ? AND name = ?";
                statement = conn.prepareStatement(sql);
                statement.setString(1, uid);
                statement.setString(2, tag.getName());

                rs = statement.executeQuery();

                if (rs.next())
                {
                    statement.close();

                    sql = "UPDATE Tags SET count = count + 1 WHERE uid = ? AND name = ? ";
                    statement = conn.prepareStatement(sql);
                    statement.setString(1, uid);
                    statement.setString(2, tag.getName());
                    statement.executeUpdate();
                }
                else
                {
                    statement.close();

                    sql = "INSERT INTO Tags (uid, name, deglvl) VALUES (?,?,?)";
                    statement = conn.prepareStatement(sql);
                    statement.setString(1, uid);
                    statement.setString(2, tag.getName());
                    statement.setInt(3, tag.getScore());
                    statement.executeUpdate();
                }
                rs.close();
            }
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

    public int getNumberOfDoujinsRecorded(String uid)
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "SELECT COUNT(*) as total FROM Doujins WHERE uid = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, uid);
            rs = statement.executeQuery();

            return rs.getInt("total");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return 0;
        }
        finally
        {
            closeQuietly(conn, statement, rs);
        }
    }

    public double getMeanDGI(String uid)
    {
        List<Doujin> doujins = getUserDoujins(uid);

        if (doujins.size() == 0)
        {
            return -1;
        }

        double dgi = 0;

        for (Doujin d : doujins)
        {
            dgi += d.getDegeneracyIndex();
        }

        return dgi / doujins.size();
    }

    public List<Doujin> getUserDoujins(String uid)
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "SELECT id FROM Doujins WHERE uid = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, uid);
            rs = statement.executeQuery();
            List<Doujin> results = new ArrayList<>();

            while (rs.next())
            {
                try
                {
                    results.add(new Doujin(rs.getString("id")));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            return results;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            closeQuietly(conn, statement, rs);
        }
    }

    public List<Tag> getUserTags(String uid)
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "SELECT name, count FROM Tags WHERE uid = ? ORDER BY count DESC LIMIT 10 ";
            statement = conn.prepareStatement(sql);
            statement.setString(1, uid);
            rs = statement.executeQuery();
            List<Tag> tags = new ArrayList<>();

            while (rs.next())
            {
                tags.add(new Tag(rs.getString("name"), rs.getInt("count")));
            }

            return tags;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            closeQuietly(conn, statement, rs);
        }
    }

    public List<Doujin> getMostDegenerateDoujins(String uid)
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "SELECT id, date FROM Doujins WHERE uid = ? ORDER BY degen DESC LIMIT 5 ";
            statement = conn.prepareStatement(sql);
            statement.setString(1, uid);
            rs = statement.executeQuery();
            List<Doujin> list = new ArrayList<>();

            while (rs.next())
            {
                try
                {
                    Doujin d = new Doujin(rs.getString("id"));
                    d.setDateRecorded(rs.getLong("date"));
                    list.add(d);
                }
                catch (Exception e)
                {
                    System.out.println("Doujin not found: " + rs.getString("id"));
                }
            }
            return list;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            closeQuietly(conn, statement, rs);
        }
    }

    public void checkUser(String uid, String guild)
    {
        Connection conn = null;
        PreparedStatement statement = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "INSERT OR IGNORE INTO Users (uid, guild) values(?,?);";
            statement = conn.prepareStatement(sql);
            statement.setString(1, uid);
            statement.setString(2, guild);
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
    }

    public Map<String[], Integer> getCommonDoujin(int id, String guild)
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "SELECT Doujins.uid, fapCount, date FROM Users INNER JOIN Doujins " +
                    "ON Doujins.uid = Users.uid WHERE guild = ? AND id = ? ORDER BY fapCount DESC LIMIT 5";
            statement = conn.prepareStatement(sql);
            statement.setString(1, guild);
            statement.setInt(2, id);
            rs = statement.executeQuery();

            Map<String[], Integer> map = new LinkedHashMap<>();

            while (rs.next())
            {
                String[] arr = {rs.getString("uid"), rs.getString("date")};
                map.put(arr, rs.getInt("fapCount"));
            }

            return map;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            closeQuietly(conn, statement, rs);
        }
    }

    public String getFavoriteArtist(String uid)
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "SELECT artist FROM Doujins WHERE uid = ? GROUP BY artist HAVING COUNT(*) = " +
                    "(SELECT MAX(Cnt) FROM(SELECT COUNT(*) as Cnt FROM Doujins WHERE uid = ? " +
                    "GROUP BY artist) tmp)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, uid);
            statement.setString(2, uid);

            rs = statement.executeQuery();
            if (rs.next())
            {
                return rs.getString("artist");
            }
            return "";
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return "";
        }
        finally
        {
            closeQuietly(conn, statement, rs);
        }
    }

    public List<Doujin> getRecents(String uid)
    {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try
        {
            conn = Database.getInstance().connectToDB();
            String sql = "SELECT id, date, artist FROM Doujins WHERE uid = ? ORDER BY date DESC LIMIT 5";
            statement = conn.prepareStatement(sql);
            statement.setString(1, uid);
            rs = statement.executeQuery();

            List<Doujin> list = new ArrayList<>();

            while (rs.next())
            {
                String art = rs.getString("artist");
                Doujin d;
                if (art.contains("pornhub"))
                {
                    d = new Porno(rs.getString("artist"));
                }
                else
                {
                    d = new Doujin(rs.getString("id"));
                }

                d.setDateRecorded(rs.getLong("date"));
                list.add(d);
            }

            return list;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            closeQuietly(conn, statement, rs);
        }
    }

}