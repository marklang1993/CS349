import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Created by bwbecker on 2016-10-10.
 */

public class ERModelTest {

    private EREditController _controller;
    private EREditView _view;
    private EREditModel _model;

    public ERModelTest() {
        _controller = new EREditController();
        _view = new EREditView(_controller);
        _model = new EREditModel(_view);
        _controller.SetModel(_model);
    }

    @Test
    public void Test_NewGraph() {
        _model.NewGraph();

        // Test Edit Mode, Graph Size, 2 Lists
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);

        Size modelSize = _model.GetGraphSize();
        assertEquals(modelSize.Width,600);
        assertEquals(modelSize.Height,600);

        ArrayList<EREditEntity> entityList = _model.GetEntityList();
        ArrayList<EREditArrow> arrowList = _model.GetArrowList();
        assertEquals(entityList.size(), 0);
        assertEquals(arrowList.size(), 0);
    }

    @Test
    public void Test_EachMode(){
        _model.NewGraph();

        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);
        _model.BoxMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.BOX);
        _model.ArrowMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.ARROW);
        _model.TextMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.TEXT);
        _model.EraserMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.ERASER);
        _model.CursorMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);
    }

    @Test
    public void Test_AddEntity(){
        _model.NewGraph();

        // Add Entity
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);
        _model.BoxMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.BOX);

        _model.ClickOnGraph(new Point(10, 10));
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);

        // Test Adding
        ArrayList<EREditEntity> entityList = _model.GetEntityList();
        ArrayList<EREditArrow> arrowList = _model.GetArrowList();
        assertEquals(entityList.size(), 1);
        assertEquals(arrowList.size(), 0);
    }

    @Test
    public void Test_DeleteEntity1() {
        _model.NewGraph();

        // Add Entity
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);
        _model.BoxMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.BOX);
        _model.ClickOnGraph(new Point(20, 20));
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);

        // Delete
        _model.EraserMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.ERASER);
        _model.ClickOnGraph(new Point(30, 30));
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);

        // Check Result
        assertEquals(_model.GetEntityList().size(), 0);
    }

    @Test
    public void Test_DeleteEntity2() {
        // Add Entity 1
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);
        _model.BoxMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.BOX);
        _model.ClickOnGraph(new Point(20, 20));
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);

        // Add Entity 2
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);
        _model.BoxMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.BOX);
        _model.ClickOnGraph(new Point(100, 20));
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);

        // Click 1st Entity
        _model.ClickOnGraph(new Point(30, 30));
        // Check Selected
        assertEquals(_model.GetEntityList().get(0).IsSelected(), true);

        // Add Arrow
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);
        _model.ArrowMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.ARROW);

        // Create Relation by clicking 2nd Entity
        _model.ClickOnGraph(new Point(110, 30));
        assertEquals(_model.GetArrowList().size(), 1);

        // Delete
        _model.EraserMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.ERASER);
        _model.ClickOnGraph(new Point(30, 30));
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);

        // Check Result
        assertEquals(_model.GetEntityList().size(), 1);
        assertEquals(_model.GetArrowList().size(), 0);
    }

    @Test
    public void Test_ModifyText(){
        _model.NewGraph();

        // Add Entity
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);
        _model.BoxMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.BOX);
        _model.ClickOnGraph(new Point(20, 20));
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);

        // Switch to TEXT mode
        _model.TextMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.TEXT);

        // Find the entity, switch to TEXT_EDIT mode
        int entityIndex = _model.ClickOnGraphText(new Point(30, 30), null);
        assertEquals(entityIndex, 0);
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.TEXT_EDIT);

        // Modify the name of entity
        _model.ClickOnGraphText(entityIndex, "Model");
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);
        assertEquals(_model.GetEntityList().get(entityIndex).GetText(), "Model");
    }

    @Test
    public void Test_ClickEntityArrowLists() {
        _model.NewGraph();

        // Add Entity 1
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);
        _model.BoxMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.BOX);
        _model.ClickOnGraph(new Point(20, 20));
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);

        // Add Entity 2
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);
        _model.BoxMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.BOX);
        _model.ClickOnGraph(new Point(100, 20));
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);

        // Click 1st Entity
        _model.ClickOnGraph(new Point(30, 30));
        // Check Selected
        assertEquals(_model.GetEntityList().get(0).IsSelected(), true);

        // Add Arrow
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);
        _model.ArrowMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.ARROW);

        // Create Relation by clicking 2nd Entity
        _model.ClickOnGraph(new Point(110, 30));
        assertEquals(_model.GetArrowList().size(), 1);

        // Clear Select
        _model.ClickOnGraph(new Point(200, 200));
        ArrayList<EREditEntity> entityList = _model.GetEntityList();
        ArrayList<EREditArrow> arrowList = _model.GetArrowList();
        for (EREditEntity entity: entityList) {
            assertEquals(entity.IsSelected(), false);
        }
        for (EREditArrow arrow: arrowList) {
            assertEquals(arrow.IsSelected(), false);
        }

        // ClickEntityList
        _model.ClickEntityList(0);
        assertEquals(entityList.get(0).IsSelected(), true);
        assertEquals(entityList.get(1).IsSelected(), false);
        assertEquals(arrowList.get(0).IsSelected(), true);

        _model.ClickEntityList(1);
        assertEquals(entityList.get(0).IsSelected(), false);
        assertEquals(entityList.get(1).IsSelected(), true);
        assertEquals(arrowList.get(0).IsSelected(), true);

        // ClickArrowList
        _model.ClickArrowList(0);
        assertEquals(entityList.get(0).IsSelected(), true);
        assertEquals(entityList.get(1).IsSelected(), true);
        assertEquals(arrowList.get(0).IsSelected(), true);
    }

    @Test
    public void Test_AdjustScrollBar() {
        _model.NewGraph();

        assertEquals(_model.GetOffset().X, 0);
        assertEquals(_model.GetOffset().Y, 0);

        // Horizontal test
        _model.MoveGraphHorizontal(0.5);
        assertEquals(_model.GetOffset().X, 300);

        // Vertical test
        _model.MoveGraphVertical(0.5);
        assertEquals(_model.GetOffset().Y, 300);

        // Return back
        _model.MoveGraphHorizontal(0.0);
        _model.MoveGraphVertical(0.0);
        assertEquals(_model.GetOffset().X, 0);
        assertEquals(_model.GetOffset().Y, 0);
    }

    @Test
    public void Test_Zoom() {
        _model.NewGraph();
        double ZERO = 0.0001d;

        assertEquals(Math.abs(_model.GetMultiplicity() - 1.0d) < ZERO, true);

        _model.ZoomIn();
        assertEquals(Math.abs(_model.GetMultiplicity() - 2.0d) < ZERO, true);
        _model.ZoomIn();
        assertEquals(Math.abs(_model.GetMultiplicity() - 4.0d) < ZERO, true);
        _model.ZoomIn();
        assertEquals(Math.abs(_model.GetMultiplicity() - 8.0d) < ZERO, true);
        _model.ZoomIn();
        assertEquals(Math.abs(_model.GetMultiplicity() - 8.0d) < ZERO, true);

        _model.ZoomOut();
        assertEquals(Math.abs(_model.GetMultiplicity() - 4.0d) < ZERO, true);
        _model.ZoomOut();
        assertEquals(Math.abs(_model.GetMultiplicity() - 2.0d) < ZERO, true);
        _model.ZoomOut();
        assertEquals(Math.abs(_model.GetMultiplicity() - 1.0d) < ZERO, true);
        _model.ZoomOut();
        assertEquals(Math.abs(_model.GetMultiplicity() - 0.5d) < ZERO, true);
        _model.ZoomOut();
        assertEquals(Math.abs(_model.GetMultiplicity() - 0.25d) < ZERO, true);
        _model.ZoomOut();
        assertEquals(Math.abs(_model.GetMultiplicity() - 0.25d) < ZERO, true);
    }

    @Test
    public void Test_Move() {
        _model.NewGraph();

        // Add Entity
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);
        _model.BoxMode();
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.BOX);
        _model.ClickOnGraph(new Point(20, 20));
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);

        // Drag Entity (Failed)
        _model.StartMove(new Point(100, 30));
        assertEquals(_model.GetDragEntityIndex(), -1);
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);

        // Drag Entity (Successful)
        _model.StartMove(new Point(30, 30));
        assertEquals(_model.GetDragEntityIndex(), 0);
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.DRAGGING);

        // Dragging
        _model.Move(new Point(50, 50));
        assertEquals(_model.GetDragEntityIndex(), 0);
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.DRAGGING);

        // Release Dragging
        _model.ReleaseMove();
        assertEquals(_model.GetDragEntityIndex(), -1);
        assertEquals(_model.GetEditMode(), EREditModel.EDIT_MODE.CURSOR);
    }
}