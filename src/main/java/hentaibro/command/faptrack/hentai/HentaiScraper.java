package hentaibro.command.faptrack.hentai;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HentaiScraper
{

    public static List<Doujin> getFavs(String user, String pw)
    {
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        HtmlPage page;
        try
        {
            //login and get to favorites page
            page = client.getPage("https://nhentai.net/login/?next=%2Ffavorites%2F");
            HtmlForm form = page.getForms().get(0);
            form.getInputByName("username_or_email").setValueAttribute(user);
            form.getInputByName("password").setValueAttribute(pw);
            HtmlButton button = (HtmlButton) form.getElementsByTagName("button").get(0);
            page = button.click();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        //List<HtmlElement> pagination = page.getByXPath("//section[@class='pagination']");
        List<Doujin> favs = new ArrayList<>();

        for (int i = 1; i < 8; i++)
        {
            System.out.println("on page " + i);
            List<HtmlElement> items = page.getByXPath("//div[@class='gallery-favorite']");

            for (HtmlElement item : items)
            {
                String id = item.getAttribute("data-id");
                try
                {
                    String url = "https://nhentai.net/g/" + id + "/";
                    favs.add(new Doujin(url));
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }

            List<HtmlElement> nextButton = page.getByXPath("//a[@class='next']");

            try
            {
                page = nextButton.get(0).click();
            }
            catch (Exception e)
            {
                //we are at the last page
                break;
            }

        }
        System.out.println(favs.size());

        return favs;
    }


}
