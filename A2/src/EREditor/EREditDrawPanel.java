package EREditor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by LangChen on 2016/10/10.
 */
public class EREditDrawPanel extends JPanel {

    private List<EREditDrawable> _listDrawable;
    private Point _offset;
    private double _multiplicity;

    public EREditDrawPanel() {
        _listDrawable = null;
        _offset = null;
        _multiplicity = 1.0d;
    }

    public void SetPara(List<EREditDrawable> listDrawable, Point offset)
    {
        _listDrawable = listDrawable;
        _offset = offset;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(_listDrawable != null && _offset != null)
        {
            // Draw
            for (EREditDrawable drawable: _listDrawable) {
                drawable.draw(g, _offset, _multiplicity);
            }
        }
    }

}
