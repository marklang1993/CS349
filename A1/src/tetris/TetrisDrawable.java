package tetris;

import java.awt.*;

/**
 * Created by LangChen on 2016/9/26.
 */
public interface TetrisDrawable {
    void draw(Graphics g);
}

class TetrisPiece implements TetrisDrawable{

    private int[][] _displayMatrix;         // Pieces description matrix
    private Size _displaySize;              // Display size
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

    TetrisPiece(int[][] displayMatrix, Size displaySize)
    {
        _displayMatrix = displayMatrix;
        _displaySize = displaySize;
    }

    @Override
    public void draw(Graphics g)
    {
        for (int i = 0; i < _displaySize.Width; i++)
        {
            for(int j = 0; j < _displaySize.Height; j++)
            {
                if(_displayMatrix[i][j] >= 0)
                {
                    // Draw boarder
                    g.setColor(Color.gray);
                    g.drawRect(i * TetrisMath.PieceSize.Width,
                            j * TetrisMath.PieceSize.Height,
                            TetrisMath.PieceSize.Width,
                            TetrisMath.PieceSize.Height);
                    // Fill with color
                    g.setColor(_colorMap[_displayMatrix[i][j]]);
                    g.fillRect(i * TetrisMath.PieceSize.Width + 1,
                            j * TetrisMath.PieceSize.Height + 1,
                            TetrisMath.PieceSize.Width - 1,
                            TetrisMath.PieceSize.Height - 1);
                }
            }
        }
    }

}


