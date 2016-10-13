package EREditor;

/**
 * Created by LangChen on 2016/10/10.
 */
public class EREditMath {
    public static Point RawToDisplay(Point rawPosition, Point offset, double multiplicity) {
        // Coordinates system transfer - Position
        rawPosition = rawPosition.Subtract(offset);
        rawPosition = rawPosition.Multiply(multiplicity);
        return rawPosition;
    }

    public static Size RawToDisplay(Size rawSize, double multiplicity){
        // Coordinates system transfer - Size
        return rawSize.Multiply(multiplicity);
    }

    public static Point DisplayToRaw(Point displayPosition, Point offset, double multiplicity) {
        // Coordinates system transfer - Position
        displayPosition = displayPosition.Add(offset);
        displayPosition = displayPosition.Multiply(1.0d / multiplicity);
        return displayPosition;
    }

    public static Size DisplayToRaw(Size displaySize, double multiplicity){
        // Coordinates system transfer - Size
        return displaySize.Multiply(1.0d / multiplicity);
    }

    public static String GetId(String suffix) {
        StringBuilder sb = new StringBuilder(100);
        sb.append(System.currentTimeMillis());
        if(suffix == null) { suffix = "NULL"; }
        sb.append(suffix);

        return sb.toString();
    }
}
