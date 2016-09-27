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

    // basic parameters
    private int _fps;

    // drawing data
    private int[][] _displayMatrix;
    private Size _displayArea;

    // timer
    private Timer _viewUpdate;

    // controller
    private TetrisController _tetrisController;

    // list of TetrisDrawable
    LinkedList<TetrisDrawable> _drawableList;

    public TetrisView(int fps, Size displayArea, TetrisController tetrisController)
    {
        // init. basic parameters
        _fps = fps;

        // init. data
        _displayMatrix = null;
        _displayArea = displayArea;

        // init. Controller
        _tetrisController = tetrisController;

        // init. view update component
        _viewUpdate = new Timer(1000 / _fps, new ViewUpdateListener(this));

        // init. drawables
        Point offset = new Point(32, 32);
        _drawableList = new LinkedList<TetrisDrawable>();
        _drawableList.add(new TetrisPiece(this, _displayArea, offset));
        _drawableList.add(new TetrisBoarder(_displayArea, offset));
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        for (TetrisDrawable drawable: _drawableList) {
            drawable.draw(g);
        }

    }

    public void Start(JFrame parentFrame)
    {
        // init Listeners
        parentFrame.addKeyListener(new ViewKeyListener(_tetrisController));
        parentFrame.addMouseListener(new ViewMouseListener(_tetrisController));
        parentFrame.addMouseMotionListener(new ViewMouseMotionListener(_tetrisController));
        parentFrame.addMouseWheelListener(new ViewMouseWheelListener(_tetrisController));

        // Start to repaint the view
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

// For accepting users' inputs
class ViewKeyListener implements KeyListener
{
    TetrisController _tetrisController;

    public ViewKeyListener(TetrisController tetrisController)
    {
        _tetrisController = tetrisController;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        ;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        _tetrisController.Push(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        ;
    }
}

class ViewMouseListener implements MouseListener
{
    TetrisController _tetrisController;

    public ViewMouseListener(TetrisController tetrisController)
    {
        _tetrisController = tetrisController;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        _tetrisController.Push(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        ;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        ;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        ;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        ;
    }
}

class ViewMouseMotionListener implements MouseMotionListener
{
    TetrisController _tetrisController;

    public ViewMouseMotionListener(TetrisController tetrisController)
    {
        _tetrisController = tetrisController;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        ;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        _tetrisController.Push(e);
    }
}

class ViewMouseWheelListener implements MouseWheelListener
{
    TetrisController _tetrisController;

    public ViewMouseWheelListener(TetrisController tetrisController)
    {
        _tetrisController = tetrisController;
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        _tetrisController.Push(e);
    }
}