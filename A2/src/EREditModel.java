import javax.swing.*;
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
    public enum EDIT_MODE { CURSOR, DRAGGING, BOX, ARROW, ERASER}
    private final double MAX_Multiplicity = 4.0d;
    private final double MIN_Multiplicity = 0.5d;

    private Size _graphSize;            // Size of whole graph
    private Point _offset;              // Current offset (Raw Position)
    private double _multiplicity;       // Current multiplicity for Zooming
    private EDIT_MODE _editMode;        // Current Edit Mode
    private EREditIView _mainView;      // MainView
    private JFrame _mainFrame;          // MainFrame
    private int _dragEntityIndex;       // Dragged Entity Index
    private Point _dragMousePosOffset;  // Offset between cursor position and the entity position when dragging

    private double _hPercentage;        // Horizontal Percentage of Scrollbar
    private double _vPercentage;        // Vertical Percentage of Scrollbar


    public EREditModel(){
        _entityList = new ArrayList<>();
        _arrowList = new ArrayList<>();
        _listIView = new ArrayList<>();
    }

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
        _hPercentage = percentage;
        UpdateGraphHorizontal();
    }
    public void UpdateGraphHorizontal(){
        Size rawSize = EREditMath.DisplayToRaw(((EREditMainView)_mainView).GetDisplayPaneSize(), _multiplicity);
        int _surplus = _graphSize.Width - rawSize.Width;
        if(_surplus <= 0)
        {
            _offset.X = 0;
        }
        else
        {
            _offset.X = (int)(_surplus * _hPercentage);
        }
        _updateView();
    }
    public void MoveGraphVertical(double percentage) {
        _vPercentage = percentage;
        UpdateGraphVertical();
    }
    public void UpdateGraphVertical(){
        Size rawSize = EREditMath.DisplayToRaw(((EREditMainView)_mainView).GetDisplayPaneSize(), _multiplicity);
        int _surplus = _graphSize.Height - rawSize.Height;
        if(_surplus <= 0)
        {
            _offset.Y = 0;
        }
        else
        {
            _offset.Y = (int)(_surplus * _vPercentage);
        }
        _updateView();
    }
    public void ZoomIn() {
        double tmp = _multiplicity + 0.1d;
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
        double tmp = _multiplicity - 0.1d;
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
                    // Entity found, change STATE & record entity index & mouse position offset
                    _dragEntityIndex = index;
                    _editMode = EDIT_MODE.DRAGGING;
                    _dragMousePosOffset = new Point(rawPos.X - entity.GetPositon().X,
                                                rawPos.Y - entity.GetPositon().Y);
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
                Point newPosition = rawPos.Subtract(_dragMousePosOffset);
                if(_isEntityInCanvas(newPosition) &&
                        newPosition.X >= 0 && newPosition.Y >= 0) {
                    _entityList.get(_dragEntityIndex).Move(newPosition);
                }
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

    public void NewGraph(boolean isButtonEvent) {

        if(isButtonEvent){
            String[] btnNames = {"Yes", "No"};
            int ret = JOptionPane.showOptionDialog(
                    (JComponent)_mainView,
                    "Do you want to create a new diagram?",
                    "New diagram",
                    JOptionPane.YES_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, btnNames, btnNames[0]);

            if (ret == 1) {
                _updateView();
                return; // No
            }
        }

        _entityList.clear();
        _arrowList.clear();
        _listIView.clear();
        ((EREditMainView)_mainView).SetIViewList(_listIView);

        _offset = new Point(0, 0);
        _graphSize = new Size(600, 400);        // Default JPanel Size is (450, 450)
        _multiplicity = 2.0d;
        _dragEntityIndex = -1;
        _dragMousePosOffset = new Point(0, 0);

        _hPercentage = 0.0d;
        _vPercentage = 0.0d;

        CursorMode();
    }
    public void ResizeCanvas(){
        // Record the former size
        Size formerGraphSize = new Size(_graphSize.Width, _graphSize.Height);
        EREditResizeDialog resizeDialog = new EREditResizeDialog(_mainFrame, _graphSize);
        resizeDialog.setVisible(true);

        // Check larger
        if(_graphSize.Width >= formerGraphSize.Width && _graphSize.Height >= formerGraphSize.Height) {
            _updateView();
            return;
        }

        // Smaller
        // Remove the rectangles which are out of the new range
        for ( int i = 0; i < _entityList.size(); ++i) {
            if(!_isEntityInCanvas(_entityList.get(i).GetPositon())){
                _removeBoxWithIndex(i);
                --i;
            }
        }

        _updateView();
    }
    public void CursorMode() { _editMode = EDIT_MODE.CURSOR; _updateView(); }
    public void BoxMode() { _editMode = EDIT_MODE.BOX; _updateView();}
    public void ArrowMode(){
        EREditEntity selectedEntity = null;
        // Find a selected entity
        for ( EREditEntity entity: _entityList) {
            if(entity.IsSelected()){
                selectedEntity = entity;
                break;
            }
        }

        // Check
        if(selectedEntity == null){
            JOptionPane.showMessageDialog((JComponent)_mainView,
                    "Create relationship error! Please select an entity first.",
                    "Error!",
                    JOptionPane.ERROR_MESSAGE);
        }
        else{
            _editMode = EDIT_MODE.ARROW;
        }

        _updateView();
    }
    public void EraserMode() { _editMode = EDIT_MODE.ERASER; _updateView();}

    public void AddBox(Point rawPos){

        if(_isEntityInCanvas(rawPos)){
            _entityList.add(new EREditEntity(rawPos));
        }
        else {
            // Out of Range
            JOptionPane.showMessageDialog((JComponent)_mainView,
                    "Cannot create entity out of the canvas!",
                    "Error!",
                    JOptionPane.ERROR_MESSAGE);
        }

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
        if(startEntity.equals(endEntity)) {
            // Should not be same entity (Cannot Connect to itself)
            JOptionPane.showMessageDialog((JComponent)_mainView,
                    "Cannot create relationship with an entity itself!",
                    "Error!",
                    JOptionPane.ERROR_MESSAGE);
            CursorMode();
            return;
        }
        for (EREditArrow arrow: _arrowList) {
            if(arrow.isSameArrow(startEntity, endEntity)){
                // Cannot add repeated arrow
                JOptionPane.showMessageDialog((JComponent)_mainView,
                        "Cannot create repeated relationship!",
                        "Error!",
                        JOptionPane.ERROR_MESSAGE);
                CursorMode();
                return;
            }
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
                        _entityList.get(index).RemoveAllArrows();
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

    public void AdjustCrossBar(boolean isInc){
        EREditArrow adjustArrow = null;
        int selectedCount = 0;

        for (EREditArrow curArrow: _arrowList) {
            if(curArrow.IsSelected()){
                adjustArrow = curArrow;
                ++selectedCount;
            }
        }

        // Adjust crossbar position when only one arrow is selected
        if(selectedCount == 1){
            if (isInc){
                adjustArrow.IncTurningPointRatio();
            }
            else{
                adjustArrow.DecTurningPointRatio();
            }
        }

        _updateView();
    }

    // Update View
    private void _updateViewList(){
        _listIView.clear();

        // Get All drawable Boxes
        for (EREditEntity drawBox : _entityList){
            _listIView.add(drawBox.Export(_offset, _multiplicity));
        }

        // Get All drawable Arrows
        for (EREditArrow drawArrow : _arrowList){
            _listIView.add(drawArrow.Export(_offset, _multiplicity));
        }
    }
    private void _updateView(){
        // Update Drawing List
        _updateViewList();

        // Update Buttons
        ((EREditMainView)_mainView).SetPressedBoxBtn(_editMode == EDIT_MODE.BOX);
        ((EREditMainView)_mainView).SetPressedArrowBtn(_editMode == EDIT_MODE.ARROW);
        ((EREditMainView)_mainView).SetPressedEraserBtn(_editMode == EDIT_MODE.ERASER);

        // Update Canvas Size
        Size newCanvasSize = EREditMath.RawToDisplay(
                new Size(_graphSize.Width - _offset.X, _graphSize.Height - _offset.Y),
                _multiplicity);
        ((EREditMainView)_mainView).SetCanvasSize(newCanvasSize);

        // Draw everything
       _mainView.draw(null);
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
            if(arrow.equals(removedArrow)) {
                _arrowList.remove(index);
                return;
            }
        }
    }
    private void _removeBoxWithIndex(int index){
        // Delete Entity
        LinkedList<EREditArrow> removedArrowList =
                _entityList.get(index).RemoveAllArrows();
        _entityList.remove(index);
        // Delete All related Arrows
        for (EREditArrow arrow: removedArrowList) { _removeArrow(arrow); }

        CursorMode();
    }
    private boolean _isEntityInCanvas(Point rawStartPos){
        Point rawEndPos = rawStartPos.Add(new Point(EREditDrawBox.SIZE.Width, EREditDrawBox.SIZE.Height));
        return !(rawEndPos.X > _graphSize.Width || rawEndPos.Y > _graphSize.Height);
    }

    // Accessors
    public void SetMainView(EREditIView mainView) { _mainView = mainView; }
    public void SetMainFrame(JFrame mainFrame) { _mainFrame = mainFrame; }
    public Point GetOffset() { return _offset; }
    public double GetMultiplicity() { return _multiplicity; }
    public EDIT_MODE GetEditMode() { return _editMode; }
    public Size GetGraphSize() { return _graphSize; }
    public int GetDragEntityIndex() { return _dragEntityIndex; }
    public ArrayList<EREditEntity> GetEntityList() { return _entityList; }
    public ArrayList<EREditArrow> GetArrowList() { return _arrowList; }
}

interface EREditExport{
    String GetText();

    void Select(boolean relatedSelected);
    void Unselect(boolean relatedUnselected);
    boolean IsSelected();

    EREditIView Export(Point offset, double multiplicity);
}

class EREditEntity implements EREditExport{
    private final String _id;
    private Point _position;
    private boolean _selected;
    private String _text;

    private EREditDrawBox _drawBox;

    private static int _entityGlobalIndex = 0;

    private LinkedList<EREditArrow> _arrowList;

    public EREditEntity(Point position){
        _position = position;
        _selected = false;
        _text = "Entity" + _entityGlobalIndex;
        ++_entityGlobalIndex;
        _arrowList = new LinkedList<>();
        _id = EREditMath.GetId("ENTITY");
    }

    public void SetText(String text){ _text = text; }
    @Override
    public String GetText(){ return _text;}
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
    public Point GetPositon() { return _position; }
    public void Move(Point position) {
        _position = position;
    }

    public void AddArrow(EREditArrow arrow)
    {
        _arrowList.add(arrow);
    }
    public LinkedList<EREditArrow> RemoveAllArrows() {
        LinkedList<EREditArrow> removedArrowList = new LinkedList<>();

        // Return the arrowList where all entities in each arrow are removed
        while(!_arrowList.isEmpty()){
            EREditArrow linkedArrow = _arrowList.getFirst();
            removedArrowList.add(linkedArrow);
            linkedArrow.RemoveBothEntities();
        }

        return removedArrowList;
    }
    public void RemoveArrow(EREditArrow arrow){
        for(int i = 0; i < _arrowList.size(); ++i){
            if(_arrowList.get(i).equals(arrow))
            {
                _arrowList.remove(i);
                break;
            }
        }
    }

    public boolean equals(Object entity){
        return ((EREditEntity)entity)._id == _id;
    }
    public boolean IsContained(Point rawCursorPosition){
        Rectangle rect = new Rectangle(_position.X, _position.Y, EREditDrawBox.SIZE.Width, EREditDrawBox.SIZE.Height);
        return rect.contains(rawCursorPosition.X, rawCursorPosition.Y);
    }

    @Override
    public EREditIView Export(Point offset, double multiplicity){
        _drawBox = new EREditDrawBox(_position, _text, _selected, offset, multiplicity, _id);
        return _drawBox;
    }

    public EREditDrawBox ExportDrawBox(){
        return _drawBox;
    }
}

class EREditArrow implements EREditExport{
    private double _ratioTurningPoint;  // Turning Point Ratio (0.05~0.95)
    private final String _id;
    private boolean _selected;

    private EREditEntity _startEntity;
    private EREditEntity _endEntity;

    public EREditArrow(EREditEntity startEntity, EREditEntity endEntity){
        _startEntity = startEntity;
        _endEntity = endEntity;
        _ratioTurningPoint = 0.5;
        _id = EREditMath.GetId("ARROW");
    }

    @Override
    public String GetText(){
        StringBuilder sb = new StringBuilder();
        sb.append(_startEntity.GetText());
        sb.append("->");
        sb.append(_endEntity.GetText());
        return sb.toString();
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

    public void IncTurningPointRatio(){
        double tmp = _ratioTurningPoint + 0.01d;
        if(tmp > 0.99){
            _ratioTurningPoint = 0.99d;
        }
        else {
            _ratioTurningPoint = tmp;
        }
    }

    public void DecTurningPointRatio(){
        double tmp = _ratioTurningPoint - 0.01d;
        if(tmp < 0.01){
            _ratioTurningPoint = 0.01d;
        }
        else {
            _ratioTurningPoint = tmp;
        }
    }

    @Override
    public EREditIView Export(Point offset, double multiplicity) {
        EREditDrawBox startDrawBox = _startEntity.ExportDrawBox();
        EREditDrawBox endDrawBox = _endEntity.ExportDrawBox();

        EREditMath.DirectionPair directionPair = EREditMath.DetermineDirection(
                startDrawBox.GetStartPos(),
                endDrawBox.GetStartPos());

        EREditDrawArrow.DIRECTION startBoxDirection = directionPair.StartDirection;
        EREditDrawArrow.DIRECTION endBoxDirection = directionPair.EndDirection;


        return new EREditDrawArrow(
                startDrawBox, endDrawBox,
                startBoxDirection, endBoxDirection,
                startDrawBox.IncGetCurrentArrowIndex(startBoxDirection), endDrawBox.IncGetCurrentArrowIndex(endBoxDirection),
                _selected, offset, multiplicity, _ratioTurningPoint, _id
        );
    }

    public void RemoveBothEntities(){
        _startEntity.RemoveArrow(this);
        _endEntity.RemoveArrow(this);
    }

    @Override
    public boolean equals(Object arrow){
        return ((EREditArrow)arrow)._id == _id;
    }

    public boolean isSameArrow(EREditEntity startEntity, EREditEntity endEntity){
        return _startEntity.equals(startEntity) && _endEntity.equals(endEntity);
    }
}

