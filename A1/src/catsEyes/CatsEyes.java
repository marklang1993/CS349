package catsEyes;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

/**
 * This example recreates Xlib's explict event loop style of programming.
 * It is for pedagogical purposes only.  DO NOT USE THIS CODE for a
 * "real" Swing application!
 */
public class CatsEyes extends EventComponent {

    private Eye leftEye = new Eye(100, 100, 100, 200);
    private Eye rightEye = new Eye(225, 100, 100, 200);
    private Ball ball = new Ball(100, 450, 20, this);
    private Point mouse;
    private boolean inside = false;
    private int FPS = 30;


    private LinkedList<Displayable> displayables = new LinkedList<Displayable>();

    public CatsEyes() {
        super(EventComponent.MouseEvents | EventComponent.MouseMotionEvents | EventComponent.ComponentEvents);


        this.displayables.add(this.leftEye);
        this.displayables.add(this.rightEye);
        this.displayables.add(this.ball);
    }


    protected void paintComponent(Graphics g) {
        g.setClip(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.ORANGE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        for (Displayable d : this.displayables) {
            d.paint(g);
        }
    }


    public void eventLoop() throws InterruptedException {
        while (true) {
            if (this.hasEvents()) {
                this.handleEvent(this.nextEvent());
            }
            this.handleAnimation();
            this.paintComponent(this.getGraphics());
        }
    }


    private void handleEvent(AWTEvent e) {
        if (e instanceof MouseEvent) {
            this.handleMouseEvent((MouseEvent) e);
        }
    }


    private void handleMouseEvent(MouseEvent e) {
        switch (e.getID()) {

            case MouseEvent.MOUSE_PRESSED:
                System.out.println("mouse click!");
                break;

            case MouseEvent.MOUSE_RELEASED:
                break;

            case MouseEvent.MOUSE_DRAGGED:
                //System.out.println("mouse dragged!");
                break;

            case MouseEvent.MOUSE_MOVED:
                this.mouse = e.getPoint();
                break;

            case MouseEvent.MOUSE_ENTERED:
                this.inside = true;
                this.mouse = e.getPoint();
                break;

            case MouseEvent.MOUSE_EXITED:
                this.inside = false;
                break;

            default:
                System.out.println("Some other mouse event " + e.getID());
        }
    }


    private void lookAt(Point p) {
        this.leftEye.lookAt(p);
        this.rightEye.lookAt(p);
    }

    private void handleAnimation() {

        this.ball.move();
        if (!this.inside) {
            this.lookAt(ball.getPosition());
        } else {
            this.lookAt(this.mouse);
        }
    }

}
