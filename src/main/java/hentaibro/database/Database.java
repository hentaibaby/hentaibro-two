package hentaibro.database;

import hentaibro.Bot;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;

public class Database
{
    private static Database instance;

    public static Database getInstance()
    {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    public Connection connectToDB() throws SQLException
    {
        Connection conn;

        try
        {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + Bot.directory + "/data/hentaidb.db";
            conn = DriverManager.getConnection(url);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }

        return conn;
    }

    public void checkTables() throws SQLException
    {
        checkDoujinTable();
        checkTagTable();
        checkUsersTable();
        checkCooldownTable();
        checkPrefireTable();
        checkFRQTable();
        checkNiggerTable();
    }

    private void checkNiggerTable() throws SQLException
    {
        Connection conn = null;
        PreparedStatement statement = null;
        try
        {
            conn = getInstance().connectToDB();
            String sql = "CREATE TABLE IF NOT EXISTS NIGGER " +
                    "(uid NUMERIC NOT NULL, " +
                    "niggerCount TEXT NOT NULL, " +
                    "PRIMARY KEY (uid))";
            statement = conn.prepareStatement(sql);
            statement.execute();
        }
        finally
        {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(statement);
        }
    }

    private void checkFRQTable() throws SQLException
    {
        Connection conn = null;
        PreparedStatement statement = null;
        try
        {
            conn = getInstance().connectToDB();
            String sql = "CREATE TABLE IF NOT EXISTS FRQ " +
                    "(year NUMERIC NOT NULL, " +
                    "questionID TEXT NOT NULL, " +
                    "priority NUMERIC NOT NULL, " +
                    "qURL TEXT NOT NULL, " +
                    "aURL TEXT NOT NULL, " +
                    "PRIMARY KEY (year, questionID))";
            statement = conn.prepareStatement(sql);
            statement.execute();
        }
        finally
        {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(statement);
        }
    }

    private void checkPrefireTable() throws SQLException
    {
        Connection conn = null;
        PreparedStatement statement = null;
        try
        {
            conn = getInstance().connectToDB();
            String sql = "CREATE TABLE IF NOT EXISTS Prefire " +
                    "(year NUMERIC NOT NULL, " +
                    "questionID TEXT NOT NULL, " +
                    "qURL TEXT NOT NULL, " +
                    "aURL TEXT NOT NULL, " +
                    "PRIMARY KEY (year, questionID))";
            statement = conn.prepareStatement(sql);
            statement.execute();
        }
        finally
        {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(statement);
        }
    }

    private void checkCooldownTable() throws SQLException
    {
        Connection conn = null;
        PreparedStatement statement = null;
        try
        {
            conn = getInstance().connectToDB();
            String sql = "CREATE TABLE IF NOT EXISTS Cooldowns " +
                    "(guild TEXT NOT NULL, " +
                    "command TEXT NOT NULL, " +
                    "time NUMERIC NOT NULL," +
                    "PRIMARY KEY (guild, command, time))";
            statement = conn.prepareStatement(sql);
            statement.execute();
        }
        finally
        {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(statement);
        }
    }

    private void checkUsersTable() throws SQLException
    {
        Connection conn = null;
        PreparedStatement statement = null;
        try
        {
            conn = getInstance().connectToDB();
            String sql = "CREATE TABLE IF NOT EXISTS Users " +
                    "(uid TEXT NOT NULL, " +
                    "guild TEXT NOT NULL, " +
                    "PRIMARY KEY (uid, guild))";
            statement = conn.prepareStatement(sql);
            statement.execute();
        }
        finally
        {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(statement);
        }
    }

    private void checkDoujinTable() throws SQLException
    {
        Connection conn = null;
        Statement statement = null;

        try
        {
            conn = getInstance().connectToDB();
            String sql = "CREATE TABLE IF NOT EXISTS Doujins" +
                    " (uid NUMERIC NOT NULL," +
                    " id NUMERIC NOT NULL," +
                    " date NUMERIC NOT NULL," +
                    " degen NUMERIC NOT NULL," +
                    " artist TEXT NOT NULL," +
                    " fapCount NUMERIC DEFAULT 1," +
                    " numPages NUMERIC DEFAULT 1," +
                    " PRIMARY KEY (uid, id))";

            statement = conn.createStatement();
            statement.execute(sql);
            statement.close();

            sql = "CREATE TABLE IF NOT EXISTS tempDoujins" +
                    " (uid NUMERIC NOT NULL," +
                    " id NUMERIC NOT NULL," +
                    " date NUMERIC NOT NULL," +
                    " PRIMARY KEY (uid, id))";

            statement = conn.createStatement();
            statement.execute(sql);
        }
        finally
        {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(statement);
        }
    }

    private void checkTagTable() throws SQLException
    {
        Connection conn = null;
        Statement statement = null;
        try
        {
            conn = getInstance().connectToDB();
            String sql = "CREATE TABLE IF NOT EXISTS Tags " +
                    "(uid NUMERIC NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "deglvl NUMERIC NOT NULL, " +
                    "count NUMERIC DEFAULT 1, " +
                    "PRIMARY KEY (uid, name))";
            statement = conn.createStatement();
            statement.execute(sql);
        }
        finally
        {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(statement);
        }
    }
}