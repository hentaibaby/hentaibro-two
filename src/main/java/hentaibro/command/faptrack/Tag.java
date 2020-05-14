package hentaibro.command.faptrack;

import java.util.Objects;

public class Tag
{
    private final String name;
    private final int score;

    public Tag(String name, int score)
    {
        this.name = name;
        this.score = score;
    }

    public String getName()
    {
        return name;
    }

    public int getScore()
    {
        return score;
    }

    @Override
    public String toString()
    {
        return score + " " + name;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Tag))
            return false;
        return (((Tag) obj).getName().equals(name));
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, score);

    }

}
