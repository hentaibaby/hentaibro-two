package hentaibro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import hentaibro.command.faptrack.Tag;
import hentaibro.util.JsonUtils;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SettingsManager
{

    private static JSONObject settingsObj;
    private static Map<String, Tag> tagMap;

    public static void loadSettings()
    {
        settingsObj = loadFromFile();
        Settings.botToken = settingsObj.getString("token");
        Settings.googleAPI = settingsObj.getString("googleAPI");
        loadLists();
    }

    private static void loadLists()
    {
        try
        {
            Settings.whitelist = Files.readAllLines(new File(Bot.directory + "/whitelist.txt").toPath(),
                    Charset.defaultCharset());
            Settings.blacklist = Files.readAllLines(new File(Bot.directory + "/blacklist.txt").toPath(),
                    Charset.defaultCharset());

            File file = new File(Bot.directory + "/degentags.txt");
            Scanner reader = new Scanner(file);
            tagMap = new HashMap<>();

            while (reader.hasNext())
            {
                String[] tmp = reader.nextLine().split(" ", 2);
                tagMap.put(tmp[1], new Tag(tmp[1], Integer.parseInt(tmp[0])));
            }

            Settings.degMap = tagMap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static void appendStrToFile(String fileName, String str)
    {
        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            out.write(str.trim() + "\n");
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static JSONObject loadFromFile()
    {
        return new JSONObject(JsonUtils.readFile(Bot.directory + "/config.json"));
    }

    public static void addToBlackList(String id)
    {
        appendStrToFile(Bot.directory + "/blacklist.txt", id);
        loadLists();
    }

    public static void addToWhiteList(String id)
    {
        appendStrToFile(Bot.directory + "/whitelist.txt", id);
        loadLists();
    }

    public static void addDegenTag(String tag)
    {
        appendStrToFile(Bot.directory + "/degentags.txt", tag);
        loadLists();
        settingsObj.remove("degenTags");
        settingsObj.put("degenTags", tagMap);
        try (FileWriter writer = new FileWriter(Bot.directory + "/config.json"))
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(settingsObj.toString());
            writer.write(gson.toJson(je));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
