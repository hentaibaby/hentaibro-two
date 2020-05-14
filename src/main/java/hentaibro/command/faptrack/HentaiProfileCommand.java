package hentaibro.command.faptrack;

import hentaibro.command.Command;
import hentaibro.command.faptrack.hentai.Doujin;
import hentaibro.database.DAOFactory;
import hentaibro.database.HentaiDAO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class HentaiProfileCommand extends Command
{

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        List<User> mentioned = event.getMessage().getMentionedUsers();
        User usr = (mentioned.size() == 0) ? event.getAuthor() : mentioned.get(0);
        channel.sendMessage(getInfoEmbed(usr)).queue();
    }

    private MessageEmbed getInfoEmbed(User usr)
    {
        String uid = usr.getId();
        HentaiDAO dao = DAOFactory.getHentaiDao();
        List<Doujin> results = dao.getUserDoujins(uid);
        List<Tag> tags = dao.getUserTags(uid);

        //calculate mean degeneracy and count total pages
        double dgi = 0;
        int pages = 0;

        for (Doujin res : results)
        {
            dgi += res.getDegeneracyIndex();
            pages += res.getPages();
        }

        dgi /= results.size();

        //create top tags strings

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tags.size() / 2; i++)
        {
            sb.append(tags.get(i).getName());
            sb.append(" (");
            sb.append(tags.get(i).getScore());
            sb.append(")\n");
        }

        String favs = sb.toString().trim();
        sb = new StringBuilder();

        for (int i = tags.size() / 2; i < tags.size(); i++)
        {
            sb.append(tags.get(i).getName());
            sb.append(" (");
            sb.append(tags.get(i).getScore());
            sb.append(")\n");
        }

        String favs2 = sb.toString().trim();
        String artist = dao.getFavoriteArtist(uid);

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

        List<Map.Entry<String, Integer>> just = new ArrayList<>(sortedCharacters.entrySet());
        List<Map.Entry<String, Integer>> fam = new ArrayList<>(sortedParodies.entrySet());

        String sc = just.get(just.size() - 1).getKey();
        String sp = fam.get(fam.size() - 1).getKey();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(1327491))
                .setAuthor(usr.getName() + "#" + usr.getDiscriminator(),
                        null, usr.getAvatarUrl())
                .setThumbnail(usr.getAvatarUrl())
                .addField("Favorite Tags", favs, true)
                .addField("\u200B", favs2, true)
                .addField("Mean Degeneracy Index", Double.toString(dgi), true)
                .addField("Favorite Artist", artist, true)
                .addField("Favorite Character", sc, true)
                .addField("Favorite Parody", sp, true)
                .addField("Doujins Recorded", "" + results.size(), true)
                .addField("Pages Read", "" + pages, true);

        return eb.build();
    }


    @Override
    public String getDescription()
    {
        return "```*hprofile <@user>\n\n" +
                "Returns statistics based on all doujins a user a has recorded with *faptrack.```";
    }

    @Override
    public List<String> getAliases()
    {
        return Arrays.asList(pf + "hprofile", pf + "hp");
    }
}
