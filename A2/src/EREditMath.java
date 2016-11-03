import java.util.Map;

/**
 * Created by LangChen on 2016/10/10.
 */
public class EREditMath {
    public final static double DZero = 0.00001d;

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

    public static double CalculateLength(Point startPos, Point endPos) {
        int relativeX = endPos.X - startPos.X;
        int relativeY = endPos.Y - startPos.Y;
        double length = Math.sqrt(Math.pow(relativeX, 2.0d) + Math.pow(relativeY, 2.0d));

        return length;
    }

    public static double CalculateAngle(Point startPos, Point endPos) {
        // return 0~2pi
        // error value < 0.0d

        int relativeX = endPos.X - startPos.X;
        int relativeY = endPos.Y - startPos.Y;
        double length = Math.sqrt(Math.pow(relativeX, 2.0d) + Math.pow(relativeY, 2.0d));

        if(relativeX == 0 && relativeY == 0) return -1.0d;  // error

        double valSin = Math.abs(Math.asin(relativeY / length));

        // Determine the quadrant
        if(relativeX >= 0 && relativeY >= 0){
            return valSin;
        }
        else if(relativeX < 0 && relativeY >= 0){
            return Math.PI - valSin;
        }
        else if(relativeX < 0 && relativeY < 0){
            return Math.PI + valSin;
        }
        else {
            return 2.0d * Math.PI - valSin;
        }
    }

    public static Point[] CalculateArrowPoints(double lineAngle, double arrowAngle, double length, Point endPos){
        Point[] pointList = new Point[3];
        pointList[1] = new Point(endPos.X, endPos.Y);

        double normalAngle = lineAngle + Math.PI;
        double leftPointAngle = normalAngle - arrowAngle;
        double rightPointAngle = normalAngle + arrowAngle;

        // Left Point
        double leftPoint_XOffset = length * Math.cos(leftPointAngle);
        double leftPoint_YOffset = length * Math.sin(leftPointAngle);
        pointList[0] = new Point((int)(endPos.X + leftPoint_XOffset), (int)(endPos.Y + leftPoint_YOffset));

        // Right Point
        double rightPoint_XOffset = length * Math.cos(rightPointAngle);
        double rightPoint_YOffset = length * Math.sin(rightPointAngle);
        pointList[2] = new Point((int)(endPos.X + rightPoint_XOffset), (int)(endPos.Y + rightPoint_YOffset));

        return pointList;
    }

    public static int[] DecompositePoints(Point[] pointList, boolean isX){
        int[] posList = new int[pointList.length];

        for(int i = 0; i < pointList.length; ++i){
            posList[i] = isX ? pointList[i].X : pointList[i].Y;
        }

        return posList;
    }

    public static EREditDrawArrow.DIRECTION DetermineDirection(Point startBoxPos, Point endBoxPos, boolean isStart){
        int Xdifference = endBoxPos.X - startBoxPos.X;
        int Ydifference = endBoxPos.Y - startBoxPos.Y;

        if(Math.abs(Xdifference) > Math.abs(Ydifference)){
            // Choose Left & Right
            if(Xdifference >= 0){
                return (isStart) ? EREditDrawArrow.DIRECTION.RIGHT : EREditDrawArrow.DIRECTION.LEFT;
            }
            else{
                return (isStart) ? EREditDrawArrow.DIRECTION.LEFT : EREditDrawArrow.DIRECTION.RIGHT;
            }

        }
        else {
            // Choose Top & Bottom
            if(Ydifference >= 0){
                return isStart ? EREditDrawArrow.DIRECTION.DOWN : EREditDrawArrow.DIRECTION.UP;
            }
            else {
                return isStart ? EREditDrawArrow.DIRECTION.UP : EREditDrawArrow.DIRECTION.DOWN;
            }

        }
    }
}
