package curve;


public class Bezier
{
    public final float x1, y1, x2, y2, x3, y3, x4, y4;
    public final float c1x, c1y, c2x, c2y, c3x, c3y, c4x, c4y;

    public Bezier(float x1, float y1, float x2, float y2, float x3, float y3,
            float x4, float y4)
    {
        this.x1 = x1;
        this.y1 = y1;

        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.x4 = x4;
        this.y4 = y4;

        c1x = x4 + 3 * (x2 - x3) - x1;
        c1y = y4 + 3 * (y2 - y3) - y1;

        c2x = 3 * (x1 - 2 * x2 + x3);
        c2y = 3 * (y1 - 2 * y2 + y3);

        c3x = 3 * (x2 - x1);
        c3y = 3 * (y2 - y1);

        c4x = x1;
        c4y = y1;

    }

    public void eval_(float t, Pointf out)
    {
        float t2 = t * t;
        float t3 = t2 * t;

        out.x = c1x * t3 + c2x * t2 + c3x * t + c4x;
        out.y = c1y * t3 + c2y * t2 + c3y * t + c4y;

    }
}