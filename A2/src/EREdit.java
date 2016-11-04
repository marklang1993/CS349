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

        JFrame frame = new JFrame();

        EREditController controller = new EREditController();
        EREditModel model = new EREditModel();
        controller.SetModel(model);
        EREditMainView mainView = new EREditMainView(controller, model.GetEntityList(), model.GetArrowList());
        model.SetMainView(mainView);
        model.SetMainFrame(frame);
        model.NewGraph(false);


        SwingUtilities.invokeLater(
            new Runnable() {
                @Override
                public void run() {

                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.add(mainView);
                    frame.pack();
                    frame.setTitle("Entity-Relationship Diagram Editor");
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            }
        );

    }
}
