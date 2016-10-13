package EREditor;

import javax.swing.text.html.parser.Entity;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by LangChen on 2016/10/11.
 */
public class EREditModel {
    // Lists of entities and arrows
    private ArrayList<EREditEntity> _entityList;
    private ArrayList<EREditArrow> _arrowList;

    // Graphics Related
    private enum EDIT_MODE { CURSOR, BOX, ARROW, ERASER}
    private Point _offset;
    private double _multiplicity;
    private EDIT_MODE _editMode;
    private EREditView _view;

    public EREditModel(EREditView view){
        _view = view;

        _entityList = new ArrayList<>();
        _arrowList = new ArrayList<>();

        _offset = new Point(0, 0);
        _multiplicity = 1.0d;
        _editMode = EDIT_MODE.CURSOR;
    }

    // Actions handlers
    public void ClickOnGraph(Point mousePos){
        Point rawPos = EREditMath.DisplayToRaw(mousePos, _offset, _multiplicity);

        if(_editMode == EDIT_MODE.BOX) {
            // Create Box
            _entityList.add(new EREditEntity(rawPos));
            CursorMode();
        }
        else if(_editMode == EDIT_MODE.ARROW) {
            // Create Arrow from the SELECTED Box to the CURRENT Box

            EREditEntity startEntity = null;
            EREditEntity endEntity = null;
            for ( EREditEntity entity: _entityList) {
                // Find a selected entity
                if(entity.IsSelected()){
                    startEntity = entity;
                    break;
                }
                // Find current entity
                if(endEntity.IsContained(rawPos)){
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
            _arrowList.add(new EREditArrow(startEntity, endEntity));
            CursorMode();
        }
        else if(_editMode == EDIT_MODE.ERASER) {
            // Remove Entity
        }
        else{
            // # CURSOR Mode

            // Unselect other objects first
            _unselectOthers();
            // Select new Entity
            EREditEntity newSelectEntity = null;
            for ( EREditEntity entity: _entityList) {
                // Find new select entity
                if(entity.IsContained(rawPos)){
                    newSelectEntity = entity;
                    break;}
            }
            // Select newEntity
            if(newSelectEntity != null) { newSelectEntity.Select(true); }
        }
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
    }

    public void CursorMode() { _editMode = EDIT_MODE.CURSOR; }
    public void BoxMode() { _editMode = EDIT_MODE.BOX; }
    public void ArrowMode(){ _editMode = EDIT_MODE.ARROW; }
    public void EraserMode() { _editMode = EDIT_MODE.ERASER; }


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
}

class EREditEntity{
    private String _id;
    private Point _position;
    private boolean _selected;
    private String _text;

    private LinkedList<EREditArrow> _arrowList;

    public EREditEntity(Point position){
        _position = position;
        _selected = false;
        _text = "Entity";
        _arrowList = new LinkedList<EREditArrow>();
        _id = EREditMath.GetId("ENTITY");
    }

    public String GetText(){ return _text;}
    public void SetText(String text){ _text = text; }
    public void Select(boolean relatedSelected) {
        _selected = true;

        if(relatedSelected == true) {
            for (EREditArrow arrow: _arrowList) {
                arrow.Select(false);
            }
        }
    }
    public void Unselect(boolean relatedUnselected) {
        _selected = false;

        if(relatedUnselected == true) {
            for (EREditArrow arrow: _arrowList) {
                arrow.Unselect(false);
            }
        }
    }
    public boolean IsSelected(){ return _selected; }
    public void Move( Point position ) { _position = position; }

    public void AddArrow(EREditArrow arrow)
    {
        _arrowList.add(arrow);
    }
    public boolean IsEqual(EREditEntity entity){
        return entity._id == _id;
    }
    public boolean IsContained(Point rawCursorPosition){
        Rectangle rect = new Rectangle(_position.X, _position.Y, EREditDrawBox.SIZE.Width, EREditDrawBox.SIZE.Height);
        return rect.contains(rawCursorPosition.X, rawCursorPosition.Y);
    }
}

class EREditArrow{
    private String _id;
    private boolean _selected;

    private EREditEntity _startEntity;
    private EREditEntity _endEntity;

    public EREditArrow(EREditEntity startEntity, EREditEntity endEntity){
        _startEntity = startEntity;
        _endEntity = endEntity;
        _id = EREditMath.GetId("ARROW");
    }

    public void Select(boolean relatedSelected) {
        _selected = true;

        if(relatedSelected == true)
        {
            _startEntity.Select(false);
            _endEntity.Select(false);
        }
    }
    public void Unselect(boolean relatedUnselected) {
        _selected = false;

        if(relatedUnselected == true)
        {
            _startEntity.Unselect(false);
            _endEntity.Unselect(false);
        }
    }
    public boolean IsSelected(){ return _selected; }

    public boolean IsEqual(EREditArrow arrow){
        return arrow._id == _id;
    }
}

