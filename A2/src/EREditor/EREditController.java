package EREditor;

import java.awt.*;

/**
 * Created by LangChen on 2016/10/11.
 */
public class EREditController {

    private EREditModel _model;

    public EREditController(EREditModel model){
        _model = model;
    }

    public void ButtonEventHandler(String buttonName){
        if(buttonName == "New"){

        }
        else if(buttonName == "Resize"){

        }
        else if(buttonName == "Zoom+"){

        }
        else if(buttonName == "Zoom-"){

        }
        else if(buttonName == "Box"){
            _model.BoxMode();
        }
        else if(buttonName == "Arrow"){
            _model.ArrowMode();
        }
        else if(buttonName == "Text"){

        }
        else if(buttonName == "Eraser"){
            _model.EraserMode();
        }
    }

    public void TableEventHandler(String tableName, int rowIndex){
        System.out.println(tableName + " Row: "+rowIndex);
    }

    public void ScrollBarEventHandler(String scrollbarName, int value){
        System.out.println(scrollbarName + " Value: " + value);

        if(scrollbarName == "VScrollBar")
        {
            // Vertical

        }
        else if(scrollbarName == "HScrollBar")
        {
            // Horizontal

        }
    }

    public void DrawPanelClickEventHandler(Point displayPos, boolean doubleClicked){
        System.out.println("X: " + displayPos.X + "; Y: "+ displayPos.Y + "; " + doubleClicked);
    }

    public void DrawPanelWheelEventHandler(boolean zoomIn){
        System.out.println(zoomIn ? "+" : "-");
    }

    public void WindowResizeEventHandler(AWTEvent event){

    }
}
