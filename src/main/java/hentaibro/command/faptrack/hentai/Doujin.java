package hentaibro.command.faptrack.hentai;

import hentaibro.Settings;
import hentaibro.command.faptrack.Tag;
import hentaibro.util.JsonUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Doujin
{
    private static Pattern compiledPattern = Pattern.compile("(?<=https://nhentai.net/g/)[^/]*");
    protected List<Tag> degenTags;
    protected int id;
    protected int mediaId;
    protected int pages;
    protected String title;
    protected List<Tag> allTags;
    protected String artist;
    protected double degeneracyIndex;
    protected long dateRecorded;
    protected List<String> characters;
    private boolean translated;
    private String imageFormat;
    private String parody;

    public Doujin()
    {
    }

    public Doujin(String url) throws Exception
    {
        try
        {
            //check if url is just a doujin id
            Integer.parseInt(url);
            JSONObject res = JsonUtils.readDoujinFromURL(url);
            loadDoujin(res);
        }
        catch (NumberFormatException e)
        {
            //try to init if url is a doujin link
            Matcher matcher = compiledPattern.matcher(url);
            if (matcher.find())
            {

                String id = matcher.group();
                JSONObject res = JsonUtils.readDoujinFromURL(id);
                loadDoujin(res);
            }
            else
            {
                throw new Exception("Failed to initialize doujin.");
            }
        }
    }

    public Doujin(JSONObject res)
    {
        loadDoujin(res);
    }

    private void loadDoujin(JSONObject res)
    {

        id = res.getInt("id");
        mediaId = res.getInt("media_id");
        pages = res.getInt("num_pages");
        title = res.getJSONObject("title").getString("pretty");
        allTags = new ArrayList<>();
        degenTags = new ArrayList<>();
        characters = new ArrayList<>();

        imageFormat = res.getJSONObject("images").getJSONObject("cover")
                .getString("t").equals("j") ? "jpg" : "png";

        JSONArray tagArr = res.getJSONArray("tags");

        for (int i = 0; i < tagArr.length(); i++)
        {
            String type = tagArr.getJSONObject(i).getString("type");
            String name = tagArr.getJSONObject(i).getString("name");

            switch (type)
            {
                case "artist":
                    if (name.contains(" "))
                    {
                        StringJoiner sj = new StringJoiner("-");
                        for (String s : name.split(" "))
                        {
                            sj.add(s);
                        }
                        artist = sj.toString();
                    }
                    else
                    {
                        artist = name;
                    }
                    break;
                case "tag":
                    if (Settings.degMap.containsKey(name))
                    {
                        degenTags.add(Settings.degMap.get(name));
                        allTags.add(Settings.degMap.get(name));
                    }
                    else
                    {
                        allTags.add(new Tag(name, 0));
                    }
                    break;
                case "character":
                    characters.add(name);
                    break;
                case "parody":
                    parody = name;
                    break;
                case "translated":
                    translated = true;
                    break;
            }
        }
        calcDegen();
    }

    public String calcDegen()
    {
        int numAllTags = allTags.size();
        int numDegTags = degenTags.size();

        if (numDegTags == 0)
        {
            degeneracyIndex = 0;
            return "absolute purity";
        }

        double totalScore = 0;

        for (Tag tag : degenTags)
        {
            totalScore += tag.getScore();
        }

        totalScore = Math.pow(totalScore, 0.8); // 1/2.5

        double tagDenom = numAllTags - ((double) numDegTags / 2);
        double tagRatio = (double) numDegTags / tagDenom;
        double logFunc;

        if (numAllTags == 1)
            logFunc = Math.log(2);
        else
            logFunc = Math.log(numAllTags);

        degeneracyIndex = totalScore * tagRatio * logFunc;

        return "**" + title + "** Debug DGI\n" +
                "Tag   Ratio: " + tagRatio +
                "\nTotal Tags: " + numAllTags +
                "\nDegen Tags: " + numDegTags +
                "\nSigmaScore: " + totalScore +
                "\nLogarithm : " + logFunc +
                "\nDegenIndex: " + degeneracyIndex +
                "\n" + degenTags;
    }

    protected String formatTags()
    {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < allTags.size() && i < 5; i++)
        {
            s.append(allTags.get(i).getName()).append(", ");
        }
        return s.substring(0, s.length() - 2);
    }

    public MessageEmbed getEmbed()
    {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(getTitle(), "https://nhentai.net/g/" + getId())
                .setColor(new Color(16711680))
                .setThumbnail(getThumbnail())
                .setAuthor(getArtist(), "https://nhentai.net/artist/" + getArtist(), null)
                .addField("Top Tags", formatTags(), false)
                .addField("Degeneracy Index", String.format("%.3f", getDegeneracyIndex()), false);

        return eb.build();
    }

    public String getParody()
    {
        return parody;
    }

    public List<String> getCharacters()
    {
        return characters;
    }

    public String getThumbnail()
    {
        return "https://t.nhentai.net/galleries/" + getMediaId() + "/cover." + imageFormat;
    }

    public long getDateRecorded()
    {
        return dateRecorded;
    }

    public void setDateRecorded(long dateRecorded)
    {
        this.dateRecorded = dateRecorded;
    }

    public int getMediaId()
    {
        return mediaId;
    }

    public int getPages()
    {
        return pages;
    }

    public String getTitle()
    {
        return title;
    }

    public String getArtist()
    {
        return artist;
    }

    public List<Tag> getAllTags()
    {
        return allTags;
    }

    public List<Tag> getDegenTags()
    {
        return degenTags;
    }

    public int getId()
    {
        return id;
    }

    public double getDegeneracyIndex()
    {
        return degeneracyIndex;
    }

    public boolean isTranslated()
    {
        return translated;
    }

    public String getImageFormat()
    {
        return imageFormat;
    }

    @Override
    public String toString()
    {
        return "[" + title + "]" + "(" + "https://nhentai.net/g/" + id + ")";
    }
}
