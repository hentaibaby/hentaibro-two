package hentaibro.command;

import hentaibro.command.faptrack.Tag;
import hentaibro.command.faptrack.hentai.Doujin;
import hentaibro.command.nightcore.player.MusicPlayer;
import hentaibro.database.DAOFactory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestCommand extends Command
{
    private MusicPlayer player;

    public TestCommand(MusicPlayer player)
    {
        this.player = player;
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        List<User> mentioned = event.getMessage().getMentionedUsers();
        User usr = (mentioned.size() == 0) ? event.getAuthor() : mentioned.get(0);
        List<Doujin> doujins = DAOFactory.getHentaiDao().getUserDoujins(usr.getId());

        Tag t = new Tag("webtoon", 5);
        List<Doujin> wt = new ArrayList<>();
        for (Doujin d : doujins)
        {
            if (d.getAllTags().contains(t))
                wt.add(d);
        }

        StringBuilder sb = new StringBuilder();
        for (Doujin d : wt)
            sb.append(d.getTitle()).append("     ")
                    .append("https://nhentai.net/g/").append(d.getId()).append("\n");

        channel.sendMessage("```nigger" + sb.toString() + "```").queue();

        /*
        Bot.api.getGuildById("500894688501563404").leave().queue();
        try
        {
            String voiceChannel = event.getMember().getVoiceState().getChannel().getName();
            List<NightcoreSong> songs = DAOFactory.getNCDAO().getAllSongs();
            for (NightcoreSong s : songs)
            {
                System.out.println("[" + s.title + "] (" + s.link + ")");
                player.loadAndPlay(event.getTextChannel(), s.link, voiceChannel, null);
                Thread.sleep(3000);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        List<User> mentioned = event.getMessage().getMentionedUsers();
        User usr = (mentioned.size() == 0) ? event.getAuthor() : mentioned.get(0);
        List<String> characters = new ArrayList<>();
        List<String> parodies = new ArrayList<>();
        DAOFactory.getHentaiDao().getUserDoujins(usr.getId()).forEach(d ->
        {
            if (d.getParody() != null)
            {
                parodies.add(d.getParody());
            }
            characters.addAll(d.getCharacters());
        });

        Map<String, Integer> characterMap = new HashMap<>();
        Map<String, Integer> parodyMap = new HashMap<>();

        characters.forEach(s ->
        {
            if (characterMap.containsKey(s))
            {
                characterMap.put(s, characterMap.get(s) + 1);
            }
            else
            {
                characterMap.put(s, 1);
            }
        });

        for (String s : parodies)
        {
            if (parodyMap.containsKey(s))
            {
                parodyMap.put(s, parodyMap.get(s) + 1);
            }
            else
            {
                parodyMap.put(s, 1);
            }
        }

        Map<String, Integer> sortedParodies = parodyMap
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        Map<String, Integer> sortedCharacters = characterMap
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        StringBuilder sc = new StringBuilder().append("top characters\n");
        StringBuilder sp = new StringBuilder().append("top parodies\n");
        List<Map.Entry<String, Integer>> just = new ArrayList<>(sortedCharacters.entrySet());
        List<Map.Entry<String, Integer>> fam = new ArrayList<>(sortedParodies.entrySet());

        for (int i = 1; i < 11; i++)
        {
            Map.Entry<String, Integer> e = just.get(just.size() - i);
            Map.Entry<String, Integer> d = fam.get(fam.size() - i);

            sc.append(e.getKey()).append(" ").append(e.getValue()).append("\n");
            sp.append(d.getKey()).append(" ").append(d.getValue()).append("\n");
        }

        channel.sendMessage("```" + sc.toString() + "\n" + sp.toString() + "```").queue();
        */
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "webtoon");
    }

    @Override
    protected boolean requireWhitelist()
    {
        return true;
    }

    @Override
    public String getDescription()
    {
        return null;
    }
}
