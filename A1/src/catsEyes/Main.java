package catsEyes;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bwbecker on 2016-09-13.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        CatsEyes c = new CatsEyes();
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(1,1));
        frame.setContentPane(c);
        frame.setSize(400, 500);
        frame.setVisible(true);

        c.eventLoop();
    }
}
