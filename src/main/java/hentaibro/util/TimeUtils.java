package hentaibro.util;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class TimeUtils
{
    public static String millistoHMS(long millis)
    {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    public static String millistoMS(long millis)
    {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    public static String formatDateDifference(long diff)
    {
        long hours = diff / 60 / 60 / 1000;
        long days = hours / 24;
        long months = days / 30;

        if (months < 1)
        {
            return days + " Days " + hours % 24 + " Hours Ago";
        }
        else
        {
            return months + " Months " + days + " Days Ago";
        }
    }

    public static String getDBTimestamp()
    {
        return new Timestamp(System.currentTimeMillis()).toString();
    }
}
