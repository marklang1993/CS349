package catsEyes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by bwbecker on 2016-09-01.
 */
public class EventComponent extends JComponent {

    private Queue<AWTEvent> evtQueue = new LinkedList<AWTEvent>();

    public static int MouseEvents = 1;
    public static int MouseMotionEvents = 2;
    public static int MouseWheelEvents = 4;
    public static int KeyboardEvents = 8;
    public static int ComponentEvents = 16;


    public EventComponent(int eventFlags) {
        if ((eventFlags & MouseEvents) == MouseEvents)
            this.addMouseListener(this.mouseListener);
        if ((eventFlags & MouseMotionEvents) == MouseMotionEvents)
            this.addMouseMotionListener(this.mouseMotionListener);
        if ((eventFlags & KeyboardEvents) == KeyboardEvents)
            this.addKeyListener(this.keyListener);
        if ((eventFlags & ComponentEvents) == ComponentEvents)
            this.addComponentListener(this.componentListener);
    }

    synchronized private void addEvt(AWTEvent e) {
        this.evtQueue.add(e);
    }

    synchronized protected AWTEvent nextEvent() {
        return this.evtQueue.remove();
    }

    protected boolean hasEvents() {
        return !this.evtQueue.isEmpty();
    }

    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            addEvt(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            addEvt(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            addEvt(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            addEvt(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            addEvt(e);
        }
    };

    private MouseMotionListener mouseMotionListener = new MouseMotionListener() {
        @Override
        public void mouseDragged(MouseEvent e) {
            addEvt(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            addEvt(e);
        }
    };

    private KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) { addEvt(e); }

        @Override
        public void keyPressed(KeyEvent e) { addEvt(e); }

        @Override
        public void keyReleased(KeyEvent e) { addEvt(e); }
    };

    private ComponentListener componentListener = new ComponentListener() {
        @Override
        public void componentResized(ComponentEvent e) {addEvt(e);}

        @Override
        public void componentMoved(ComponentEvent e) {addEvt(e);}

        @Override
        public void componentShown(ComponentEvent e) {addEvt(e);}

        @Override
        public void componentHidden(ComponentEvent e) {addEvt(e);}
    };
}
