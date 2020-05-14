package hentaibro.util;

public class Subtitle
{

    private final String content;
    private final long delay;

    Subtitle(String content, long delay)
    {
        this.content = content;
        this.delay = delay;
    }

    public String getContent()
    {
        return content;
    }

    public long getDelay()
    {
        return delay;
    }

    @Override
    public String toString()
    {
        return delay + " " + content;
    }
}
