package tetris;

/**
 * Created by bwbecker on 2016-09-19.
 */

import javax.swing.*;
import java.awt.*;

/**
 * Extended by Chen Lang on 2016-09-25
 */

public class Tetris {

    private JFrame _frame;
    private TetrisModel _tetrisModel;
    private TetrisView _tetrisView;
    private TetrisController _tetrisController;

    public Tetris(int fps, double speed, String sequence) {
        // Init. each component of MVC framework
        _tetrisModel = new TetrisModel(sequence);
        _tetrisController = new TetrisController(_tetrisModel, speed);
        _tetrisView = new TetrisView(fps, _tetrisModel.GetDrawArea(), _tetrisController, _tetrisModel);
        _tetrisModel.SetView(_tetrisView);

        // Init. view
        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(_tetrisView);
        // Show View
        frame.setVisible(true);
        _tetrisView.BindFrame(frame);

        // Start Game Timer
        _tetrisView.Start();
        _tetrisController.Start();
    }
}
