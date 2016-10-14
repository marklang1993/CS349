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

        EREditController controller = new EREditController();
        EREditView view = new EREditView(controller);
        EREditModel model = new EREditModel(view);
        controller.SetModel(model);

        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.pack();
        view.setTitle("Entity-Relationship Diagram Editor");
        view.setVisible(true);
    }
}
