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
        // Return Raw Position
        Point offset;
        if (direction == EREditDrawArrow.DIRECTION.UP){
            offset = new Point(SIZE.Width / 2, 0);
        }
        else if (direction == EREditDrawArrow.DIRECTION.DOWN){
            offset = new Point(SIZE.Width / 2, SIZE.Height);
        }
        else if (direction == EREditDrawArrow.DIRECTION.LEFT){
            offset = new Point(0, SIZE.Height / 2);
        }
        else {
            offset = new Point(SIZE.Width, SIZE.Height / 2);
        }
        return _startPos.Add(offset);
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

    public EREditDrawArrow(EREditDrawBox startBox, EREditDrawBox endBox, boolean selected, Point offset, double multiplicity){
        _startBox = startBox;
        _endBox = endBox;
        _selected = selected;

        _offset = offset;
        _multiplicity = multiplicity;

        // Test
        _startBoxDirection = DIRECTION.DOWN;
        _endBoxDirection = DIRECTION.UP;
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

    private void _drawArrowLine(Graphics2D g2, Point startPos, Point endPos){
        // Draw ArrowLine
        g2.setColor(_selected ? Color.BLUE : Color.BLACK);
        g2.drawLine(startPos.X, startPos.Y, endPos.X, endPos.Y);

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

