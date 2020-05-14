package hentaibro.util;

import hentaibro.Bot;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class JsonUtils
{
    public static JSONObject readDoujinFromURL(String s) throws IOException, JSONException
    {
        String link = "https://nhentai.net/g/" + s;
        String path = Bot.directory + "/data/doujins/" + s + ".json";
        File file = new File(path);
        if (file.createNewFile())
        {
            //System.out.println("Made NH API call!");
            //downloadJSONFromURL(link, path);
            createJSONFromURL(link, path);
        }

        return new JSONObject(readFile(path));
    }

    public static void createJSONFromURL(String url, String file) throws IOException
    {
        Document doc = Jsoup.connect(url).get();
        String st = doc.html();
        String x = st.substring(st.indexOf("N.gallery("), st.length() - 50).substring(10);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(x);
        writer.close();
    }

    public static void downloadJSONFromURL(String urlStr, String file) throws IOException
    {
        URL url = new URL(urlStr);
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedInputStream bis = new BufferedInputStream(httpcon.getInputStream());
        FileOutputStream fos = new FileOutputStream(file);
        byte dataBuffer[] = new byte[1024];
        int bytesRead;

        while ((bytesRead = bis.read(dataBuffer, 0, 1024)) != -1)
        {
            fos.write(dataBuffer, 0, bytesRead);
        }

        fos.close();
        bis.close();
    }

    public static JSONObject readJsonObjectWithoutSaving(String s) throws IOException, JSONException
    {
        URL url = new URL(s);
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
        InputStream is = httpcon.getInputStream();
        try
        {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
        finally
        {
            is.close();
        }
    }

    public static JSONArray readJsonArrayWithoutSaving(String s) throws IOException, JSONException
    {
        URL url = new URL(s);
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
        InputStream is = httpcon.getInputStream();
        try
        {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONArray(jsonText);
        }
        finally
        {
            is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1)
        {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String readFile(String filename)
    {
        String result = "";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null)
            {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

}
