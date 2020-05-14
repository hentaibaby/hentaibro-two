package hentaibro.command.meme;

import hentaibro.command.Command;
import hentaibro.command.nightcore.player.MusicPlayer;
import hentaibro.util.JsonUtils;
import hentaibro.util.TextUtils;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChanCommand extends Command
{

    private static final String API = "https://a.4cdn.org/";
    private static final List<String> BOARDS;
    private static final Random r = new Random();

    static
    {
        BOARDS = new ArrayList<>(71);
        BOARDS.add("a");
        BOARDS.add("b");
        BOARDS.add("c");
        BOARDS.add("d");
        BOARDS.add("e");
        BOARDS.add("f");
        BOARDS.add("g");
        BOARDS.add("h");
        BOARDS.add("hr");
        BOARDS.add("k");
        BOARDS.add("m");
        BOARDS.add("o");
        BOARDS.add("p");
        BOARDS.add("q");
        BOARDS.add("r");
        BOARDS.add("s");
        BOARDS.add("t");
        BOARDS.add("u");
        BOARDS.add("v");
        BOARDS.add("vg");
        BOARDS.add("vr");
        BOARDS.add("w");
        BOARDS.add("wg");
        BOARDS.add("i");
        BOARDS.add("ic");
        BOARDS.add("r9k");
        BOARDS.add("s4s");
        BOARDS.add("vip");
        BOARDS.add("qa");
        BOARDS.add("cm");
        BOARDS.add("hm");
        BOARDS.add("lgbt");
        BOARDS.add("y");
        BOARDS.add("3");
        BOARDS.add("aco");
        BOARDS.add("adv");
        BOARDS.add("an");
        BOARDS.add("asp");
        BOARDS.add("bant");
        BOARDS.add("biz");
        BOARDS.add("cgl");
        BOARDS.add("ck");
        BOARDS.add("co");
        BOARDS.add("diy");
        BOARDS.add("fa");
        BOARDS.add("fit");
        BOARDS.add("gd");
        BOARDS.add("hc");
        BOARDS.add("his");
        BOARDS.add("int");
        BOARDS.add("jp");
        BOARDS.add("lit");
        BOARDS.add("mlp");
        BOARDS.add("mu");
        BOARDS.add("n");
        BOARDS.add("news");
        BOARDS.add("out");
        BOARDS.add("po");
        BOARDS.add("pol");
        BOARDS.add("qst");
        BOARDS.add("sci");
        BOARDS.add("soc");
        BOARDS.add("sp");
        BOARDS.add("tg");
        BOARDS.add("toy");
        BOARDS.add("trv");
        BOARDS.add("tv");
        BOARDS.add("vp");
        BOARDS.add("wsg");
        BOARDS.add("wsr");
        BOARDS.add("x");
        BOARDS.add("gif");
        BOARDS.add("trash");
    }

    private MusicPlayer player;

    public ChanCommand(MusicPlayer player)
    {
        this.player = player;
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        try
        {
            String board;
            if (args.length > 1 && BOARDS.contains(args[1].toLowerCase()))
            {
                board = args[1].toLowerCase();
            }
            else if (args[1].toLowerCase().equals("poem"))
            {
                String content = "```css\n" + getPoem() + "```";
                channel.sendMessage(content).queue();
                return;
            }
            else
            {
                board = BOARDS.get(r.nextInt(BOARDS.size()));
            }
            String post = getRandomPost(getRandomThread(board));
            String content = "**/" + board + "/**```css\n" + post + "```";
            channel.sendMessage(content).queue();
            GuildVoiceState voiceState = event.getMember().getVoiceState();
            if (voiceState == null)
            {
                return;
            }
            String voiceChannel = voiceState.getChannel().getName();
            String url = TextUtils.messageToTTSLink(post);
            System.out.println(url);
            player.loadAndPlay(event.getTextChannel(), url, voiceChannel, null);
        }
        catch (IOException | JSONException e)
        {
            e.printStackTrace();
            channel.sendMessage("Error while accessing the 4chan API.").queue();
        }
    }

    private String getPoem()
    {
        StringBuilder sb = new StringBuilder();
        int stanzas = 2 + r.nextInt(2);

        for (int i = 0; i < stanzas; i++)
        {
            try
            {
                String board = BOARDS.get(r.nextInt(BOARDS.size()));
                int numLines = 3 + r.nextInt(3);
                for (int j = 0; j < numLines; j++)
                {
                    JSONArray thread = getRandomThread(board);

                    while (thread.length() < 75)
                    {
                        thread = getRandomThread(board);
                    }

                    String line;
                    int count = 0;
                    while (true)
                    {
                        count++;
                        line = getRandomPost(thread);
                        int len = line.split(" ").length;
                        if (!line.contains(">>") && !line.contains("http") && (len >= 3 && len <= 12))
                        {
                            break;
                        }
                        if (count == 100)
                        {
                            line = "epic fail";
                            break;
                        }
                    }
                    sb.append(line).append("\n");
                }
            }
            catch (Exception e)
            {
                i--;
            }
            sb.append("\n");
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

    private JSONArray getRandomThread(String board) throws IOException, JSONException
    {
        String url = API + board + "/threads.json";
        JSONArray threads = JsonUtils.readJsonArrayWithoutSaving(url);
        int pageNumber = r.nextInt(10);
        int threadNumber = r.nextInt(15);
        int threadID = threads.getJSONObject(pageNumber).getJSONArray("threads")
                .getJSONObject(threadNumber).getInt("no");
        url = "http://a.4cdn.org/" + board + "/thread/" + threadID + ".json";

        return JsonUtils.readJsonObjectWithoutSaving(url).getJSONArray("posts");
    }

    private String getRandomPost(JSONArray thread)
    {
        String content = null;
        for (int i = 0; i < 5; i++)
        {
            int postNumber = r.nextInt(thread.length());
            try
            {
                content = thread.getJSONObject(postNumber).getString("com");
                break;
            }
            catch (JSONException ignored)
            {
            }
        }
        if (content == null)
            return "";
        return TextUtils.htmlToPlainText(content);
    }

    @Override
    public int getCooldown()
    {
        return 1;
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "chan");
    }

    @Override
    public String getDescription()
    {
        return "```" +
                pf + "chan\n" +
                "Returns a random post from 4chan.\n\n" +
                pf + "chan <board>\n" +
                "Returns a random post from specified board.\n\n" +
                pf + "chan poem\n" +
                "Returns a poem compiled of random posts." +
                "```";
    }
}
