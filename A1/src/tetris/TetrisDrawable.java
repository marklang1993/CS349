package tetris;

import java.awt.*;

/**
 * Created by LangChen on 2016/9/26.
 */
public interface TetrisDrawable {
    void draw(Graphics g);
}
abstract class TetrisDrawAdapter{
    protected Size _displaySize;              // Display size (unit: Block)
    protected Point _offset;                  // Offset (unit: Pixels)

    TetrisDrawAdapter(Size displaySize, Point offset)
    {
        _displaySize = displaySize;
        _offset = offset;
    }
}

class TetrisPiece extends TetrisDrawAdapter implements TetrisDrawable{

    private TetrisView _view;               // TetrisView
    private static Color[] _colorMap =      // Pieces color map
            {
                    new Color(13, 132, 249),
                    new Color(242, 134, 25),
                    new Color(119, 119, 234),
                    new Color(242, 113, 242),
                    new Color(251, 251, 9),
                    new Color(13, 249, 13),
                    new Color(240, 30, 30)
            };


    TetrisPiece(Size displaySize, Point offset, TetrisView view)
    {
        super(displaySize, offset);
        _view = view;
    }

    @Override
    public void draw(Graphics g)
    {
        int[][] _displayMatrix = _view.DataOpeartion(null, true);         // Pieces description matrix
        if(_displayMatrix != null)
        {
            for (int i = 0; i < _displaySize.Width; i++)
            {
                for(int j = 0; j < _displaySize.Height; j++)
                {
                    if(_displayMatrix[i][j] >= 0)
                    {
                        // Draw boarder
                        g.setColor(Color.gray);
                        g.drawRect(_offset.X + i * TetrisMath.PieceSize.Width,
                                _offset.Y+ j * TetrisMath.PieceSize.Height,
                                TetrisMath.PieceSize.Width,
                                TetrisMath.PieceSize.Height);
                        // Fill with color
                        g.setColor(_colorMap[_displayMatrix[i][j]]);
                        g.fillRect(_offset.X + i * TetrisMath.PieceSize.Width + 1,
                                _offset.Y + j * TetrisMath.PieceSize.Height + 1,
                                TetrisMath.PieceSize.Width - 1,
                                TetrisMath.PieceSize.Height - 1);
                    }
                }
            }
        }

    }

}

class TetrisBoarder extends TetrisDrawAdapter implements TetrisDrawable{

    private static int _width = 3;          // Stroke size

    TetrisBoarder(Size displaySize, Point offset)
    {
        super(displaySize, offset);
    }

    @Override
    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(_width));
        g2.setColor(Color.black);

        Size _pixelSize = new Size(_displaySize.Width * TetrisMath.PieceSize.Width,
                _displaySize.Height * TetrisMath.PieceSize.Height);
        g2.drawRect(_offset.X - _width, _offset.Y - _width,
                _pixelSize.Width + _width * 2, _pixelSize.Height + _width * 2);
    }
}

class TetrisScore extends TetrisDrawAdapter implements TetrisDrawable{

    private TetrisView _view;               // TetrisView

    TetrisScore(Size displaySize, Point offset, TetrisView view)
    {
        super(displaySize, offset);
        _view = view;
    }

    @Override
    public void draw(Graphics g) {
        int score = _view.ScoreOperation(0, true);

        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.black);
        Point pos = _offset.Subtract(new Point(2, 12));
        g2.setFont(new Font("Arial", Font.PLAIN, 14));

        // Create the string
        StringBuilder sb = new StringBuilder(100);
        sb.append("Score: ");
        sb.append(score);
        g2.drawString(sb.toString(), pos.X, pos.Y);
    }
}

