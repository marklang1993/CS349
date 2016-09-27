package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.PublicKey;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.Timer;

/**
 * Created by LangChen on 2016/9/26.
 */
public class TetrisController{

    private TetrisModel _tetrisModel;
    private LinkedList<AWTEvent> _eventList;

    private Timer _timer;

    public TetrisController(TetrisModel tetrisModel, double speed)
    {
        _eventList = new LinkedList<AWTEvent>();
        _tetrisModel = tetrisModel;

        _timer = new Timer((int)(speed * 1000), new InputProcessor(this));
        _timer.start();
    }

    private boolean isEmpty()
    {
        return _eventList.isEmpty();
    }

    private AWTEvent newest()
    {
        AWTEvent top = null;
        while(!isEmpty()) {
            top = _eventList.peek();
            _eventList.pop();
            if(isEmpty()) {
                break;
            }
        }
        return top;
    }

    public void Push(AWTEvent event)
    {
        _eventList.push(event);
    }

    public void Activate()
    {
        AWTEvent event = newest();
        if(event != null)
        {
            if(event instanceof KeyEvent)
            {
                keyboardEventHandler((KeyEvent) event);
            }
            else if (event instanceof MouseEvent)
            {
                mouseEventHandler((MouseEvent) event);
            }
            else
            {
                // Non-realizable input => NextStep
                _tetrisModel.NextStep();
            }
        }
        else
        {
            // No user input => NextStep
            _tetrisModel.NextStep();
        }
    }

    private void mouseEventHandler(MouseEvent event)
    {
        _tetrisModel.NextStep();
        // Not implement yet!
    }

    private void keyboardEventHandler(KeyEvent event)
    {
        switch (event.getKeyCode())
        {
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_NUMPAD6:
                // Right
                _tetrisModel.MoveRight();
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_NUMPAD4:
                // Left
                _tetrisModel.MoveLeft();
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_NUMPAD8:
                // Drop
                _tetrisModel.Drop();
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_X:
            case KeyEvent.VK_NUMPAD1:
            case KeyEvent.VK_NUMPAD5:
            case KeyEvent.VK_NUMPAD9:
                // Rotate Right
                _tetrisModel.RotateRight();
                break;
            case KeyEvent.VK_CONTROL:
            case KeyEvent.VK_Z:
            case KeyEvent.VK_NUMPAD3:
            case KeyEvent.VK_NUMPAD7:
                // Rotate Left
                _tetrisModel.RotateLeft();
                break;
            case KeyEvent.VK_P:
                // Pause/Resume

                break;
            default:
                // Non-realizable input => NextStep
                _tetrisModel.NextStep();
                break;
        }
    }
}

class InputProcessor implements ActionListener
{
    private TetrisController _tetrisController;

    public InputProcessor(TetrisController tetrisController)
    {
        _tetrisController = tetrisController;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        _tetrisController.Activate();
    }
}
