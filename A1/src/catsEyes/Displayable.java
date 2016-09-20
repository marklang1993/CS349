package catsEyes;

import javax.swing.*;
import java.awt.*;

import static java.lang.Math.*;

/**
 * Created by bwbecker on 2016-09-13.
 */
interface Displayable {
    void paint(Graphics g);
}


class Eye implements Displayable {
    private int x, y, width, height;
    private int lookX, lookY;

    public Eye(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.lookX = x + width / 2;
        this.lookY = y + height / 2;
    }

    public void lookAt(Point p) {
        this.lookX = p.x;
        this.lookY = p.y;
    }

    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillOval(this.x, this.y, this.width, this.height);
        double cx = x + width / 2;
        double cy = y + height / 2;

        double alpha;
        if (lookX > cx) alpha = atan((lookY - cy) / (lookX - cx));
        else alpha = Math.PI - atan((lookY - cy) / (cx - lookX));
        g.setColor(Color.white);
        g.fillOval(Math.toIntExact(Math.round(cx + cos(alpha) * width / 4 - width / 8)),
                Math.toIntExact(Math.round(cy + sin(alpha) * height / 4 - height / 8)),
                width / 4, height / 4);
    }
}


class Ball implements Displayable {
    private int x, y, radius, direction;
    private JComponent parent;

    public Ball(int x, int y, int radius, JComponent parent) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.parent = parent;
        this.direction = 3;
    }

    public void move() {
        this.x += this.direction;
        if (Math.random() > 0.99 || this.x < 0 || this.x > parent.getWidth() - 2*this.radius) {
            this.direction = -this.direction;
        }
    }

    public Point getPosition() {
        return new Point(this.x + this.radius, this.y + this.radius);
    }

    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillOval(this.x, this.y, 2 * this.radius, 2 * this.radius);
    }
}