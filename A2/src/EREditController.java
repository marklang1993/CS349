import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

/**
 * Created by LangChen on 2016/10/11.
 */
public class EREditController {

    private EREditModel _model;

    public void SetModel(EREditModel model){
        _model = model;
    }

    public void ButtonEventHandler(String buttonName){
        if(buttonName.equals("New") ){
            _model.NewGraph();
        }
        else if(buttonName.equals("Resize")){

        }
        else if(buttonName.equals("Zoom+")){
            _model.ZoomIn();
        }
        else if(buttonName.equals("Zoom-")){
            _model.ZoomOut();
        }
        else if(buttonName.equals("Box")){
            _model.BoxMode();
        }
        else if(buttonName.equals("Arrow")){
            _model.ArrowMode();
        }
        else if(buttonName.equals("Text")){
            _model.TextMode();
        }
        else if(buttonName.equals("Eraser")){
            _model.EraserMode();
        }
    }

    public void TableEventHandler(String tableName, int rowIndex){
//        System.out.println(tableName + " Row: "+rowIndex);
        if(tableName.equals("BoxList"))
        {
            _model.ClickEntityList(rowIndex);
        }
        else if(tableName.equals("ArrowList"))
        {
            _model.ClickArrowList(rowIndex);
        }
    }

    public void ScrollBarEventHandler(String scrollbarName, double percentage){
//        System.out.println(scrollbarName + " Value: " + percentage);

        if(scrollbarName.equals("VScrollBar"))
        {
            // Vertical
            _model.MoveGraphVertical(percentage);
        }
        else if(scrollbarName.equals("HScrollBar"))
        {
            // Horizontal
            _model.MoveGraphHorizontal(percentage);
        }
    }

    public void DrawPanelClickEventHandler(Point displayPos, boolean doubleClicked){
        System.out.println("X: " + displayPos.X + "; Y: "+ displayPos.Y + "; " + doubleClicked);

        // Coordinates system transformation
        Point rawPos = EREditMath.DisplayToRaw(displayPos, _model.GetOffset(), _model.GetMultiplicity());
        EREditModel.EDIT_MODE edit_mode = _model.GetEditMode();
        if(!doubleClicked)
        {
            // Click: Box selecting
            if(edit_mode == EREditModel.EDIT_MODE.BOX) {
                // # Create Box
                _model.AddBox(rawPos);
            }
            else if(edit_mode == EREditModel.EDIT_MODE.ARROW) {
                // # Create Arrow from the SELECTED Box to the CURRENT Box
                _model.AddArrow(rawPos);
            }
            else if(edit_mode == EREditModel.EDIT_MODE.ERASER) {
                // # Remove Entity
                _model.RemoveBox(rawPos);
            }
            else if (edit_mode == EREditModel.EDIT_MODE.CURSOR){
                // # CURSOR Mode
                _model.ClickCursor(rawPos);
            }
        }
        else
        {
            // Double-click: Name editing
        }
    }

    public void DrawPanelPressedEventHandler(Point displayPos) {
        if (_model.GetEditMode() == EREditModel.EDIT_MODE.CURSOR) {
            // # CURSOR Mode
            _model.StartMove(displayPos);
        }
        System.out.println("START MOVE");
    }

    public void DrawPanelReleasedEventHandler() {
        if (_model.GetEditMode() == EREditModel.EDIT_MODE.DRAGGING) {
            // # CURSOR Mode
            _model.ReleaseMove();
        }
        System.out.println("STOP MOVE");
    }

    public void DrawPanelDragEventHandler(Point displayPos){
        if (_model.GetEditMode() == EREditModel.EDIT_MODE.DRAGGING) {
            // # CURSOR Mode
            _model.Move(displayPos);
        }
        System.out.println("MOVE: " + displayPos.X + ", " +displayPos.Y);
    }

    public void DrawPanelWheelEventHandler(boolean zoomIn){
//        System.out.println(zoomIn ? "+" : "-");
        if(zoomIn)
        {
            _model.ZoomIn();
        }
        else
        {
            _model.ZoomOut();
        }
    }

    public void JTableUpdateViewHandler(){
        _model.CursorMode();
    }

    public void WindowResizeEventHandler(ComponentEvent event){
        _model.CursorMode();
    }

    public void WindowKeyEventHandler(int keyCode){
        switch (keyCode) {
            case KeyEvent.VK_OPEN_BRACKET:
                _model.ZoomOut();
                break;
            case KeyEvent.VK_CLOSE_BRACKET:
                _model.ZoomIn();
                break;
        }
    }
}
