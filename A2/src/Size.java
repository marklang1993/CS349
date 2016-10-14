/**
 * Created by LangChen on 2016/9/26.
 */
public class Size
{
    public int Width;
    public int Height;

    Size(Size size)
    {
        Width = size.Width;
        Height = size.Height;
    }

    Size(int width, int height){
        Width = width;
        Height = height;
    }

    Size Add(int offset) { return new Size(Width + offset, Height + offset); }

    Size Multiply(double multiplicity) { return new Size ((int)(Width * multiplicity), (int)(Height * multiplicity)); }
}
