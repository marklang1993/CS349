package EREditor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by LangChen on 2016/10/5.
 */
public class EREdit_Main {
    public static void main(String[] args) {
        EREditModel model = new EREditModel(null);
        EREditController controller = new EREditController(model);
        JFrame frame = new EREditView(controller);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setTitle("Entity-Relationship Diagram Editor");
        frame.setVisible(true);
    }
}
