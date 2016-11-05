import javax.swing.*;
import java.awt.*;

/**
 * Created by LangChen on 2016/10/10.
 */
public interface EREditIView {
    void draw(Graphics g);
}

class EREditDrawBox extends JComponent implements EREditIView{

    public final static Size SIZE = new Size(50, 30);   // Size for each Box (multiplicity = 1.0f)

    private Point _startPos;    // The most top-left position (Unit: Pixel, RAW Position)
    private String _text;       // The text shown inside
    private boolean _selected;  // Selected by Mouse
    private final String _id;   // Id

    private Point _offset;          // Offset
    private double _multiplicity;   // Multiplicity

    // For arrows
    private int _upArrowCount;
    private int _rightArrowCount;
    private int _downArrowCount;
    private int _leftArrowCount;

    public EREditDrawBox(Point startPos, String text, boolean selected,
                         Point offset, double multiplicity, String id) {
        _startPos = startPos;
        _text = text;
        _selected = selected;

        _offset = offset;
        _multiplicity = multiplicity;
        _id = id;

        _upArrowCount = 0;
        _rightArrowCount = 0;
        _downArrowCount = 0;
        _leftArrowCount = 0;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        Point displayPos = EREditMath.RawToDisplay(_startPos, _offset, _multiplicity);
        Size displaySize = EREditMath.RawToDisplay(SIZE, _multiplicity);

        // Draw Box
        g2.setColor(_selected ? Color.BLUE : Color.BLACK);
        g2.drawRect(displayPos.X, displayPos.Y, displaySize.Width, displaySize.Height);

        // Draw Text inside
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 12));
        g2.drawString(_text,
                displayPos.X + displaySize.Width  / 2 - 3 * _text.length(),
                displayPos.Y + displaySize.Height / 2 + 4);
    }

    public Point GetArrowPosition(EREditDrawArrow.DIRECTION direction, int count, int index){
        return EREditMath.GetArrowPosition(direction, _startPos, count, index);
    }

    public Point GetStartPos() { return _startPos; }

    @Override
    public boolean equals(Object box){
        return ((EREditDrawBox)box)._id == _id;
    }

    public int GetArrowCount(EREditDrawArrow.DIRECTION direction){
        if(direction == EREditDrawArrow.DIRECTION.UP){
            return _upArrowCount;
        }
        else if (direction == EREditDrawArrow.DIRECTION.RIGHT){
            return _rightArrowCount;
        }
        else if (direction == EREditDrawArrow.DIRECTION.DOWN){
            return _downArrowCount;
        }
        else{
            return _leftArrowCount;
        }
    }

    public int IncGetCurrentArrowIndex(EREditDrawArrow.DIRECTION direction){
        if(direction == EREditDrawArrow.DIRECTION.UP){
            ++_upArrowCount;
            return _upArrowCount - 1;
        }
        else if (direction == EREditDrawArrow.DIRECTION.RIGHT){
            ++_rightArrowCount;
            return  _rightArrowCount - 1;
        }
        else if (direction == EREditDrawArrow.DIRECTION.DOWN){
            ++_downArrowCount;
            return _downArrowCount - 1;
        }
        else{
            ++_leftArrowCount;
            return _leftArrowCount - 1;
        }
    }
}

class EREditDrawArrow extends JComponent implements EREditIView{

    public enum DIRECTION {UP, DOWN, LEFT, RIGHT}

    private final int ArrowLength = 15;
    private final double ArrowAngle = 0.5236d;

    private EREditDrawBox _startBox;
    private DIRECTION _startBoxDirection;
    private EREditDrawBox _endBox;
    private DIRECTION _endBoxDirection;

    private boolean _selected;  // Selected by Mouse

    private Point _offset;          // Offset
    private double _multiplicity;   // Multiplicity
    private double _ratioTurningPoint;
    private int _startBoxArrowIndex;
    private int _endBoxArrowIndex;
    private final String _id;       // Id

    public EREditDrawArrow(EREditDrawBox startBox, EREditDrawBox endBox,
                           DIRECTION startBoxDirection, DIRECTION endBoxDirection,
                           int startBoxArrowIndex, int endBoxArrowIndex,
                           boolean selected, Point offset, double multiplicity,
                           double ratioTurningPoint, String id){
        _startBox = startBox;
        _endBox = endBox;
        _startBoxDirection = startBoxDirection;
        _endBoxDirection = endBoxDirection;
        _startBoxArrowIndex = startBoxArrowIndex;
        _endBoxArrowIndex = endBoxArrowIndex;

        _selected = selected;

        _offset = offset;
        _multiplicity = multiplicity;
        _ratioTurningPoint = ratioTurningPoint;
        _id = id;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        // Check null
        if(_startBox == null || _endBox == null) return;
        if(_startBoxDirection == null || _endBoxDirection == null) return;

        Point startPos = _startBox.GetArrowPosition(_startBoxDirection,
                _startBox.GetArrowCount( _startBoxDirection), _startBoxArrowIndex);
        Point endPos = _endBox.GetArrowPosition(_endBoxDirection,
                _endBox.GetArrowCount(_endBoxDirection), _endBoxArrowIndex);

        startPos = EREditMath.RawToDisplay(startPos, _offset, _multiplicity);
        endPos = EREditMath.RawToDisplay(endPos, _offset, _multiplicity);

        _drawArrowLine(g2, startPos, endPos);
    }

    private boolean _isVDirection(DIRECTION direction){
        return direction == DIRECTION.UP || direction == DIRECTION.DOWN;
    }

    private boolean _isHDirection(DIRECTION direction){
        return direction == DIRECTION.LEFT || direction == DIRECTION.RIGHT;
    }

    private void _drawArrowLine(Graphics2D g2, Point startPos, Point endPos){
        // Draw ArrowLine

        // #2 Draw Line
        g2.setColor(_selected ? Color.BLUE : Color.BLACK);

        if(_isVDirection(_startBoxDirection) && _isVDirection(_endBoxDirection)){
            // double V
            double partialLengthLine = (endPos.Y - startPos.Y) * _ratioTurningPoint;
            int turningPointY = (int)(startPos.Y + partialLengthLine);
            g2.drawLine(startPos.X, startPos.Y, startPos.X, turningPointY);
            g2.drawLine(startPos.X, turningPointY, endPos.X, turningPointY);
            g2.drawLine(endPos.X, turningPointY, endPos.X, endPos.Y);
            startPos = new Point(endPos.X, turningPointY);      // update for angle calculation
        }
        else if(_isVDirection(_startBoxDirection) && _isHDirection(_endBoxDirection)){
            // start V, end H
            g2.drawLine(startPos.X, startPos.Y, startPos.X, endPos.Y);
            g2.drawLine(startPos.X, endPos.Y, endPos.X, endPos.Y);
            startPos = new Point(startPos.X, endPos.Y);         // update for angle calculation
        }
        else if(_isHDirection(_startBoxDirection) && _isVDirection(_endBoxDirection)){
            // start H, end V
            g2.drawLine(startPos.X, startPos.Y, endPos.X, startPos.Y);
            g2.drawLine(endPos.X, startPos.Y, endPos.X, endPos.Y);
            startPos = new Point(endPos.X, startPos.Y);         // update for angle calculation
        }
        else {
            // double H
            double partialLengthLine = (endPos.X - startPos.X) * _ratioTurningPoint;
            int turningPointX = (int)(startPos.X + partialLengthLine);
            g2.drawLine(startPos.X, startPos.Y, turningPointX, startPos.Y);
            g2.drawLine(turningPointX, startPos.Y, turningPointX, endPos.Y);
            g2.drawLine(turningPointX, endPos.Y, endPos.X, endPos.Y);
            startPos = new Point(turningPointX, endPos.Y);      // update for angle calculation
        }

        // #2 Draw Arrow
        // Calculate angle
        double lineLength = EREditMath.CalculateLength(startPos, endPos);
        double lineAngle = EREditMath.CalculateAngle(startPos, endPos);
        if(lineAngle < 0.0d) return;    // Error

        // Calculate Point List
        Point[] pointList;
        if(lineLength < ArrowLength){
            pointList = EREditMath.CalculateArrowPoints(lineAngle, ArrowAngle, lineLength, endPos);
        }
        else {
            pointList = EREditMath.CalculateArrowPoints(lineAngle, ArrowAngle, ArrowLength, endPos);
        }

        // Decomposite
        g2.fillPolygon(
                EREditMath.DecompositePoints(pointList, true),
                EREditMath.DecompositePoints(pointList, false),
                pointList.length);
    }

    @Override
    public boolean equals(Object arrow){
        return ((EREditDrawArrow)arrow)._id == _id;
    }
}

