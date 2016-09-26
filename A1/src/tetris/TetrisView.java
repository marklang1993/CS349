package tetris;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by LangChen on 2016/9/26.
 */
public class TetrisView extends JFrame{

    // basic parameters
    private int _fps;

    // drawing data
    private int[][] _displayMatrix;
    private Size _displayArea;

    // timer
    private Timer _viewUpdate;

    public TetrisView(int fps, Size displayArea)
    {
        // init. basic parameters
        _fps = fps;

        // init. data
        _displayMatrix = null;
        _displayArea = displayArea;

        // init. view update component
        _viewUpdate = new Timer(1000 / _fps, new ViewUpdateListener(this));
        _viewUpdate.start();
    }

    public synchronized int[][] DataOpeartion(int[][] displayMatrix, boolean isRead)
    {
        if(isRead)
        {
            // Read Operation
            return _displayMatrix;
        }
        else
        {
            // Write Operation
            _displayMatrix = displayMatrix;
            return null;
        }
    }

    public synchronized Size GetDisplayArea()
    {
        return _displayArea;
    }
}

class ViewUpdateListener implements ActionListener
{
    private TetrisView _tetrisView;

    public ViewUpdateListener(TetrisView tetrisView)
    {
        _tetrisView = tetrisView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[][] _displayMatrix = _tetrisView.DataOpeartion(null, true);

        // For Testing
        TetrisDrawable drawable = new TetrisPiece(_displayMatrix, _tetrisView.GetDisplayArea());
        drawable.draw(_tetrisView.getGraphics());
    }
}