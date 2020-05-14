package hentaibro.command.general;

import hentaibro.command.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShellCommand extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        try
        {
            args = Arrays.copyOfRange(args, 1, args.length);
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(args);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            StringBuilder out = new StringBuilder();
            String s;
            while ((s = stdInput.readLine()) != null)
            {
                out.append(s).append("\n");
            }
            StringBuilder err = new StringBuilder();
            while ((s = stdError.readLine()) != null)
            {
                err.append(s).append("\n");
            }
            String output = out.toString();
            if (output.length() >= 2000)
            {
                output = output.substring(0, 2000);
            }
            String error = err.toString();
            if (err.length() >= 2000)
            {
                error = error.substring(0, 2000);
            }
            if (output.isEmpty())
            {
                output = "no out";
            }
            if (error.isEmpty())
            {
                error = "no err";
            }
            channel.sendMessage(output).queue();
            channel.sendMessage(error).queue();
        }
        catch (IOException e)
        {
            channel.sendMessage(e.getMessage()).queue();
        }
    }

    @Override
    protected boolean requireWhitelist()
    {
        return true;
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "zsh");
    }

    @Override
    public String getDescription()
    {
        return null;
    }
}
