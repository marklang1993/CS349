/**
 * Created by LangChen on 2016/10/10.
 */
public class EREditMath {

    public static class DirectionPair{
        public EREditDrawArrow.DIRECTION StartDirection;
        public EREditDrawArrow.DIRECTION EndDirection;
    }

    public static class BoxVertex{
        public Point UpLeft;
        public Point UpRight;
        public Point DownRight;
        public Point DownLeft;

        public BoxVertex(Point startPos){
            UpLeft = new Point(startPos.X, startPos.Y);
            UpRight = new Point(startPos.X + EREditDrawBox.SIZE.Width, startPos.Y);
            DownLeft = new Point(startPos.X, startPos.Y + EREditDrawBox.SIZE.Height);
            DownRight = new Point(startPos.X + EREditDrawBox.SIZE.Width, startPos.Y + EREditDrawBox.SIZE.Height);
        }
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

    public static Point GetArrowPosition(EREditDrawArrow.DIRECTION direction, Point startPos, int count, int index){
        // Return Raw Position
        Point offset;
        if (direction == EREditDrawArrow.DIRECTION.UP){
            offset = new Point((int)(EREditDrawBox.SIZE.Width / ((double)(count + 1) / (index + 1))), 0);
        }
        else if (direction == EREditDrawArrow.DIRECTION.DOWN){
            offset = new Point((int)(EREditDrawBox.SIZE.Width / ((double)(count + 1) / (index + 1))), EREditDrawBox.SIZE.Height);
        }
        else if (direction == EREditDrawArrow.DIRECTION.LEFT){
            offset = new Point(0, (int)(EREditDrawBox.SIZE.Height / ((double)(count + 1)/ (index + 1))));
        }
        else {
            offset = new Point(EREditDrawBox.SIZE.Width, (int)(EREditDrawBox.SIZE.Height / ((double)(count + 1)/ (index + 1))));
        }
        return startPos.Add(offset);
    }

    public static Point GetArrowPosition(EREditDrawArrow.DIRECTION direction, Point startPos){
        return GetArrowPosition(direction, startPos, 1, 0);
    }

    private static EREditDrawArrow.DIRECTION _determineDirection(
            BoxVertex targetBoxVertex,
            Point startPos,
            EREditDrawArrow.DIRECTION startDirection){
        Point arrowPos = GetArrowPosition(startDirection, startPos);

        if(startDirection == EREditDrawArrow.DIRECTION.UP){
            if (arrowPos.X >= targetBoxVertex.DownLeft.X && arrowPos.X <= targetBoxVertex.DownRight.X){
                return EREditDrawArrow.DIRECTION.DOWN;
            }
            else if(arrowPos.X < targetBoxVertex.DownLeft.X){
                return EREditDrawArrow.DIRECTION.LEFT;
            }
            else{
                return EREditDrawArrow.DIRECTION.RIGHT;
            }
        }
        else if(startDirection == EREditDrawArrow.DIRECTION.RIGHT){
            if(arrowPos.Y >= targetBoxVertex.UpLeft.Y && arrowPos.Y <= targetBoxVertex.DownLeft.Y){
                return EREditDrawArrow.DIRECTION.LEFT;
            }
            else if(arrowPos.Y < targetBoxVertex.UpLeft.Y){
                return EREditDrawArrow.DIRECTION.UP;
            }
            else{
                return EREditDrawArrow.DIRECTION.DOWN;
            }
        }
        else if(startDirection == EREditDrawArrow.DIRECTION.DOWN){
            if(arrowPos.X >= targetBoxVertex.UpLeft.X && arrowPos.X <= targetBoxVertex.UpRight.X){
                return EREditDrawArrow.DIRECTION.UP;
            }
            else if(arrowPos.X < targetBoxVertex.UpLeft.X){
                return EREditDrawArrow.DIRECTION.LEFT;
            }
            else{
                return EREditDrawArrow.DIRECTION.RIGHT;
            }
        }
        else{
            if(arrowPos.Y >= targetBoxVertex.UpRight.Y && arrowPos.Y <= targetBoxVertex.DownRight.Y){
                return EREditDrawArrow.DIRECTION.RIGHT;
            }
            else if(arrowPos.Y < targetBoxVertex.UpLeft.Y){
                return EREditDrawArrow.DIRECTION.UP;
            }
            else{
                return EREditDrawArrow.DIRECTION.DOWN;
            }
        }
    }

    public static DirectionPair DetermineDirection(Point startBoxPos, Point endBoxPos){
        int Xdifference = endBoxPos.X - startBoxPos.X;
        int Ydifference = endBoxPos.Y - startBoxPos.Y;

        EREditDrawArrow.DIRECTION startDirection;
//        EREditDrawArrow.DIRECTION endDirection;

        // 1st Step: Approx. Get Directions
        if(Math.abs(Xdifference) > Math.abs(Ydifference)){
            // Choose Left & Right
            if(Xdifference >= 0){
                startDirection = EREditDrawArrow.DIRECTION.RIGHT;
//                endDirection = EREditDrawArrow.DIRECTION.LEFT;
            }
            else{
                startDirection = EREditDrawArrow.DIRECTION.LEFT;
//                endDirection = EREditDrawArrow.DIRECTION.RIGHT;
            }

        }
        else {
            // Choose Top & Bottom
            if(Ydifference >= 0){
                startDirection = EREditDrawArrow.DIRECTION.DOWN;
//                endDirection = EREditDrawArrow.DIRECTION.UP;
            }
            else {
                startDirection = EREditDrawArrow.DIRECTION.UP;
//                endDirection = EREditDrawArrow.DIRECTION.DOWN;
            }
        }

        // 2nd Step
        BoxVertex endBoxVertex = new BoxVertex(endBoxPos);

        DirectionPair directionPair = new DirectionPair();
        directionPair.StartDirection = startDirection;
        directionPair.EndDirection = _determineDirection(endBoxVertex, startBoxPos, startDirection);

        return directionPair;
    }

}
