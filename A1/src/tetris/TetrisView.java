package tetris;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;

/**
 * Created by LangChen on 2016/9/26.
 */
public class TetrisView extends JPanel{

    // Basic parameters
    private int _fps;

    // Drawing data
    private int[][] _displayMatrix;     // Data matrix for displaying
    private int _score;                 // Current Score
    private int _currentPieceIndex;     // Current Piece Index;
    private Size _displayArea;          // Display area (unit: Block)
    private Point _offset;              // Offset related to (0, 0)

    private JFrame _parenFrame;         // Main JFrame
    private Timer _viewUpdate;          // For view updating with exact FPS
    private TetrisController _tetrisController; // Controller
    private TetrisModel _tetrisModel;   // Model
    LinkedList<TetrisDrawable> _drawableList;   // List of TetrisDrawable

    public TetrisView(int fps, Size displayArea, TetrisController tetrisController, TetrisModel tetrisModel)
    {
        // init. basic parameters
        _fps = fps;

        // init. data
        _displayMatrix = null;
        _displayArea = displayArea;

        // init. Controller&Model
        _tetrisController = tetrisController;
        _tetrisModel = tetrisModel;

        // init. view update component
        _viewUpdate = new Timer(1000 / _fps, new ViewUpdateListener(this));

        // init. pieceSize
        TetrisMath.PieceSize = new Size(TetrisMath.MinPieceSize);

        // init. drawables
        _offset = new Point(60, 32);
        _drawableList = new LinkedList<>();
        _drawableList.add(new TetrisBackGround(_displayArea, _offset));
        _drawableList.add(new TetrisPiece(_displayArea, _offset, this));
        _drawableList.add(new TetrisNextPiece(_displayArea, _offset, this, _tetrisModel.GetPieceSequence()));
        _drawableList.add(new TetrisBoarder(_displayArea, _offset));
        _drawableList.add(new TetrisScore(_displayArea, _offset, this));
        _drawableList.add(new TetrisSplash(_displayArea, _offset));
        _drawableList.add(new TetrisGameOver(_displayArea, _offset));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Get Status
        int gameStatus = _tetrisModel.GetStatus();

        // Draw
        for (TetrisDrawable drawable: _drawableList) {
            drawable.draw(g, gameStatus);
        }

    }

    public void BindFrame(JFrame parentFrame) {
        // Init
        _parenFrame = parentFrame;
        _parenFrame.addKeyListener(new ViewKeyListener(_tetrisController));
        _parenFrame.addMouseListener(new ViewMouseListener(_tetrisController, _offset));
        _parenFrame.addMouseMotionListener(new ViewMouseMotionListener(_tetrisController, _offset));
        _parenFrame.addMouseWheelListener(new ViewMouseWheelListener(_tetrisController));
        _parenFrame.addComponentListener(new ViewComponentListener(_tetrisController));

        // Set Size
        Insets insets = _parenFrame.getInsets();    // Get the size of boarder of JFrame
        _parenFrame.setSize(new Dimension(
                _displayArea.Width * TetrisMath.PieceSize.Width + _offset.X * 2 + insets.left + insets.right,
                _displayArea.Height * TetrisMath.PieceSize.Height + _offset.Y * 2 + insets.top + insets.bottom
        ));
    }

    public void ResizePieceSize() {
        Insets insets = _parenFrame.getInsets();    // Get the size of boarder of JFrame
        Dimension frameSize = _parenFrame.getSize();    // Get the new size of JFrame
        Size minFrameSize = new Size(
                _displayArea.Width * TetrisMath.MinPieceSize.Width + _offset.X * 2 + insets.left + insets.right,
                _displayArea.Height * TetrisMath.MinPieceSize.Height + _offset.Y * 2 + insets.top + insets.bottom
        );

        // Validate new size
        if(frameSize.width >= minFrameSize.Width && frameSize.height >= minFrameSize.Height)
        {
            // Calculate new PieceSize
            Size innerSize = new Size(
                    frameSize.width - insets.left - insets.right - _offset.X * 2,
                    frameSize.height - insets.top - insets.bottom - _offset.Y * 2
            );
            TetrisMath.PieceSize = new Size(
                    innerSize.Width  / _displayArea.Width,
                    innerSize.Height / _displayArea.Height
            );
        }
    }

    public void Start() {
        // Start the timer of repainting the view
        _viewUpdate.start();
    }

    public synchronized int[][] DataOpeartion(int[][] displayMatrix, boolean isRead) {
        if (isRead) {
            // Read Operation
            return _displayMatrix;
        } else {
            // Write Operation
            _displayMatrix = displayMatrix;
            return null;
        }
    }

    public synchronized int ScoreOperation(int score, boolean isRead) {
        if(isRead)
        {
            return _score;
        }
        else
        {
            _score = score;
            return 0;
        }
    }

    public synchronized int CurrentPieceOperation(int currentPieceIndex, boolean isRead) {
        if(isRead)
        {
            return _currentPieceIndex;
        }
        else
        {
            _currentPieceIndex = currentPieceIndex;
            return 0;
        }
    }

    public TetrisModel.BlockStatus[][] GetPieceMatrix(int currentPieceIndex)
    {
        return _tetrisModel.GetPiecesData().elementAt(currentPieceIndex).elementAt(0);
    }
}

// For drawing Timer
class ViewUpdateListener implements ActionListener
{
    private TetrisView _tetrisView;

    public ViewUpdateListener(TetrisView tetrisView)
    {
        _tetrisView = tetrisView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        _tetrisView.repaint();
    }
}

// ViewMouseClickedEvent
class ViewMouseClickedEvent extends MouseEvent
{
    public enum MouseType {
        CLICKED,
        MOVE
    };

    private Point _innerPosition;      // Inner Position (in PlayArea, unit: Block)
    private MouseType _type;           // MoveEvent Type

    public ViewMouseClickedEvent(MouseEvent event,
                                 Point innerPos,
                                 MouseType type)
    {
        super((JFrame) event.getSource(), event.getID(), event.getWhen(), event.getModifiers(), event.getX(), event.getY(), event.getClickCount(), event.isPopupTrigger());
        _innerPosition = new Point(innerPos);
        _type = type;
    }

    public Point GetBlockPosition()
    {
        return _innerPosition;
    }

    public MouseType GetMouseEventType()
    {
        return _type;
    }
}

// For accepting users' inputs
class ViewKeyListener extends KeyAdapter
{
    TetrisController _tetrisController;

    public ViewKeyListener(TetrisController tetrisController)
    {
        _tetrisController = tetrisController;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        _tetrisController.EventHandle(e);
    }
}

class ViewMouseListener extends MouseAdapter
{
    private TetrisController _tetrisController;
    private Point _offset;

    public ViewMouseListener(TetrisController tetrisController, Point offset)
    {
        _tetrisController = tetrisController;
        _offset = offset;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();
        JFrame sourceFrame;
        if(src instanceof JFrame)
        {
            sourceFrame = (JFrame) src;
            Insets insets = sourceFrame.getInsets();    // Get the size of boarder of JFrame
            Point innerPos; // unit: Block
            innerPos = new Point(
                    (e.getX() - insets.left - _offset.X) / TetrisMath.PieceSize.Width,
                    (e.getY() - insets.top - _offset.Y) / TetrisMath.PieceSize.Height
            );

            ViewMouseClickedEvent event = new ViewMouseClickedEvent(
                    e,
                    innerPos,
                    ViewMouseClickedEvent.MouseType.CLICKED
            );
            _tetrisController.EventHandle(event);
        }
    }
}

class ViewMouseMotionListener extends MouseMotionAdapter
{
    private TetrisController _tetrisController;
    private Point _offset;

    public ViewMouseMotionListener(TetrisController tetrisController, Point offset)
    {
        _tetrisController = tetrisController;
        _offset = offset;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Object src = e.getSource();
        JFrame sourceFrame;
        if(src instanceof JFrame)
        {
            sourceFrame = (JFrame) src;
            Insets insets = sourceFrame.getInsets();    // Get the size of boarder of JFrame
            Point innerPos; // unit: Block
            innerPos = new Point(
                    (e.getX() - insets.left - _offset.X) / TetrisMath.PieceSize.Width,
                    (e.getY() - insets.top - _offset.Y) / TetrisMath.PieceSize.Height
            );

            ViewMouseClickedEvent event = new ViewMouseClickedEvent(
                    e,
                    innerPos,
                    ViewMouseClickedEvent.MouseType.MOVE
            );
            _tetrisController.EventHandle(event);
        }
    }
}

class ViewMouseWheelListener implements MouseWheelListener
{
    private TetrisController _tetrisController;

    public ViewMouseWheelListener(TetrisController tetrisController)
    {
        _tetrisController = tetrisController;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        _tetrisController.EventHandle(e);
    }
}

class ViewComponentListener extends ComponentAdapter
{
    private TetrisController _tetrisController;

    public ViewComponentListener(TetrisController tetrisController)
    {
        _tetrisController = tetrisController;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        _tetrisController.EventHandle(e);
    }
}