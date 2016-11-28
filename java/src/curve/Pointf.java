package curve;

public class Pointf
{
    public float x, y;
    public float ux=0, uy=0;
    public Pointf()
    {
        this(0, 0);
    }

    public Pointf(float x, float y)
    {
        super();
        this.x = x;
        this.y = y;
    }
}