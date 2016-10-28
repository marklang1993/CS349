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
        EREditMainView mainView = new EREditMainView(controller);
        EREditModel model = new EREditModel(mainView);
        controller.SetModel(model);

        SwingUtilities.invokeLater(
            new Runnable() {
                @Override
                public void run() {
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.add(mainView);
                    frame.pack();
                    frame.setTitle("Entity-Relationship Diagram Editor");
                    frame.setVisible(true);
                }
            }
        );

    }
}
