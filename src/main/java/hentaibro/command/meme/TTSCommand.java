package hentaibro.command.meme;

import hentaibro.command.Command;
import hentaibro.command.nightcore.player.MusicPlayer;
import hentaibro.util.TextUtils;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

public class TTSCommand extends Command
{
    private MusicPlayer player;

    public TTSCommand(MusicPlayer player)
    {
        this.player = player;
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        if (args.length < 2)
        {
            printInvalidSyntax();
            return;
        }
        GuildVoiceState voiceState = event.getMember().getVoiceState();
        if (voiceState == null)
        {
            return;
        }
        String msg = getMessageAsString(args);
        String voiceChannel = voiceState.getChannel().getName();
        String url = TextUtils.messageToTTSLink(msg);
        player.loadAndPlay(event.getTextChannel(), url, voiceChannel, null);
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "tts");
    }

    @Override
    public String getDescription()
    {
        return null;
    }
}
