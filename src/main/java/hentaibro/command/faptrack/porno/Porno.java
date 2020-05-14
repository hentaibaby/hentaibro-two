package hentaibro.command.faptrack.porno;

import hentaibro.Settings;
import hentaibro.command.faptrack.Tag;
import hentaibro.command.faptrack.hentai.Doujin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Porno extends Doujin
{
    private static Pattern compiledPattern = Pattern.compile("(?<=https://www.pornhub.com/view_video.php\\?viewkey=).*");
    private String url;

    public Porno(String url) throws Exception
    {
        degenTags = new ArrayList<>();
        allTags = new ArrayList<>();
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find())
        {
            id = matcher.group().hashCode();
        }
        else
        {
            throw new IllegalArgumentException("Invalid PH Link: " + url);
        }
        this.url = url;
        Document doc = Jsoup.connect(url).get();
        Elements cats = doc.getElementsByClass("categoriesWrapper").get(0).children();
        for (int i = 0; i < cats.size() - 1; i++)
        {
            String name = cats.get(i).text().toLowerCase().trim();
            if (Settings.degMap.containsKey(name))
            {
                degenTags.add(Settings.degMap.get(name));
                allTags.add(Settings.degMap.get(name));
            }
            else
            {
                allTags.add(new Tag(name, 0));
            }
        }
        title = doc.getElementsByClass("inlineFree").text();
        artist = url;
        pages = 0;
        calcDegen();
    }

    @Override
    public String getThumbnail()
    {
        return "https://cdn.discordapp.com/avatars/224669665572421632/f2df401d79d56632cd810e8ff7305c50.png";
    }

    @Override
    public MessageEmbed getEmbed()
    {
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(getTitle(), url)
                .setColor(new Color(16711680))
                .setThumbnail("https://cdn.discordapp.com/attachments/209101183166447617/547329393157079050/20190218_195814.jpg")
                .addField("Tags", formatTags(), false)
                .addField("Degeneracy Index", String.format("%.3f", getDegeneracyIndex()), false);
        return eb.build();
    }
}
