package tetris;

/**
 * Created by LangChen on 2016/9/26.
 */
public class Point {
    public int X;
    public int Y;

    Point(int x, int y)
    {
        X = x;
        Y = y;
    }

    Point Add(Point p)
    {
        return new Point(X + p.X, Y + p.Y);
    }

    Point Add(int offset)
    {
        return new Point(X + offset, Y + offset);
    }

    Point Subtract(Point p)
    {
        return new Point(X - p.X, Y - p.Y);
    }

    Point Subtract(int offset)
    {
        return new Point(X - offset, Y - offset);
    }
}
