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
        _tetrisModel = new TetrisModel(sequence);
        _tetrisController = new TetrisController(_tetrisModel, speed);
        _tetrisView = new TetrisView(fps, _tetrisModel.GetDrawArea(), _tetrisController);
        _tetrisModel.SetView(_tetrisView);

        // show view
        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.add(_tetrisView);
        frame.setVisible(true);

        _tetrisView.Start(frame);
        _tetrisController.Start();
    }
}
