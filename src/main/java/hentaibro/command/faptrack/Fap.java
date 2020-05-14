package hentaibro.command.faptrack;

public class Fap
{
    private FapMaterial fapMaterial;
    private long timestamp;

    public Fap(FapMaterial fapMaterial, long timestamp)
    {
        this.fapMaterial = fapMaterial;
        this.timestamp = timestamp;
    }

    public FapMaterial getFapMaterial()
    {
        return fapMaterial;
    }

    public long getTimestamp()
    {
        return timestamp;
    }
}
