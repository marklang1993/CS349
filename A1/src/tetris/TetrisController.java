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
    private Timer _timer;

    public TetrisController(TetrisModel tetrisModel, double speed)
    {
        _tetrisModel = tetrisModel;
        _timer = new Timer((int)(speed * 1000), new InputProcessor(_tetrisModel));
    }

    public void Start()
    {
        _timer.start();
    }

    public void EventHandle(AWTEvent event)
    {
        if(event != null)
        {
            if(event instanceof KeyEvent)
            {
                _keyboardEventHandler((KeyEvent) event);
            }
            else if (event instanceof ViewMouseClickedEvent)
            {
                _mouseEventHandler((ViewMouseClickedEvent) event);
            }
            else if(event instanceof MouseWheelEvent)
            {
                _mouseWheelEventHandler((MouseWheelEvent) event);
            }
            else if(event instanceof ComponentEvent)
            {
                _componentEventHandler((ComponentEvent) event);
            }
        }
    }

    private void _mouseEventHandler(ViewMouseClickedEvent event) {
        ViewMouseClickedEvent.MouseType _type = event.GetMouseEventType();
        int gameStatus = _tetrisModel.GetStatus();

        if(gameStatus == TetrisModel.STATUS_PLAYING) {
            if (_type == ViewMouseClickedEvent.MouseType.MOVE) {
                // Mouse Motion
                _tetrisModel.MouseMovePiece(event.GetBlockPosition());
            } else if (_type == ViewMouseClickedEvent.MouseType.CLICKED) {
                // Mouse Clicked
                _tetrisModel.MouseClickPiece(event.GetBlockPosition());
            }
        }
    }

    private void _mouseWheelEventHandler(MouseWheelEvent event) {
        int gameStatus = _tetrisModel.GetStatus();

        if(gameStatus == TetrisModel.STATUS_PLAYING)
        {
            if(event.getWheelRotation() > 0) {
                _tetrisModel.RotateRight();
            }
            else {
                _tetrisModel.RotateLeft();
            }
        }
    }

    private void _keyboardEventHandler(KeyEvent event) {
        int gameStatus = _tetrisModel.GetStatus();
        int keyCode = event.getKeyCode();

        switch (gameStatus) {
            case TetrisModel.STATUS_SPLASH:
                _tetrisModel.NewGame();
                break;

            case TetrisModel.STATUS_PLAYING:
                switch (keyCode)
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
                    // Pause
                    _tetrisModel.Pause();
                    break;
                }
                break;

            case TetrisModel.STATUS_PAUSE:
                if(keyCode == KeyEvent.VK_P)
                {
                    // Resume
                    _tetrisModel.Pause();
                }
                break;

            case TetrisModel.STATUS_GAMEOVER:
                _tetrisModel.Splash();
                break;
        }

    }

    private void _componentEventHandler(ComponentEvent event)
    {
        _tetrisModel.ResizePieceSize();
    }
}

class InputProcessor implements ActionListener
{
    private TetrisModel _tetrisModel;

    public InputProcessor(TetrisModel tetrisModel)
    {
        _tetrisModel = tetrisModel;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        int gameStatus = _tetrisModel.GetStatus();
        if(gameStatus == TetrisModel.STATUS_PLAYING)
        {
            _tetrisModel.NextStep();
        }
    }
}
