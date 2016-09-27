package tetris;

import java.awt.*;

/**
 * Created by LangChen on 2016/9/26.
 */
public interface TetrisDrawable {
    void draw(Graphics g);
}

class TetrisPiece implements TetrisDrawable{

    private TetrisView _view;               // TetrisView
    private Size _displaySize;              // Display size
    private Point _offset;                  // Offset (Pixels)
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


    TetrisPiece(TetrisView view, Size displaySize, Point offset)
    {
        _view = view;
        _displaySize = displaySize;
        _offset = offset;
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

class TetrisBoarder implements TetrisDrawable{

    private Size _displaySize;              // Display size (Pixels)
    private Point _offset;                  // Offset (Pixels)
    private static int _width = 3;


    TetrisBoarder(Size displaySize, Point offset)
    {
        _displaySize = new Size(displaySize.Width * TetrisMath.PieceSize.Width,
                displaySize.Height * TetrisMath.PieceSize.Height);
        _offset = offset;
    }

    @Override
    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(_width));
        g2.setColor(Color.black);
        g2.drawRect(_offset.X - _width, _offset.Y - _width,
                _displaySize.Width + _width * 2, _displaySize.Height + _width * 2);
    }
}


