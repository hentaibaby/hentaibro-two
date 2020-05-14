package hentaibro.database;

import hentaibro.Bot;
import hentaibro.util.TimeUtils;
import org.apache.commons.dbutils.DbUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class BackupManager
{
    private static final Path SOURCE = Paths.get(Bot.directory + "/data/hentaidb.db");
    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    private static final Runnable backup = () ->
    {
        Connection conn = null;
        try
        {
            conn = Database.getInstance().connectToDB();
            Path target = Paths.get(Bot.directory + "/data/backup/" + TimeUtils.getDBTimestamp() + ".db");
            Files.copy(SOURCE, target, REPLACE_EXISTING);
        }
        catch (SQLException | IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            DbUtils.closeQuietly(conn);
        }
    };

    public static void startBackupService()
    {
        scheduler.scheduleAtFixedRate(backup, 1, 1, TimeUnit.HOURS);
    }

    public static void backupNow()
    {
        new Thread(backup).start();
    }
}
