package tetris;

import java.awt.*;
import java.io.SequenceInputStream;

/**
 * Created by LangChen on 2016/9/26.
 */
public interface TetrisDrawable {
    void draw(Graphics g, int gameStatus);
}
abstract class TetrisDrawAdapter{
    protected Size _displaySize;              // Display size (unit: Block)
    protected Point _offset;                  // Offset (unit: Pixels)

    TetrisDrawAdapter(Size displaySize, Point offset)
    {
        _displaySize = displaySize;
        _offset = offset;
    }

    protected Size GetBackgroundSize() {
        return new Size(
                (_displaySize.Width + 2) * TetrisMath.PieceSize.Width + _offset.X * 2,
                (_displaySize.Height + 2) * TetrisMath.PieceSize.Height + _offset.Y * 2
        );
    }
}

// Backgroud
class TetrisBackGround extends TetrisDrawAdapter implements TetrisDrawable{
    private Color _color = new Color(255, 255 ,255);

    TetrisBackGround(Size displaySize, Point offset)
    {
        super(displaySize, offset);
    }

    @Override
    public void draw(Graphics g, int gameStatus) {

        Graphics2D g2 = (Graphics2D)g;
        Size bgSize = GetBackgroundSize();

        g2.setColor(_color);
        g2.fillRect(0 ,0, bgSize.Width, bgSize.Height);
    }
}

// Splash
class TetrisSplash extends TetrisDrawAdapter implements TetrisDrawable
{
    TetrisSplash(Size displaySize, Point offset)
    {
        super(displaySize, offset);
    }
    @Override
    public void draw(Graphics g, int gameStatus) {

        if(gameStatus == TetrisModel.STATUS_SPLASH)
        {
            Graphics2D g2 = (Graphics2D)g;

            g2.setColor(Color.black);
            Point pos = _offset.Add(new Point(45, 10));
            Point nextLine = new Point(0, 25);
            g2.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 24));
            g2.drawString("Tetris", pos.X, pos.Y);
            pos.X = 20;
            pos.Y += 20;

            // Keyboard
            g2.setFont(new Font("Times New Roman", Font.BOLD, 14));
            pos = pos.Add(nextLine);
            g2.drawString("Game Action      Keyboard     Numpad", pos.X, pos.Y);

            g2.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            pos = pos.Add(nextLine);
            g2.drawString("   Move Left       LEFT Arrow        4 ", pos.X, pos.Y);
            pos = pos.Add(nextLine);
            g2.drawString("  Move Right     RIGHT Arrow       6 ", pos.X, pos.Y);
            pos = pos.Add(nextLine);
            g2.drawString("      Drop             Space Bar           8 ", pos.X, pos.Y);
            pos = pos.Add(nextLine);
            g2.drawString("  Rotate Right     UP Arrow, X    1, 5, 9 ", pos.X, pos.Y);
            pos = pos.Add(nextLine);
            g2.drawString("  Rotate Left        Control, Z         3, 7 ", pos.X, pos.Y);
            pos = pos.Add(nextLine);
            g2.drawString("      Pause                  P          ", pos.X, pos.Y);

            // Mouse
            g2.setFont(new Font("Times New Roman", Font.BOLD, 14));
            pos = pos.Add(nextLine);
            g2.drawString("Game Action                  Mouse", pos.X, pos.Y);

            g2.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            pos = pos.Add(nextLine);
            g2.drawString(" Piece Select      Press on unselected piece", pos.X, pos.Y);
            pos = pos.Add(nextLine);
            g2.drawString("      Drop           Press on selected piece", pos.X, pos.Y);
            pos = pos.Add(nextLine);
            g2.drawString("      Move          Move when piece selected", pos.X, pos.Y);
            pos = pos.Add(nextLine);
            g2.drawString("     Rotate          Rotate mouse wheel", pos.X, pos.Y);

            // Start info
            g2.setFont(new Font("Times New Roman", Font.BOLD, 14));
            pos = pos.Add(new Point(55, 50));
            g2.drawString("Press any key to start...", pos.X, pos.Y);
        }
    }
}

// Game Over
class TetrisGameOver extends TetrisDrawAdapter implements TetrisDrawable
{
    TetrisGameOver(Size displaySize, Point offset)
    {
        super(displaySize, offset);
    }
    @Override
    public void draw(Graphics g, int gameStatus) {

        if(gameStatus == TetrisModel.STATUS_GAMEOVER)
        {
            Graphics2D g2 = (Graphics2D)g;
            Size bgSize = GetBackgroundSize();

            g2.setColor(Color.black);
            Point pos = _offset.Add(new Point(10, 120));
            g2.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 24));
            g2.drawString("Game Over", pos.X, pos.Y);
            pos.X = 20;
            pos.Y += 20;


            // Start info
            g2.setFont(new Font("Times New Roman", Font.BOLD, 14));
            pos = pos.Add(new Point(40, 50));
            g2.drawString("Press any key to restart...", pos.X, pos.Y);
        }
    }
}

// Boarder
class TetrisBoarder extends TetrisDrawAdapter implements TetrisDrawable{

    private static int _width = 3;          // Stroke size

    TetrisBoarder(Size displaySize, Point offset)
    {
        super(displaySize, offset.Subtract(new Point(40, 0)));
    }

    @Override
    public void draw(Graphics g, int gameStatus) {

        if(gameStatus == TetrisModel.STATUS_PLAYING || gameStatus == TetrisModel.STATUS_PAUSE){
            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(new BasicStroke(_width));
            g2.setColor(Color.black);

            Size _pixelSize = new Size(_displaySize.Width * TetrisMath.PieceSize.Width,
                    _displaySize.Height * TetrisMath.PieceSize.Height);
            g2.drawRect(_offset.X - _width, _offset.Y - _width,
                    _pixelSize.Width + _width * 2, _pixelSize.Height + _width * 2);
        }
    }
}

// Pieces Matrix
class TetrisPiece extends TetrisDrawAdapter implements TetrisDrawable{

    private TetrisView _view;               // TetrisView
    public static Color[] ColorMap =      // Pieces color map
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
        super(displaySize, offset.Subtract(new Point(40, 0)));
        _view = view;
    }

    @Override
    public void draw(Graphics g, int gameStatus)
    {
        if(gameStatus == TetrisModel.STATUS_PLAYING || gameStatus == TetrisModel.STATUS_PAUSE) {
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
                            g.setColor(ColorMap[_displayMatrix[i][j]]);
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

}

// Current Score
class TetrisScore extends TetrisDrawAdapter implements TetrisDrawable{

    private TetrisView _view;               // TetrisView

    TetrisScore(Size displaySize, Point offset, TetrisView view)
    {
        super(displaySize, offset.Subtract(new Point(44, 12)));
        _view = view;
    }

    @Override
    public void draw(Graphics g, int gameStatus) {
        if(gameStatus == TetrisModel.STATUS_PLAYING || gameStatus == TetrisModel.STATUS_PAUSE)
        {
            int score = _view.ScoreOperation(0, true);

            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(Color.black);
            g2.setFont(new Font("Arial", Font.PLAIN, 14));

            // Create the string
            StringBuilder sb = new StringBuilder(100);
            sb.append("Score: ");
            sb.append(score);
            g2.drawString(sb.toString(), _offset.X, _offset.Y);
        }
    }
}

// Next Piece
class TetrisNextPiece extends TetrisDrawAdapter implements TetrisDrawable{

    private TetrisView _view;               // TetrisView
    private String _sequence;               // Pieces Sequence

    TetrisNextPiece(Size displaySize, Point offset, TetrisView view, String sequence)
    {
        super(displaySize, offset.Add(new Point(150, 20)));
        _view = view;
        _sequence = sequence;
    }

    @Override
    public void draw(Graphics g, int gameStatus) {

        if(gameStatus == TetrisModel.STATUS_PLAYING || gameStatus == TetrisModel.STATUS_PAUSE) {
            int nextPieceIndex = TetrisMath.GetCurrentPieceType(_sequence,
                    TetrisMath.GetNextPieceIndex(_sequence, _view.CurrentPieceOperation(0, true)));
            TetrisModel.BlockStatus[][] _piecesMatrix = _view.GetPieceMatrix(nextPieceIndex);

            if(_piecesMatrix != null)
            {
                for (int i = 0; i < 4; i++)
                {
                    for(int j = 0; j < 4; j++)
                    {
                        if(_piecesMatrix[i][j] == TetrisModel.BlockStatus.ACTIVE)
                        {
                            // Draw text
                            Graphics2D g2 = (Graphics2D)g;
                            g2.setColor(Color.black);
                            g2.setFont(new Font("Arial", Font.PLAIN, 14));
                            g2.drawString("Next Piece", _offset.X - 10, _offset.Y - 16);


                            // Draw boarder
                            g.setColor(Color.gray);
                            g.drawRect(_offset.X + i * TetrisMath.PieceSize.Width,
                                    _offset.Y+ j * TetrisMath.PieceSize.Height,
                                    TetrisMath.PieceSize.Width,
                                    TetrisMath.PieceSize.Height);
                            // Fill with color
                            g.setColor(TetrisPiece.ColorMap[nextPieceIndex]);
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
}
