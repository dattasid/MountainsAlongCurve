package curve;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

//import awtutils.ImageOps;

public class MountainsAlongCurve
{


    
    public static void main(String[] args) throws IOException
    {
        BufferedImage im = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        
        Random rand = new Random();
        
        Bezier b1 = new Bezier(
                10+rand.nextInt(400), rand.nextInt(400),
                10+rand.nextInt(400), rand.nextInt(400),
                10+rand.nextInt(400), rand.nextInt(400),
                10+rand.nextInt(400), rand.nextInt(400)
                );
        
//        b1 = new Bezier(
//                50, 10, 200, 10, 350, 100, 350, 200
//                );
//        
        Graphics2D g2 = im.createGraphics();
        g2.setColor(Color.black);
        g2.fillRect(0, 0, 400, 400);
        
////        drawBez(b1, g2);
////        drawBezLine(b1, g2);
//        
        Bezier[] ba = offset(b1, 10, 30);
////        drawBez(ba[0], g2);
////        drawBez(ba[1], g2);
////        drawBezLine(b2, g2);
//        
        drawBezTri(b1, ba[0], g2, im, false);
        drawBezTri(b1, ba[1], g2, im, true);
//        
//        b1 = new Bezier(
//                  350, 210, 350, 300, 200, 370, 200, 200
//                  );
//        ba = offset(b1, 10, 20);
//      
//      drawBezTri(b1, ba[0], g2, im, false);
//      drawBezTri(b1, ba[1], g2, im, true);
        
//      b1 = new Bezier(
//      20, 20, 350, 20, 20, 350, 350, 350
//      );
//      Bezier[] ba = offset(b1, 40, 50);
//    
//      drawBezTri(b1, ba[0], g2, im, false);
//      drawBezTri(b1, ba[1], g2, im, true);
        
//        ImageOps.showImage(im);
//        ImageOps.exitInSecs(10);
        
        ImageIO.write(im, "png", new File("a.png"));
        
    }

    static Pointf[] tri = new Pointf[3];
    private static void drawBezTri(Bezier b1, Bezier b2, Graphics2D g2,
            BufferedImage im, boolean neg)
    {
        Pointf p2 = null, p3 = null;
        boolean which = false;
        float d = .02f;
        for (float t = 0; t < 1+d; t+=d)
        {
            Pointf p1 = new Pointf();
            if (which)
            {
                b1.eval_(t, p1);
                p1.ux = t;
                p1.uy = 0;
            }
            else
            {
                b2.eval_(t, p1);
                p1.ux = t;
                p1.uy = neg?-1:1;
            }
        
            which = !which;
            
            if (p2 != null && p3 != null)
            {
//                g2.drawLine(x0, y0, x1, y1);
//                g2.drawLine(x1, y1, (int)p1.x, (int)p1.y);
//                g2.drawLine(x0, y0, (int)p1.x, (int)p1.y);
                tri[0] = p1;
                tri[1] = p2;
                tri[2] = p3;
                renderTri(im, tri);
            }
            p3 = p2;
            p2 = p1;
        }
    }
    
    /**
     * Render the triangle given by the 3 points, using texture coordinates
     * given in the points.
     * @param im
     * @param v
     */
    static void renderTri(BufferedImage im, Pointf[] v)
    {
       
        int ymin = (int) Math.min(v[0].y, Math.min(v[1].y, v[2].y));
        int ymax = (int) Math.max(v[0].y, Math.max(v[1].y, v[2].y));
        
        ArrayList<Integer> xa = new ArrayList<Integer>(3);
        int W = im.getWidth();
        int H = im.getHeight();
        
        for (int y = ymin; y <= ymax; y++)
        {
//            System.out.println(y);
            xa.clear();
            
            for (int i = 0; i < 3; i++)
            {
                Pointf v1 = v[i];
                Pointf v2 = v[(i+1)%3];
                
                if (v1.y > v2.y)
                {
                    Pointf t = v1; v1 = v2; v2 = t;
                }
                
                if (y < v1.y || y >= v2.y)
                {
                    continue;
                }
                double xx = v1.x + (y - v1.y)*(v2.x-v1.x)*1./(v2.y-v1.y);// TODO
                int xx1 = (int) Math.round(xx);
                xa.add(xx1);
//                if (y == 60)
//                    System.out.println("==== "+v1+" "+v2+" > "+xx1+" "+xx);
            }

            if (xa.size() != 2)
            {
//                System.out.println("Skip "+xa);
                continue;
            }
            
            int x1 = xa.get(0), x2 = xa.get(1);
            if (x1 > x2)
            {
                int t = x1; x1 = x2; x2 = t;
            }
            
//            System.out.println("- "+x1+" "+x2);
            
            for (int x = x1+1; x <= x2; x++)
            {
                
                if (x < 0 || y < 0 || x >= W || y >= H)
                    continue;
                
                float den = (v[1].y-v[2].y)*(v[0].x-v[2].x) + (v[0].y-v[2].y)*(v[2].x-v[1].x);
                float l1 = ((v[1].y-v[2].y)*(x-v[2].x) + (y-v[2].y)*(v[2].x-v[1].x))*1f/den;
                
                float l2 = ((v[2].y-v[0].y)*(x-v[2].x) + (y-v[2].y)*(v[0].x-v[2].x))*1f/den;
                
                float l3 = 1-l1-l2;
                
//                if (l1 < 0 || l2 < 0 || l3 < 0 || l1 > 1 || l2 > 1 || l3 > 1)
//                    System.out.println("ERR "+l1+" "+l2+" "+l3);
                
                float ux = l1 * v[0].ux + l2 * v[1].ux + l3 * v[2].ux;
                float uy = l1 * v[0].uy + l2 * v[1].uy + l3 * v[2].uy;
                
                double n = ridged(ux*2  *3, uy * 2);
//                double n = siny(ux, uy*5);
                
                n*=(1-Math.abs(uy));
                if (ux < .2f)
                {
                    n*=ux/.2f;
                }
                if (ux > .8f)
                {
                    n*=(1-ux)/.2f;
                }
                
                if (n < 0) n = 0;
                if (n > 1) n = 1;
                
                int col1 = (int) (n*255); 
                int col2 = col1 * 0x010101+0xff000000;
                im.setRGB(x, y, col2);
            }
            
        }
        
//        for (int x = 0; x < 400; x++)
//            for (int y = 0; y < 400; y++)
//            {
//                double n = ridged(x/400., y/400.);
//                
//                int col1 = (int) (n*255); 
//                int col2 = col1 * 0x010101+0xff000000;
//                im.setRGB(x, y, col2);
//            }
    }
    
    public static double siny(double x, double y)
    {
        return (1+Math.sin(y*5))/2;
    }
    
    static double OX = Math.random()*100, OY = Math.random()*100;
    /**
     * Tidged perlin.
     * @param x
     * @param y
     * @return
     */
    public static double ridged(double x, double y)
    {
        double n = 0;
        double fac = 1;
        for (int i = 0; i < 3; i++)
        {
            n += (1-Math.abs(ImprovedPerlin.noise(x+OX, y+OY, .5f))) * fac;
            fac *= .5;
            x*=2; y*=2;
        }
        
//        n *= .57;/* for 3 oct */
        n = n*.5;
        n -= .5;
        n *= 2.3;
        
        return n;
    }
    
    /**
     * Draw the control points as line segments.
     * @param b
     * @param g2
     */
    private static void drawBezLine(Bezier b, Graphics2D g2)
    {
        g2.drawLine((int)b.x1, (int)b.y1, (int)b.x2, (int)b.y2);
        g2.drawLine((int)b.x2, (int)b.y2, (int)b.x3, (int)b.y3);
        g2.drawLine((int)b.x3, (int)b.y3, (int)b.x4, (int)b.y4);
    }
    
    /**
     * Draw the bezier curve
     * @param b
     * @param g2
     */
    private static void drawBez(Bezier b, Graphics2D g2)
    {
        Pointf p1 = new Pointf();
        int x0 = -1, y0 = -1;
        for (float i = 0; i < 1.05; i+=.05)
        {
            b.eval_(i, p1);
            if (x0 > 0)
                g2.drawLine(x0, y0, (int)p1.x, (int)p1.y);
            x0 = (int) p1.x;
            y0 = (int) p1.y;
        }
    }
    
    /**
     * Calculate the approximate offset curve to the given bezier in both directions. 
     * @param b
     * @param dist1
     * @param dist2
     * @return
     */
    public static Bezier[] offset(Bezier b, int dist1, int dist2)
    {
        Pointf n1 = norm_n(b.x1, b.y1, b.x2, b.y2);
        Pointf n2 = offsetPt(b.x1, b.y1, b.x2, b.y2, b.x3, b.y3);
        Pointf n3 = offsetPt(b.x2, b.y2, b.x3, b.y3, b.x4, b.y4);
        Pointf n4 = norm_n(b.x3, b.y3, b.x4, b.y4);
        
                
        return new Bezier[]{
                new Bezier(b.x1+n1.x*dist1, b.y1+n1.y*dist1,
                        b.x2+n2.x*dist2, b.y2+n2.y*dist2,
                        b.x3+n3.x*dist2, b.y3+n3.y*dist2,
                        b.x4+n4.x*dist1, b.y4+n4.y*dist1
                        ),
                new Bezier(b.x1-n1.x*dist1, b.y1-n1.y*dist1,
                        b.x2-n2.x*dist2, b.y2-n2.y*dist2,
                        b.x3-n3.x*dist2, b.y3-n3.y*dist2,
                        b.x4-n4.x*dist1, b.y4-n4.y*dist1
                        ),
        };
        
    }
    
    /**
     * Calculate the normal to a line defined by 2 points
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static Pointf norm_n(float x1, float y1, float x2, float y2)
    {
        float v1x = x2-x1;
        float v1y = y2-y1;
        
        float n1x = v1y;
        float n1y = -v1x;
        
        float l = (float) Math.sqrt(n1x*n1x+n1y*n1y);
        
        return new Pointf(n1x/l, n1y/l);
    }
    
    /**
     * Calculate the average normal to two line segments given by 3 points.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @return
     */
    public static Pointf offsetPt(float x1, float y1, float x2, float y2, float x3, float y3)
    {
        float v1x = x2-x1;
        float v1y = y2-y1;
        
        float n1x = v1y;
        float n1y = -v1x;
        
        float v2x = x3-x2;
        float v2y = y3-y2;
        
        float n2x = v2y;
        float n2y = -v2x;
        
        float nx = n1x + n2x;
        float ny = n1y + n2y;
        
        float l = (float) Math.sqrt(nx*nx+ny*ny);
        
        return new Pointf(nx/l, ny/l);
    }
    
    public static float dist(float x1, float y1, float x2, float y2)
    {
        float dx = x1-x2;
        float dy = y1-y2;
        return (float) Math.sqrt(dx*dx+dy*dy);
    }
    

    

}
