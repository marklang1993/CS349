import javax.swing.*;
import java.util.Map;

/**
 * Created by LangChen on 2016/10/10.
 */
public class EREditMath {

    public static class DirectionPair{
        public EREditDrawArrow.DIRECTION StartDirection;
        public EREditDrawArrow.DIRECTION EndDirection;
        public double Length;
    }

    public static class BoxVertex{
        public Point UpLeft;
        public Point UpRight;
        public Point DownRight;
        public Point DownLeft;
    }

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

    public static EREditDrawArrow.DIRECTION[] GetAdjecentDirections(EREditDrawArrow.DIRECTION reference){
        EREditDrawArrow.DIRECTION adjecentDirections[] = new EREditDrawArrow.DIRECTION[3];

        if(reference == EREditDrawArrow.DIRECTION.UP){
            adjecentDirections[0] = EREditDrawArrow.DIRECTION.LEFT;
            adjecentDirections[1] = EREditDrawArrow.DIRECTION.UP;
            adjecentDirections[2] = EREditDrawArrow.DIRECTION.RIGHT;
        }
        else if(reference == EREditDrawArrow.DIRECTION.RIGHT){
            adjecentDirections[0] = EREditDrawArrow.DIRECTION.UP;
            adjecentDirections[1] = EREditDrawArrow.DIRECTION.RIGHT;
            adjecentDirections[2] = EREditDrawArrow.DIRECTION.DOWN;
        }
        else if(reference == EREditDrawArrow.DIRECTION.DOWN){
            adjecentDirections[0] = EREditDrawArrow.DIRECTION.RIGHT;
            adjecentDirections[1] = EREditDrawArrow.DIRECTION.DOWN;
            adjecentDirections[2] = EREditDrawArrow.DIRECTION.LEFT;
        }
        else {
            adjecentDirections[0] = EREditDrawArrow.DIRECTION.UP;
            adjecentDirections[1] = EREditDrawArrow.DIRECTION.LEFT;
            adjecentDirections[2] = EREditDrawArrow.DIRECTION.DOWN;
        }
        return adjecentDirections;
    }

    public static Point GetArrowPosition(EREditDrawArrow.DIRECTION direction, Point startPos){
        // Return Raw Position
        Point offset;
        if (direction == EREditDrawArrow.DIRECTION.UP){
            offset = new Point(EREditDrawBox.SIZE.Width / 2, 0);
        }
        else if (direction == EREditDrawArrow.DIRECTION.DOWN){
            offset = new Point(EREditDrawBox.SIZE.Width / 2, EREditDrawBox.SIZE.Height);
        }
        else if (direction == EREditDrawArrow.DIRECTION.LEFT){
            offset = new Point(0, EREditDrawBox.SIZE.Height / 2);
        }
        else {
            offset = new Point(EREditDrawBox.SIZE.Width, EREditDrawBox.SIZE.Height / 2);
        }
        return startPos.Add(offset);
    }

    public static BoxVertex GetBoxVertex(Point startPos){
        BoxVertex boxVertex = new BoxVertex();

        boxVertex.UpLeft = new Point(startPos.X, startPos.Y);
        boxVertex.UpRight = new Point(startPos.X + EREditDrawBox.SIZE.Width, startPos.Y);
        boxVertex.DownLeft = new Point(startPos.X, startPos.Y + EREditDrawBox.SIZE.Height);
        boxVertex.DownRight = new Point(startPos.X + EREditDrawBox.SIZE.Width, startPos.Y + EREditDrawBox.SIZE.Height);

        return boxVertex;
    }

    public static DirectionPair DetermineDirection(Point startBoxPos, Point endBoxPos){
        int Xdifference = endBoxPos.X - startBoxPos.X;
        int Ydifference = endBoxPos.Y - startBoxPos.Y;

        EREditDrawArrow.DIRECTION startDirection;
        EREditDrawArrow.DIRECTION endDirection;




        // 1st Step: Approx. Get Directions
        if(Math.abs(Xdifference) > Math.abs(Ydifference)){
            // Choose Left & Right
            if(Xdifference >= 0){
                startDirection = EREditDrawArrow.DIRECTION.RIGHT;
                endDirection = EREditDrawArrow.DIRECTION.LEFT;
            }
            else{
                startDirection = EREditDrawArrow.DIRECTION.LEFT;
                endDirection = EREditDrawArrow.DIRECTION.RIGHT;
            }

        }
        else {
            // Choose Top & Bottom
            if(Ydifference >= 0){
                startDirection = EREditDrawArrow.DIRECTION.DOWN;
                endDirection = EREditDrawArrow.DIRECTION.UP;
            }
            else {
                startDirection = EREditDrawArrow.DIRECTION.UP;
                endDirection = EREditDrawArrow.DIRECTION.DOWN;
            }
        }

        // 2nd Step
        EREditDrawArrow.DIRECTION[] startDirectionAdjecentList = GetAdjecentDirections(startDirection);
        EREditDrawArrow.DIRECTION[] endDirectionAdjecentList = GetAdjecentDirections(endDirection);

        DirectionPair[] possibleDirectionList = new DirectionPair[9];

        for(int i = 0; i < 3; ++i){
            for(int j = 0; j < 3; ++j){
                int index = i * 3 + j;
                possibleDirectionList[index] = new DirectionPair();
                possibleDirectionList[index].StartDirection = startDirectionAdjecentList[i];
                possibleDirectionList[index].EndDirection = endDirectionAdjecentList[j];

                Point startArrowPosition = GetArrowPosition(possibleDirectionList[index].StartDirection, startBoxPos);
                Point endArrowPosition = GetArrowPosition(possibleDirectionList[index].EndDirection, endBoxPos);
                possibleDirectionList[index].Length = CalculateLength(startArrowPosition, endArrowPosition);
            }
        }

        // Find the tuple with minimum length
        double minLength = possibleDirectionList[0].Length;
        int minIndex = 0;
        for(int i = 1; i < 9; ++i){
            if(possibleDirectionList[i].Length < minLength){
                minLength = possibleDirectionList[i].Length;
                minIndex = i;
            }
        }

        return possibleDirectionList[minIndex];
    }

}
