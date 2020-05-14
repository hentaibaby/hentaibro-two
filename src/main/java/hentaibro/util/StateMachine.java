package hentaibro.util;

import net.dv8tion.jda.api.events.message.GenericMessageEvent;

public interface StateMachine
{
    boolean updateState(GenericMessageEvent event);
}
