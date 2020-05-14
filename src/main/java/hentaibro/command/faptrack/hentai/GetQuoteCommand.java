package hentaibro.command.faptrack.hentai;

import hentaibro.Bot;
import hentaibro.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class GetQuoteCommand extends Command
{

    private static String test(Doujin d, String link)
    {
        Random r = new Random();

        int pg = r.nextInt(d.getPages() - 3) + 3;

        try
        {
            URL url = new URL("https://i.nhentai.net/galleries/" + d.getMediaId() + "/" + pg + "." + d.getImageFormat());
            // URL url = new URL(link);

            System.out.println(url);
            BufferedImage img = ImageIO.read(url);
            File file = new File(Bot.directory + "/GetQuoteCommand." + d.getImageFormat());
            ImageIO.write(img, d.getImageFormat(), file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "fail";
        }
        try
        {
            Process p = Runtime.getRuntime().exec("tesseract --tessdata-dir " +
                    Bot.directory + "/data GetQuoteCommand." + d.getImageFormat() + " fam -l eng -psm 1");
            p.waitFor();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "fail";
        }

        return loadFromFile();

    }

    private static String loadFromFile()
    {
        try
        {
            BufferedReader rd = new BufferedReader(new FileReader(new File(Bot.directory + "/fam.txt")));

            StringBuilder sb = new StringBuilder();

            String s;
            while ((s = rd.readLine()) != null)
            {
                sb.append(s);
                sb.append(" ");
            }
            return sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return " ";
        }
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        try
        {
            Doujin d = new Doujin(args[1]);
            channel.sendMessage(test(d, args[1])).queue();
        }
        catch (Exception e)
        {
            channel.sendMessage("empty").queue();
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList("*getquote");
    }

    @Override
    public String getDescription()
    {
        return null;
    }
}
