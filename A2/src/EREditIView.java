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

    private Point _offset;          // Offset
    private double _multiplicity;   // Multiplicity

    public EREditDrawBox(Point startPos, String text, boolean selected, Point offset, double multiplicity) {
        _startPos = startPos;
        _text = text;
        _selected = selected;

        _offset = offset;
        _multiplicity = multiplicity;
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

    public Point GetArrowPosition(EREditDrawArrow.DIRECTION direction){
        return EREditMath.GetArrowPosition(direction, _startPos);
    }

    public Point GetStartPos() { return _startPos; }
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

    public EREditDrawArrow(EREditDrawBox startBox, EREditDrawBox endBox,
                           DIRECTION startBoxDirection, DIRECTION endBoxDirection,
                           boolean selected, Point offset, double multiplicity){
        _startBox = startBox;
        _endBox = endBox;
        _selected = selected;

        _offset = offset;
        _multiplicity = multiplicity;

        _startBoxDirection = startBoxDirection;
        _endBoxDirection = endBoxDirection;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        // Check null
        if(_startBox == null || _endBox == null) return;
        if(_startBoxDirection == null || _endBoxDirection == null) return;

        Point startPos = _startBox.GetArrowPosition(_startBoxDirection);
        Point endPos = _endBox.GetArrowPosition(_endBoxDirection);

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
            double halfLengthLine = (endPos.Y - startPos.Y) / 2.0d;
            int turningPointY = (int)(startPos.Y + halfLengthLine);
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
            double halfLengthLine = (endPos.X - startPos.X) / 2.0d;
            int turningPointX = (int)(startPos.X + halfLengthLine);
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
}

