package hentaibro.util;

import hentaibro.command.faptrack.hentai.Doujin;
import hentaibro.database.DAOFactory;
import hentaibro.database.HentaiDAO;
import net.dv8tion.jda.api.entities.User;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GraphingUtils
{
    //number of days to plot
    private static final long MILLIS_IN_DAY = TimeUnit.DAYS.toMillis(1);

    //private static SimpleRegression getRegression

    public static byte[] getDoujinHistogram(User usr) throws IOException
    {
        CategoryChart chart = new CategoryChartBuilder().width(600).height(400)
                .title("Distribution of Doujins according to DGI")
                .xAxisTitle("DGI").yAxisTitle("# of Doujins in Range")
                .theme(Styler.ChartTheme.Matlab).build();

        HentaiDAO dao = DAOFactory.getHentaiDao();
        double[] xData = new double[11];
        double[] yData = new double[11];
        for (int i = 0; i < xData.length; i++)
        {
            xData[i] = i * 20;
        }

        for (int i = 0; i < xData.length - 1; i++)
        {
            String sql = "SELECT count(*) as total FROM Doujins WHERE uid = " + usr.getId() + " " +
                    "AND degen >= " + xData[i] + " AND degen < " + xData[i + 1];
            yData[i] = dao.getCount(sql);
        }

        yData[yData.length - 1] = dao.getCount("SELECT count(*) as total FROM Doujins WHERE uid = " + usr.getId() + " " +
                "AND degen >= " + xData[xData.length - 1]);

        chart.addSeries("Mean DGI", xData, yData);
        return BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.JPG);
    }

    public static byte[] getDGIChartForMultipleUsers(List<User> users, int numDays) throws IOException, SQLException
    {
        XYChart chart = new XYChartBuilder().width(600).height(400)
                .title("Degeneracy vs Time").xAxisTitle("Time").yAxisTitle("Mean DGI")
                .theme(Styler.ChartTheme.Matlab).build();

        for (User usr : users)
        {
            String sql = "SELECT * FROM Doujins WHERE uid = " + usr.getId() + " ORDER BY date DESC";
            List<Doujin> doujins = DAOFactory.getHentaiDao().getMultipleDoujins(sql);
            chart.addSeries(usr.getName(), getXData(numDays), getYData(doujins, numDays));
        }

        return BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.JPG);
    }

    public static XYChart getDGIChartForSingleUser(User usr, int numDays) throws SQLException
    {
        XYChart chart = new XYChartBuilder().width(600).height(400)
                .title("Degeneracy vs Time").xAxisTitle("Time").yAxisTitle("Mean DGI")
                .theme(Styler.ChartTheme.Matlab).build();

        String sql = "SELECT * FROM Doujins WHERE uid = " + usr.getId() + " ORDER BY date DESC";
        List<Doujin> doujins = DAOFactory.getHentaiDao().getMultipleDoujins(sql);

        chart.addSeries("DGI vs Time", getXData(numDays), getYData(doujins, numDays));
        return chart;
    }

    private static double[] getXData(int numDays)
    {
        double[] xData = new double[numDays];

        for (int i = numDays - 1; i > 0; i--)
        {
            xData[i] = i + 1;
        }

        return xData;
    }

    private static double[] getYData(List<Doujin> doujins, int numDays)
    {
        long start = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(numDays - 1);
        List<Doujin> doujinsInRange = new ArrayList<>();
        List<Doujin> doujinsBefore = new ArrayList<>();

        for (Doujin d : doujins)
        {
            if (d.getDateRecorded() >= start)
            {
                doujinsInRange.add(d);
            }
            else
            {
                doujinsBefore.add(d);
            }
        }

        double[] yData = new double[numDays];

        double startingDGI = 0;
        double rollingDGI = 0;

        if (doujinsBefore.size() > 0)
        {
            for (Doujin d : doujinsBefore)
            {
                startingDGI += d.getDegeneracyIndex();
            }
            rollingDGI = startingDGI;
            startingDGI /= doujinsBefore.size();

        }

        int numDoujinsCounted = doujinsBefore.size();
        yData[0] = startingDGI;
        int j = 1;
        for (int i = numDays - 1; i > 0; i--)
        {
            List<Doujin> temp = getDoujinsInRange(doujinsInRange, i, i - 1);
            if (!temp.isEmpty())
            {
                for (Doujin d : temp)
                {
                    rollingDGI += d.getDegeneracyIndex();
                }
                numDoujinsCounted += temp.size();
            }
            yData[j] = rollingDGI / numDoujinsCounted;
            j++;
        }

        return yData;
    }

    private static List<Doujin> getDoujinsInRange(List<Doujin> doujins, int start, int end)
    {
        long x = System.currentTimeMillis() - (start * MILLIS_IN_DAY);
        long y = System.currentTimeMillis() - (end * MILLIS_IN_DAY);

        List<Doujin> temp = new ArrayList<>();
        for (Doujin d : doujins)
        {
            if (d.getDateRecorded() >= x && d.getDateRecorded() <= y)
            {
                temp.add(d);
            }
        }
        return temp;
    }


}
