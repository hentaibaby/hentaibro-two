package hentaibro.command.meme;

import hentaibro.command.Command;
import hentaibro.util.TextUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ScannerThread extends Command
{
    private Sender sender;


    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        if (sender != null)
        {
            sender.interrupt();
            sender = null;
        }

        sender = new Sender(channel);
        sender.start();
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList("*scan");
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    protected boolean requireWhitelist()
    {
        return true;
    }

    private class Sender extends Thread
    {
        private final Scanner scanner;
        private final MessageChannel channel;

        Sender(MessageChannel channel)
        {
            this.channel = channel;
            scanner = new Scanner(System.in);
        }

        @Override
        public void run()
        {
            while (true)
            {
                String s = scanner.nextLine();

                String meme = TextUtils.emojify(s);

                if (meme.equals("Your message is too long!"))
                    System.out.println("too long");
                else
                    channel.sendMessage(meme).queue();
            }
        }
    }
}