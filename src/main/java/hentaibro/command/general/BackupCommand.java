package hentaibro.command.general;

import hentaibro.command.Command;
import hentaibro.database.BackupManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class BackupCommand extends Command
{
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        BackupManager.backupNow();
        channel.sendMessage("Backup Complete!").queue();
    }

    @Override
    protected boolean requireWhitelist()
    {
        return true;
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList("*backup");
    }

    @Override
    public String getDescription()
    {
        return "a";
    }
}
