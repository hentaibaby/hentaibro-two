package hentaibro.util;

import hentaibro.command.faptrack.Tag;
import hentaibro.command.faptrack.hentai.Doujin;
import hentaibro.command.nightcore.NightcoreSong;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.*;

public class TextUtils
{
    private static final Map<String, String> emojiMap;
    private static final Map<String, String> edgeMap;
    private static final String TTSapi = "https://translate.google.com/translate_tts?ie=UTF-8&tl=en-TR&client=tw-ob&q=";


    static
    {
        emojiMap = new HashMap<>();
        emojiMap.put("a", "<:a_:484513976697225217>");
        emojiMap.put("b", "<:B_:484514585613697034>");
        emojiMap.put("c", "<:C_:484515067375779880>");
        emojiMap.put("d", "<:D_:484515349908160512>");
        emojiMap.put("e", "<:E_:484241794964520960>");
        emojiMap.put("f", "<:F_:484515067790884875>");
        emojiMap.put("g", "<:G_:484241796206166016>");
        emojiMap.put("h", "<:H_:484513976852545549>");
        emojiMap.put("i", "<:I_:484241795027304449>");
        emojiMap.put("j", "<:J_:484514585529810955>");
        emojiMap.put("k", "<:K_:484513976793825291>");
        emojiMap.put("l", "<:L_:484513977192284180>");
        emojiMap.put("m", "<:M2_:540804594301927440>");
        emojiMap.put("n", "<:N2_:540804989476536330>");
        emojiMap.put("o", "<:O_:484514586209288195>");
        emojiMap.put("p", "<:P_:484513979163475998>");
        emojiMap.put("q", "<:Q_:484513976806539267>");
        emojiMap.put("r", "<:R_:484241795132162089>");
        emojiMap.put("s", "<:S_:484514586054230016>");
        emojiMap.put("t", "<:T_:484513978085539852>");
        emojiMap.put("u", "<:U_:484513980270903306>");
        emojiMap.put("v", "<:V_:484513978844708864>");
        emojiMap.put("w", "<:W_:484513981692772353>");
        emojiMap.put("x", "<:X_:484513977184026634>");
        emojiMap.put("y", "<:Y_:484513977204998145>");
        emojiMap.put("z", "<:Z_:484513977234227222>");

        edgeMap = new HashMap<>();
        edgeMap.put("a", "₳");
        edgeMap.put("b", "฿");
        edgeMap.put("c", "₵");
        edgeMap.put("d", "Đ");
        edgeMap.put("e", "Ɇ");
        edgeMap.put("f", "₣");
        edgeMap.put("g", "₲");
        edgeMap.put("h", "Ⱨ");
        edgeMap.put("i", "ł");
        edgeMap.put("j", "J");
        edgeMap.put("k", "₭");
        edgeMap.put("l", "Ⱡ");
        edgeMap.put("m", "₥");
        edgeMap.put("n", "₦");
        edgeMap.put("o", "Ø");
        edgeMap.put("p", "₱");
        edgeMap.put("q", "Q");
        edgeMap.put("r", "Ɽ");
        edgeMap.put("s", "₴");
        edgeMap.put("t", "₮");
        edgeMap.put("u", "Ʉ");
        edgeMap.put("v", "V");
        edgeMap.put("w", "₩");
        edgeMap.put("x", "Ӿ");
        edgeMap.put("y", "Ɏ");
        edgeMap.put("z", "Ⱬ");
    }

    public static String htmlToPlainText(String html)
    {
        Document document = Jsoup.parse(html);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");
        return document.text().replaceAll("\\\\n", "\n");
        //return Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }

    public static String messageToTTSLink(String message)
    {
        String[] tokens = message.split(" ");
        StringJoiner sj = new StringJoiner("+");
        System.out.println(Arrays.toString(tokens));
        for (String s : tokens)
        {
            if (s.contains("\n") || s.contains(">"))
                continue;
            sj.add(s);
        }
        return TTSapi + sj.toString();
    }

    public static String emojify(String s)
    {
        StringBuilder sb = new StringBuilder();

        for (char c : s.trim().toLowerCase().toCharArray())
        {
            String str = emojiMap.get(String.valueOf(c));

            if (str == null)
                sb.append(String.valueOf(c));
            else
                sb.append(str);
        }
        String j = sb.toString();
        if (j.length() >= 2000)
        {
            return "Your message is too long!";
        }
        return j;
    }

    public static String edgify(String s)
    {
        StringBuilder sb = new StringBuilder();

        for (char c : s.trim().toLowerCase().toCharArray())
        {
            String str = edgeMap.get(String.valueOf(c));

            if (str == null)
                sb.append(String.valueOf(c));
            else
                sb.append(str);
        }
        String j = sb.toString();
        if (j.length() >= 2000)
        {
            return "Your message is too long!";
        }
        return j;
    }

    //accepts emojiMap where key = title and val = link
    public static String formatNightcoreSearch(Map<String, String> map)
    {
        StringBuilder sb = new StringBuilder();
        int count = 1;

        for (Map.Entry<String, String> item : map.entrySet())
        {
            sb.append("**").append(count);
            sb.append(". [");
            sb.append(item.getKey());
            sb.append("](");
            sb.append(item.getValue());
            sb.append(")**\n");
            count++;
        }
        return sb.toString();
    }

    public static String formatTopNightcoreSongList(List<NightcoreSong> list)
    {
        StringBuilder sb = new StringBuilder();
        int count = 1;

        for (NightcoreSong song : list)
        {
            sb.append("**").append(count).append(". ");
            sb.append(song.playCount).append(" plays, ");
            sb.append("(").append(TimeUtils.millistoHMS(song.playCount * song.duration)).append(") ");
            sb.append("[").append(song.title).append("](").append(song.link).append(")");
            sb.append("**\n");
            count++;
        }
        return sb.toString();
    }

    public static String formatDoujinAsOWO(Doujin d)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("[**").append(d.getTitle()).append("**]").append("(").append("https://nhentai.net/g/").append(d.getId()).append("/)\n");
        sb.append("▸ ").append(getRankingEmote(d.getDegeneracyIndex()));
        sb.append("▸ **").append(String.format("%.2f", d.getDegeneracyIndex())).append("dgi**\n");
        sb.append("▸ ").append(d.getPages()).append(" ");
        sb.append("▸ x").append(d.getDegenTags().size()).append("/").append(d.getAllTags().size()).append(" ");

        //this is bad lol
        int zero = 0;
        int one = 0;
        int two = 0;
        int three = 0;

        for (Tag t : d.getAllTags())
        {
            int s = t.getScore();
            if (s == 0)
                zero++;
            if (s == 1)
                one++;
            if (s == 2)
                two++;
            if (s == 3)
                three++;
        }

        sb.append("▸ [").append(three).append("/").append(two).append("/").append(one).append("/").append(zero).append("]\n");
        sb.append("▸ Score set ").append(TimeUtils.formatDateDifference(System.currentTimeMillis() - d.getDateRecorded()));
        sb.append("\n");

        return sb.toString();
    }

    public static String formatDoujinAsOWO(List<Doujin> doujins)
    {
        StringBuilder sb = new StringBuilder();

        int count = 1;
        for (Doujin d : doujins)
        {
            sb.append("**").append(count).append(".** ");
            sb.append(formatDoujinAsOWO(d));
            count++;
        }
        return sb.toString();
    }

    private static String getRankingEmote(double dgi)
    {
        if (dgi == 0)
            return "<:rankingF:484147019401658368>";
        if (dgi < 30)
            return "<:rankingD:484147018965712897>";
        if (dgi < 60)
            return "<:rankingC:484147019015913484>";
        if (dgi < 100)
            return "<:rankingB:484147019305320449>";
        if (dgi < 200)
            return "<:rankingA:484145211824668685>";
        return "<:rankingS:484147019196268550>";
    }

}
