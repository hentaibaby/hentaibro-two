package hentaibro.command.faptrack.hentai;

import hentaibro.util.JsonUtils;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HentaiSearcher extends ListenerAdapter
{

    public static Doujin getRandomDoujin(String[] terms)
    {
        Random r = new Random();
        int rand = r.nextInt(82);
        String link = "https://nhentai.net/api/galleries/tagged?tag_id=" + terms[1] + "&page=" + rand + "&sort=popular";
        System.out.println(rand);
        JSONObject results;

        try
        {
            results = JsonUtils.readJsonObjectWithoutSaving(link);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        JSONArray resultsArr = results.getJSONArray("result");

        if (resultsArr.length() == 0)
        {
            return null;
        }

        ArrayList<Doujin> doujins = new ArrayList<>();

        for (int i = 0; i < resultsArr.length(); i++)
        {
            doujins.add(new Doujin(resultsArr.getJSONObject(i)));
        }
        for (int i = 0; i < doujins.size(); i++)
        {
            int x = r.nextInt(25);
            Doujin d = doujins.get(x);
            if (d.isTranslated())
            {
                System.out.println(x);
                return d;
            }
        }
        return doujins.get(0);
    }

    public static List<Doujin> getSearchResults(String[] terms)
    {

        StringBuilder tags = new StringBuilder();
        for (int i = 1; i < terms.length; i++)
        {
            tags.append(terms[i]).append("+");
        }

        tags = new StringBuilder(tags.substring(0, tags.length() - 1));
        String link = "https://nhentai.net/api/galleries/search?query=" + tags + "&sort=popular";
        JSONObject results;

        try
        {
            results = JsonUtils.readJsonObjectWithoutSaving(link);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        JSONArray resultsArr = results.getJSONArray("result");

        if (resultsArr.length() == 0)
        {
            return null;
        }

        ArrayList<Doujin> doujins = new ArrayList<>();

        for (int i = 0; i < 5 && i < resultsArr.length(); i++)
        {
            doujins.add(new Doujin(resultsArr.getJSONObject(i)));
        }

        return doujins;

    }

}
