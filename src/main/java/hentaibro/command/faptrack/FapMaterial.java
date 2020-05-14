package hentaibro.command.faptrack;

import java.util.List;

public abstract class FapMaterial
{
    protected List<Tag> degenTags;
    protected String id;
    protected String title;
    protected List<Tag> allTags;
    protected double degeneracyIndex;
    protected long dateRecorded;
    protected List<String> characters;

    public List<Tag> getDegenTags()
    {
        return degenTags;
    }

    public String getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public List<Tag> getAllTags()
    {
        return allTags;
    }

    public double getDegeneracyIndex()
    {
        return degeneracyIndex;
    }

    public long getDateRecorded()
    {
        return dateRecorded;
    }

    public void setDateRecorded(long dateRecorded)
    {
        this.dateRecorded = dateRecorded;
    }

    public List<String> getCharacters()
    {
        return characters;
    }
}