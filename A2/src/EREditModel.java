import java.awt.*;
import java.util.*;

/**
 * Created by LangChen on 2016/10/11.
 */
public class EREditModel {
    // Lists of entities and arrows
    private ArrayList<EREditEntity> _entityList;
    private ArrayList<EREditArrow> _arrowList;

    // Lists of IViews
    private ArrayList<EREditIView> _listIView;

    // Graphics Related
    public enum EDIT_MODE { CURSOR, DRAGGING, BOX, ARROW, TEXT, TEXT_EDIT, ERASER}
    private final double MAX_Multiplicity = 8.0d;
    private final double MIN_Multiplicity = 0.25d;

    private Size _graphSize;            // Size of whole graph
    private Point _offset;              // Current offset
    private double _multiplicity;       // Current multiplicity for Zooming
    private EDIT_MODE _editMode;        // Current Edit Mode
    private EREditIView _mainView;      // MainView
    private int _dragEntityIndex;       // Dragged Entity Index

    public EREditModel(EREditIView view){
        _mainView = view;

        NewGraph();
    }

    // Actions handlers
//    public int ClickOnGraphText(Point mousePos, String text){
//        // Coordinates system transformation
//        Point rawPos = EREditMath.DisplayToRaw(mousePos, _offset, _multiplicity);
//
//        // Find correct entity
//        int index = -1;
//        for ( EREditEntity entity: _entityList) {
//            // Find new select entity
//            if(entity.IsContained(rawPos)){ ++index; break; }
//            ++index;
//        }
//        if(index == -1) {
//            // Entity not found
//            CursorMode();
//            return index;
//        }
//
//        // Entity found
//        return ClickOnGraphText(index, text);
//    }
//    public int ClickOnGraphText(int index, String text){
//
//        if (_editMode == EDIT_MODE.TEXT){
//            // Start Editing
//            _editMode = EDIT_MODE.TEXT_EDIT;
//            _updateView();
//        }
//        else if (_editMode == EDIT_MODE.TEXT_EDIT) {
//            // Finish Editing
//            _entityList.get(index).SetText(text);
//            CursorMode();
//        }
//
//        return index;
//    }
    public void ClickEntityList(int index){
        // Unselect other objects first
        _unselectOthers();

        if(index < _entityList.size())
        {
            EREditEntity selectEntity = _entityList.get(index);
            selectEntity.Select(true);
        }
        CursorMode();

        _updateView();
    }
    public void ClickArrowList(int index){
        // Unselect other objects first
        _unselectOthers();

        if(index < _arrowList.size())
        {
            EREditArrow selectArrow = _arrowList.get(index);
            selectArrow.Select(true);
        }
        CursorMode();

        _updateView();
    }
    public void MoveGraphHorizontal(double percentage) {
        int _surplus = _graphSize.Width - ((EREditMainView)_mainView).GetDisplayPaneSize().Width;
        if(_surplus <= 0)
        {
            _offset.X = 0;
        }
        else
        {
            _offset.X = (int)(_surplus * percentage);
        }
        _updateView();
    }
    public void MoveGraphVertical(double percentage) {
        int _surplus = _graphSize.Height - ((EREditMainView)_mainView).GetDisplayPaneSize().Height;
        if(_surplus <= 0)
        {
            _offset.Y = 0;
        }
        else
        {
            _offset.Y = (int)(_surplus * percentage);
        }
        _updateView();
    }
    public void ZoomIn() {
        double tmp = _multiplicity * 2.0d;
        if(tmp <= MAX_Multiplicity)
        {
            _multiplicity = tmp;
        }
        else
        {
            _multiplicity = MAX_Multiplicity;
        }

        _updateView();
    }
    public void ZoomOut() {
        double tmp = _multiplicity / 2.0d;
        if(tmp >= MIN_Multiplicity)
        {
            _multiplicity = tmp;
        }
        else
        {
            _multiplicity = MIN_Multiplicity;
        }
        _updateView();
    }
    public void StartMove(Point mousePos) {
        // Coordinates system transformation
        Point rawPos = EREditMath.DisplayToRaw(mousePos, _offset, _multiplicity);

        if(_editMode == EDIT_MODE.CURSOR){
            // Pressed Mouse and Select

            // Find the corresponding entity index
            int index = -1;
            for ( EREditEntity entity: _entityList) {
                // Find new select entity
                if(entity.IsContained(rawPos)){
                    ++index;
                    _dragEntityIndex = index;
                    // Entity found, change STATE
                    _editMode = EDIT_MODE.DRAGGING;
                    return;
                }
                ++index;
            }


            // Entity not found
            _dragEntityIndex = -1;
        }
    }
    public void Move(Point mousePos) {
        // Coordinates system transformation
        Point rawPos = EREditMath.DisplayToRaw(mousePos, _offset, _multiplicity);

        if (_editMode == EDIT_MODE.DRAGGING){
            // Dragging
            if(_dragEntityIndex != -1 && _dragEntityIndex < _entityList.size())
            {
                _entityList.get(_dragEntityIndex).Move(rawPos);
                _updateView();
            }
        }
    }
    public void ReleaseMove() {
        if(_editMode == EDIT_MODE.DRAGGING) {
            _dragEntityIndex = -1;
            CursorMode();
        }
    }

    public void NewGraph() {
        _entityList = new ArrayList<>();
        _arrowList = new ArrayList<>();
        _listIView = new ArrayList<>();

        _offset = new Point(0, 0);
        _graphSize = new Size(600, 600);        // Default JPanel Size is (450, 450)
        _multiplicity = 1.0d;
        _editMode = EDIT_MODE.CURSOR;
        _dragEntityIndex = -1;
    }
    public void CursorMode() { _editMode = EDIT_MODE.CURSOR; _updateView(); }
    public void BoxMode() { _editMode = EDIT_MODE.BOX; _updateView();}
    public void ArrowMode(){ _editMode = EDIT_MODE.ARROW; _updateView();}
    public void TextMode(){ _editMode = EDIT_MODE.TEXT; _updateView();}
    public void EraserMode() { _editMode = EDIT_MODE.ERASER; _updateView();}

    public void AddBox(Point rawPos){
        _entityList.add(new EREditEntity(rawPos));
        CursorMode();
    }
    public void AddArrow(Point rawPos){
        EREditEntity startEntity = null;
        EREditEntity endEntity = null;

        // Find a selected entity
        for ( EREditEntity entity: _entityList) {
            if(entity.IsSelected()){
                startEntity = entity;
                break;
            }
        }

        // Find current entity
        for ( EREditEntity entity: _entityList) {
            if(entity.IsContained(rawPos)){
                endEntity = entity;
                break;
            }
        }

        // Check
        if(startEntity == null || endEntity == null) {
            // Not found
            CursorMode();
            return;
        }
        if(startEntity.IsEqual(endEntity))
        {
            // Should not be same
            CursorMode();
            return;
        }

        // Add
        EREditArrow connector = new EREditArrow(startEntity, endEntity);
        _arrowList.add(connector);
        startEntity.AddArrow(connector);
        endEntity.AddArrow(connector);

        CursorMode();
    }
    public void RemoveBox(Point rawPos){
        int index = -1;
        for ( EREditEntity entity: _entityList) {
            // Find new select entity
            if(entity.IsContained(rawPos)){
                ++index;
                // Delete Entity
                LinkedList<EREditArrow> removedArrowList =
                        _entityList.get(index).RemoveAllArrow();
                _entityList.remove(index);
                // Delete All related Arrows
                for (EREditArrow arrow: removedArrowList) { _removeArrow(arrow); }
                break;
            }
            ++index;
        }

        CursorMode();
    }
    public void ClickCursor(Point rawPos){
        // Unselect other objects first
        _unselectOthers();
        // Select new Entity
        EREditEntity newSelectEntity = null;
        for ( EREditEntity entity: _entityList) {
            // Find new select entity
            if(entity.IsContained(rawPos)){
                newSelectEntity = entity;
                break;
            }
        }
        // Select newEntity
        if(newSelectEntity != null) { newSelectEntity.Select(true); }

        _updateView();
    }

    // Update View
    private void _updateViewList(){
        _listIView.clear();

        // Get All drawable Boxes
        for (EREditEntity drawBox : _entityList){
            _listIView.add(drawBox.Export());
        }

        // Get All drawable Arrows
        for (EREditArrow drawArrow : _arrowList){
            _listIView.add(drawArrow.Export());
        }
    }
    private void _updateView(){
        // Update Drawing List
        _updateViewList();

        // Draw everything
        Graphics drawingGraphics = ((EREditMainView)_mainView).GetDrawingGraphics();
        if(_listIView != null && _offset != null)
        {
            // Draw
            for (EREditIView drawable: _listIView) {
                drawable.draw(drawingGraphics, _offset, _multiplicity);
            }
        }
    }

    // Helper functions
    private void _unselectOthers(){
        // Unselect other objects (Entity, Arrow)
        EREditEntity lastSelectEntity = null;
        int lastSelectEntityCount = 0;
        EREditArrow lastSelectArrow = null;
        int lastSelectArrowCount = 0;

        // Entity Loop
        for ( EREditEntity entity: _entityList) {
            // Find last select entity
            if(entity.IsSelected()) {
                lastSelectEntity = entity;
                ++lastSelectEntityCount;
            }
        }

        // Arrow Loop
        for ( EREditArrow arrow: _arrowList) {
            // Find last select entity
            if(arrow.IsSelected()) {
                lastSelectArrow = arrow;
                ++lastSelectArrowCount;
            }
        }

        // Unselect others
        if(lastSelectArrowCount == 1) { lastSelectArrow.Unselect(true);}
        if(lastSelectEntityCount == 1) { lastSelectEntity.Unselect(true);}
    }
    private void _removeArrow(EREditArrow removedArrow) {
        int index = 0;
        for (EREditArrow arrow: _arrowList) {
            if(arrow.IsEqual(removedArrow)) {
                _arrowList.remove(index);
                return;
            }
        }
    }

    // Accessors
    public Point GetOffset() { return _offset; }
    public double GetMultiplicity() { return _multiplicity; }
    public EDIT_MODE GetEditMode() { return _editMode; }
    public Size GetGraphSize() { return _graphSize; }
    public int GetDragEntityIndex() { return _dragEntityIndex; }
    public ArrayList<EREditEntity> GetEntityList() { return _entityList; }
    public ArrayList<EREditArrow> GetArrowList() { return _arrowList; }
}

interface EREditExport
{
    void Select(boolean relatedSelected);
    void Unselect(boolean relatedUnselected);
    boolean IsSelected();

    EREditIView Export();
}

class EREditEntity implements EREditExport{
    private final String _id;
    private Point _position;
    private boolean _selected;
    private String _text;

    private EREditDrawBox _drawBox;

    private LinkedList<EREditArrow> _arrowList;

    public EREditEntity(Point position){
        _position = position;
        _selected = false;
        _text = "Entity";
        _arrowList = new LinkedList<>();
        _id = EREditMath.GetId("ENTITY");
    }

    public String GetText(){ return _text;}
    public void SetText(String text){ _text = text; }
    @Override
    public void Select(boolean relatedSelected) {
        _selected = true;

        if(relatedSelected == true) {
            for (EREditArrow arrow: _arrowList) {
                arrow.Select(false);
            }
        }
    }
    @Override
    public void Unselect(boolean relatedUnselected) {
        _selected = false;

        if(relatedUnselected == true) {
            for (EREditArrow arrow: _arrowList) {
                arrow.Unselect(false);
            }
        }
    }
    @Override
    public boolean IsSelected(){ return _selected; }
    public void Move( Point position ) { _position = position; }

    public void AddArrow(EREditArrow arrow)
    {
        _arrowList.add(arrow);
    }
    public LinkedList<EREditArrow> RemoveAllArrow() {
        LinkedList<EREditArrow> removedArrowList = new LinkedList<>();

        for (EREditArrow linkedArrow : _arrowList) {
            removedArrowList.add(linkedArrow);
            linkedArrow.RemoveBothEntities();
        }
        return removedArrowList;
    }
    public void RemoveArrow(EREditArrow arrow){
        for(int i = 0; i < _arrowList.size(); ++i){
            if(_arrowList.get(i).IsEqual(arrow))
            {
                _arrowList.remove(i);
                break;
            }
        }
    }

    public boolean IsEqual(EREditEntity entity){
        return entity._id == _id;
    }
    public boolean IsContained(Point rawCursorPosition){
        Rectangle rect = new Rectangle(_position.X, _position.Y, EREditDrawBox.SIZE.Width, EREditDrawBox.SIZE.Height);
        return rect.contains(rawCursorPosition.X, rawCursorPosition.Y);
    }

    @Override
    public EREditIView Export(){
        _drawBox = new EREditDrawBox(_position, _text, _selected);
        return _drawBox;
    }

    public EREditDrawBox ExportDrawBox(){
        return _drawBox;
    }
}

class EREditArrow implements EREditExport{
    private final String _id;
    private boolean _selected;

    private EREditEntity _startEntity;
    private EREditEntity _endEntity;

    public EREditArrow(EREditEntity startEntity, EREditEntity endEntity){
        _startEntity = startEntity;
        _endEntity = endEntity;
        _id = EREditMath.GetId("ARROW");
    }

    @Override
    public void Select(boolean relatedSelected) {
        _selected = true;

        if(relatedSelected == true)
        {
            _startEntity.Select(false);
            _endEntity.Select(false);
        }
    }
    @Override
    public void Unselect(boolean relatedUnselected) {
        _selected = false;

        if(relatedUnselected == true)
        {
            _startEntity.Unselect(false);
            _endEntity.Unselect(false);
        }
    }
    @Override
    public boolean IsSelected(){ return _selected; }

    @Override
    public EREditIView Export() {
        return new EREditDrawArrow(_startEntity.ExportDrawBox(), _endEntity.ExportDrawBox(), _selected);
    }

    public void RemoveBothEntities(){
        _startEntity.RemoveArrow(this);
        _endEntity.RemoveArrow(this);
    }

    public boolean IsEqual(EREditArrow arrow){
        return arrow._id == _id;
    }
}

