import java.awt.*;

/**
 * Created by LangChen on 2016/10/10.
 */
public interface EREditDrawable {
    void draw(Graphics g, Point offset, double multiplicity);
}

class EREditDrawBox implements EREditDrawable{

    public final static Size SIZE = new Size(50, 30);   // Size for each Box (multiplicity = 1.0f)

    private Point _startPos;    // The most top-left position (Unit: Pixel)
    private String _text;       // The text shown inside
    private boolean _selected;  // Selected by Mouse

    public EREditDrawBox(Point startPos, String text) {
        _startPos = startPos;
        _text = text;
        _selected = false;
    }

    @Override
    public void draw(Graphics g, Point offset, double multiplicity) {
        Graphics2D g2 = (Graphics2D)g;

        Point displayPos = EREditMath.RawToDisplay(_startPos, offset, multiplicity);
        Size displaySize = EREditMath.RawToDisplay(SIZE, multiplicity);

        // Draw Box
        g2.setColor(_selected ? Color.BLACK : Color.BLUE);
        g2.drawRect(displayPos.X, displayPos.Y, displaySize.Width, displaySize.Height);

        // Draw Text inside
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 12));
        g2.drawString(_text, displayPos.X, displayPos.Y);
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

class EREditDrawArrow implements EREditDrawable{

    public enum DIRECTION {UP, DOWN, LEFT, RIGHT}

    private EREditDrawBox _startBox;
    private DIRECTION _startBoxDirection;
    private EREditDrawBox _endBox;
    private DIRECTION _endBoxDirection;

    private boolean _selected;  // Selected by Mouse

    @Override
    public void draw(Graphics g, Point offset, double multiplicity) {
        Graphics2D g2 = (Graphics2D)g;

        // Check null
        if(_startBox == null || _endBox == null) return;
        if(_startBoxDirection == null || _endBoxDirection == null) return;

        Point startPos = _startBox.GetArrowPosition(_startBoxDirection);
        Point endPos = _endBox.GetArrowPosition(_endBoxDirection);

        startPos = EREditMath.RawToDisplay(startPos, offset, multiplicity);
        endPos = EREditMath.RawToDisplay(endPos, offset, multiplicity);

        _drawArrowLine(g2, startPos, endPos);
    }

    private void _drawArrowLine(Graphics2D g2, Point startPos, Point endPos){
        // Draw ArrowLine
        g2.setColor(_selected ? Color.BLACK : Color.BLUE);
        g2.drawLine(startPos.X, startPos.Y, endPos.X, endPos.Y);

    }
}

