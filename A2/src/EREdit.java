import javax.swing.*;

/**
 * Created by bwbecker on 2016-10-10.
 */
 
 /**
 * Modifited by LangChen on 2016/10/14.
 */
 
public class EREdit {

    public static void main(String[] args) {
//        System.out.println("Hello, world!");
        EREditView view = new EREditView(null);
        EREditModel model = new EREditModel(view);
        EREditController controller = new EREditController(model);
        JFrame frame = new EREditView(controller);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setTitle("Entity-Relationship Diagram Editor");
        frame.setVisible(true);
    }
}
