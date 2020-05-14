package hentaibro.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SubReader
{

    public static List<Subtitle> read(String path) throws Exception
    {

        File file = new File(path);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

        List<Subtitle> subs = new ArrayList<>();
        String line;

        long prev = 0;

        while ((line = reader.readLine()) != null)
        {
            if (!line.equals("") && line.contains("-->"))
            {

                //spit timecode line where index 0 is start and 2 is end
                String[] tokens = line.split(" ");
                String[] zero = tokens[0].split(":");

                long start = convertStringToMillis(zero);
                StringBuilder sb = new StringBuilder();
                String temp;

                while ((temp = reader.readLine()) != null)
                {
                    if (temp.equals(""))
                        break;

                    sb.append(temp);
                    sb.append("\n");

                }

                subs.add(new Subtitle(sb.toString().trim(), start - prev));

                prev = start;

            }

        }
        return subs;
    }

    private static long convertStringToMillis(String[] data)
    {
        int hours = Integer.parseInt(data[0]);
        int minutes = Integer.parseInt(data[1]);
        double seconds = Double.parseDouble(data[2]);
        double time = 1000 * (seconds + 60 * minutes + 3600 * hours);

        return (long) time;
    }


}
