import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Created by LangChen on 2016/10/5.
 */
public class EREditMainView extends JPanel implements EREditIView{

    // Widgets
    private JPanel _btnPanel;
    private JButton _newBtn;
    private JButton _boxBtn;
    private JButton _arrowBtn;
    private JButton _textBtn;
    private JButton _eraserBtn;
    private JButton _resizeBtn;
    private JButton _zoomInBtn;
    private JButton _zoomOutBtn;
    private JPanel _displayPanel;
    private JScrollPane _boxDisplayPane;
    private JTable _boxDisplayList;
    private JScrollPane _arrowDisplayPane;
    private JTable _arrowDisplayList;
    private JPanel _drawPanel;
    private JScrollBar _vSrcollBar;
    private JScrollBar _hSrcollBar;
    private JButton _adjustBtn;

    // Controller
    private EREditController _controller;

    // Table Data
    private EREditTableModel<EREditEntity> _boxTableModel;
    private EREditTableModel<EREditArrow> _arrowTableModel;

    // List of IView
    private ArrayList<EREditIView> _listIView;

    public EREditMainView(EREditController controller,
                          ArrayList<EREditEntity> entityList,
                          ArrayList<EREditArrow> arrowList) {
        _controller = controller;

        initializeWidgets(entityList, arrowList);
    }

    private void initializeWidgets(ArrayList<EREditEntity> entityList, ArrayList<EREditArrow> arrowList) {
        _btnPanel = new JPanel();
        _newBtn = new JButton();
        _boxBtn = new JButton();
        _arrowBtn = new JButton();
        _textBtn = new JButton();
        _eraserBtn = new JButton();
        _resizeBtn = new JButton();
        _zoomInBtn = new JButton();
        _zoomOutBtn = new JButton();
        _displayPanel = new JPanel();
        _drawPanel = new JPanel();
        _vSrcollBar = new JScrollBar(Adjustable.VERTICAL, 0, 0, 0, 100);
        _hSrcollBar = new JScrollBar(Adjustable.HORIZONTAL, 0, 0, 0, 100);
        _adjustBtn = new JButton();

        DefaultTableCellRenderer cellRender = new DefaultTableCellRenderer();

        _boxDisplayPane = new JScrollPane();
        _boxTableModel = new EREditTableModel<>(entityList, "Entity", _controller);
        _boxDisplayList = new JTable(_boxTableModel) {
            @Override
            public boolean isCellEditable(int row, int coloum) //Set Table uneditable
            {
                return true;
            }
        };
        _boxDisplayList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        _boxDisplayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableColumn mainColumn = _boxDisplayList.getColumn("Entity");
        mainColumn.setPreferredWidth(158);
        mainColumn.setResizable(false);
        cellRender.setHorizontalAlignment(SwingConstants.CENTER);
        mainColumn.setCellRenderer(cellRender);

        _arrowDisplayPane = new JScrollPane();
        _arrowTableModel = new EREditTableModel<>(arrowList, "Relationship", _controller);
        _arrowDisplayList = new JTable(_arrowTableModel){
            @Override
            public boolean isCellEditable(int row, int coloum) //Set Table uneditable
            {
                return false;
            }
        };
        _arrowDisplayList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        _arrowDisplayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainColumn = _arrowDisplayList.getColumn("Relationship");
        mainColumn.setPreferredWidth(158);
        mainColumn.setResizable(false);
        cellRender.setHorizontalAlignment(SwingConstants.CENTER);
        mainColumn.setCellRenderer(cellRender);

        //This Panel
        this.setMinimumSize(new Dimension(16, 32));
        this.setLayout(new GridBagLayout());
        ((GridBagLayout) this.getLayout()).columnWidths = new int[]{166, 429, 20, 0};
        ((GridBagLayout) this.getLayout()).rowHeights = new int[]{26, 460, 20, 0};
        ((GridBagLayout) this.getLayout()).columnWeights = new double[]{0.0, 1.0, 0.0, 1.0E-4};
        ((GridBagLayout) this.getLayout()).rowWeights = new double[]{0.0, 1.0, 0.0, 1.0E-4};

        //1. _btnPanel
        _btnPanel.setFont(_btnPanel.getFont().deriveFont(_btnPanel.getFont().getSize() - 14f));
        _btnPanel.setLayout(new GridBagLayout());
        ((GridBagLayout) _btnPanel.getLayout()).columnWidths = new int[]{65, 65, 65, 65, 25, 75, 75, 75, 25, 25, 25, 0, 0};
        ((GridBagLayout) _btnPanel.getLayout()).rowHeights = new int[]{0, 0, 0};
        ((GridBagLayout) _btnPanel.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0E-4};
        ((GridBagLayout) _btnPanel.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0E-4};

        //_newBtn
        _newBtn.setMinimumSize(new Dimension(70, 20));
        _newBtn.setMaximumSize(new Dimension(70, 20));
        _newBtn.setPreferredSize(new Dimension(70, 20));
        _newBtn.setFont(_newBtn.getFont().deriveFont(_newBtn.getFont().getStyle() | Font.BOLD, _newBtn.getFont().getSize() - 3f));
        _newBtn.setOpaque(false);
        _newBtn.setText("New");
        _newBtn.addActionListener(new ButtonActionListener(_controller));
        _btnPanel.add(_newBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 5), 0, 0));

        //_resizeBtn
        _resizeBtn.setMinimumSize(new Dimension(70, 20));
        _resizeBtn.setMaximumSize(new Dimension(70, 20));
        _resizeBtn.setPreferredSize(new Dimension(70, 20));
        _resizeBtn.setFont(_resizeBtn.getFont().deriveFont(_resizeBtn.getFont().getStyle() | Font.BOLD, _resizeBtn.getFont().getSize() - 3f));
        _resizeBtn.setText("Resize");
        _resizeBtn.addActionListener(new ButtonActionListener(_controller));
        _btnPanel.add(_resizeBtn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

        //_zoomInBtn
        _zoomInBtn.setMinimumSize(new Dimension(70, 20));
        _zoomInBtn.setMaximumSize(new Dimension(70, 20));
        _zoomInBtn.setPreferredSize(new Dimension(70, 20));
        _zoomInBtn.setFont(_zoomInBtn.getFont().deriveFont(_zoomInBtn.getFont().getStyle() | Font.BOLD, _zoomInBtn.getFont().getSize() - 3f));
        _zoomInBtn.setOpaque(false);
        _zoomInBtn.setText("Zoom+");
        _zoomInBtn.addActionListener(new ButtonActionListener(_controller));
        _btnPanel.add(_zoomInBtn, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

        //_zoomOutBtn
        _zoomOutBtn.setMinimumSize(new Dimension(70, 20));
        _zoomOutBtn.setMaximumSize(new Dimension(70, 20));
        _zoomOutBtn.setPreferredSize(new Dimension(70, 20));
        _zoomOutBtn.setFont(_zoomOutBtn.getFont().deriveFont(_zoomOutBtn.getFont().getStyle() | Font.BOLD, _zoomOutBtn.getFont().getSize() - 3f));
        _zoomOutBtn.setOpaque(false);
        _zoomOutBtn.setText("Zoom-");
        _zoomOutBtn.addActionListener(new ButtonActionListener(_controller));
        _btnPanel.add(_zoomOutBtn, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

        //_boxBtn
        _boxBtn.setMinimumSize(new Dimension(70, 20));
        _boxBtn.setMaximumSize(new Dimension(70, 20));
        _boxBtn.setPreferredSize(new Dimension(70, 20));
        _boxBtn.setFont(_boxBtn.getFont().deriveFont(_boxBtn.getFont().getStyle() | Font.BOLD, _boxBtn.getFont().getSize() - 3f));
        _boxBtn.setOpaque(false);
        _boxBtn.setText("Box");
        _boxBtn.addActionListener(new ButtonActionListener(_controller));
        _btnPanel.add(_boxBtn, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 5), 0, 0));

        //_arrowBtn
        _arrowBtn.setMinimumSize(new Dimension(70, 20));
        _arrowBtn.setMaximumSize(new Dimension(70, 20));
        _arrowBtn.setPreferredSize(new Dimension(70, 20));
        _arrowBtn.setFont(_arrowBtn.getFont().deriveFont(_arrowBtn.getFont().getStyle() | Font.BOLD, _arrowBtn.getFont().getSize() - 3f));
        _arrowBtn.setOpaque(false);
        _arrowBtn.setText("Arrow");
        _arrowBtn.addActionListener(new ButtonActionListener(_controller));
        _btnPanel.add(_arrowBtn, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

        // _textBtn
        _textBtn.setMinimumSize(new Dimension(70, 20));
        _textBtn.setMaximumSize(new Dimension(70, 20));
        _textBtn.setPreferredSize(new Dimension(70, 20));
        _textBtn.setFont(_textBtn.getFont().deriveFont(_textBtn.getFont().getStyle() | Font.BOLD, _textBtn.getFont().getSize() - 3f));
        _textBtn.setOpaque(false);
        _textBtn.setText("Text");
        _textBtn.addActionListener(new ButtonActionListener(_controller));
        _btnPanel.add(_textBtn, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

        //_eraserBtn
        _eraserBtn.setMinimumSize(new Dimension(70, 20));
        _eraserBtn.setMaximumSize(new Dimension(70, 20));
        _eraserBtn.setPreferredSize(new Dimension(70, 20));
        _eraserBtn.setFont(_eraserBtn.getFont().deriveFont(_eraserBtn.getFont().getStyle() | Font.BOLD, _eraserBtn.getFont().getSize() - 3f));
        _eraserBtn.setOpaque(false);
        _eraserBtn.setText("Eraser");
        _eraserBtn.addActionListener(new ButtonActionListener(_controller));
        _btnPanel.add(_eraserBtn, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

        this.add(_btnPanel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

        //2. _displayPanel
        _displayPanel.setLayout(new GridLayout(2, 1, 5, 5));

        //_boxDisplayPane
        //_boxDisplayList
        _boxDisplayList.setName("BoxList");
        _boxDisplayList.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        _boxDisplayList.setPreferredScrollableViewportSize(new Dimension(150, 32));
        _boxDisplayList.addMouseListener(new TableClickListener(_controller));
        _boxDisplayPane.setViewportView(_boxDisplayList);
        _displayPanel.add(_boxDisplayPane);

        //_arrowDisplayPane
        //_arrowDisplayList
        _arrowDisplayList.setName("ArrowList");
        _arrowDisplayList.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        _arrowDisplayList.setPreferredScrollableViewportSize(new Dimension(150, 32));
        _arrowDisplayList.addMouseListener(new TableClickListener(_controller));
        _arrowDisplayPane.setViewportView(_arrowDisplayList);
        _displayPanel.add(_arrowDisplayPane);

        this.add(_displayPanel, new GridBagConstraints(0, 1, 1, 2, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

        //3. _drawPanel
        _drawPanel.addMouseListener(new DrawPanelClickListener(_controller));
        _drawPanel.addMouseMotionListener(new DrawPanelMotionListener(_controller));
        _drawPanel.addMouseWheelListener(new DrawPanelWheelListener(_controller));
        _drawPanel.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        _drawPanel.setForeground(null);
        _drawPanel.setBackground(Color.white);
        _drawPanel.setLayout(null);

        // compute preferred size
//        Dimension preferredSize = new Dimension();
//        for (int i = 0; i < _drawPanel.getComponentCount(); i++) {
//            Rectangle bounds = _drawPanel.getComponent(i).getBounds();
//            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
//            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
//
//            Insets insets = _drawPanel.getInsets();
//            preferredSize.width += insets.right;
//            preferredSize.height += insets.bottom;
//            _drawPanel.setMinimumSize(preferredSize);
//            _drawPanel.setPreferredSize(preferredSize);
//        }
        this.add(_drawPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

        //4. _vSrcollBar
        _vSrcollBar.setName("VScrollBar");
        _vSrcollBar.addAdjustmentListener(new ScrollBarAdjustListener(_controller));
        this.add(_vSrcollBar, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

        //5. _hSrcollBar
        _hSrcollBar.setName("HScrollBar");
        _hSrcollBar.addAdjustmentListener(new ScrollBarAdjustListener(_controller));
        this.add(_hSrcollBar, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

        //6. _adjustBtn
        _adjustBtn.setPreferredSize(new Dimension(20, 20));
        _adjustBtn.setMaximumSize(new Dimension(20, 20));
        _adjustBtn.setMinimumSize(new Dimension(20, 20));
        _adjustBtn.setBackground(Color.red);
        this.add(_adjustBtn, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        //7. Resize Window
        this.addComponentListener(new EREditMainViewListner(_controller));
    }

    public Size GetDisplayPaneSize(){
        return new Size(_drawPanel.getWidth(), _drawPanel.getHeight());
    }

    public void SetIViewList(ArrayList<EREditIView> listIView){
        _listIView = listIView;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) _drawPanel.getGraphics();

        // Draw elements
        if(g2 != null){
            _boxTableModel.fireTableDataChanged();
            _arrowTableModel.fireTableDataChanged();

            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, _drawPanel.getWidth(), _drawPanel.getHeight());

            if(_listIView != null) {
                // Draw
                for (EREditIView drawable: _listIView) {
                    drawable.draw(g2);
                }
            }
        }
    }
}

class EREditTableModel<T> extends AbstractTableModel {

    private ArrayList<T> _dataList;
    private String _title;
    private EREditController _mainController;

    public EREditTableModel(ArrayList<T> dataList, String title, EREditController mainController){
        _dataList = dataList;
        _title = title;
        _mainController = mainController;
    }

    public String getColumnName(int columnIndex){
        return _title;
    }

    @Override
    public int getRowCount() { return _dataList.size(); }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((EREditExport)_dataList.get(rowIndex)).GetText();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        T element = _dataList.get(rowIndex);
        if(element instanceof EREditEntity){
            ((EREditEntity)element).SetText((String)aValue);
            _mainController.JTableUpdateViewHandler();
        }
    }
}

class ButtonActionListener implements ActionListener{

    private EREditController _controller;

    public ButtonActionListener(EREditController controller){
        _controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonName = ((JButton)(e.getSource())).getText();
        _controller.ButtonEventHandler(buttonName);
    }
}

class TableClickListener extends MouseAdapter{

    private EREditController _controller;

    public TableClickListener(EREditController controller){
        _controller = controller;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JTable table = (JTable)e.getSource();
        _controller.TableEventHandler(table.getName(), table.getSelectedRow());
    }
}

class ScrollBarAdjustListener implements AdjustmentListener{

    private EREditController _controller;

    public ScrollBarAdjustListener(EREditController controller)
    {
        _controller = controller;
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        JScrollBar scrollBar = (JScrollBar) e.getSource();
        double percentage = (scrollBar.getValue()  - scrollBar.getMinimum()) /
                (double)(scrollBar.getMaximum() - scrollBar.getMinimum());
        _controller.ScrollBarEventHandler(scrollBar.getName(), percentage);
    }
}

class DrawPanelClickListener extends MouseAdapter{
    private EREditController _controller;

    public DrawPanelClickListener(EREditController controller){
        _controller = controller;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            _controller.DrawPanelClickEventHandler(new Point(e.getX(), e.getY()), e.getClickCount() == 2);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON3) {
            _controller.DrawPanelPressedEventHandler(new Point(e.getX(), e.getY()));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON3) {
            _controller.DrawPanelReleasedEventHandler();
        }
    }
}

class DrawPanelMotionListener extends MouseMotionAdapter{

    private EREditController _controller;

    public DrawPanelMotionListener(EREditController controller){
        _controller = controller;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        _controller.DrawPanelDragEventHandler(new Point(e.getX(), e.getY()));
    }
}

class DrawPanelWheelListener implements MouseWheelListener{
    private EREditController _controller;

    public DrawPanelWheelListener(EREditController controller)
    {
        _controller = controller;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        _controller.DrawPanelWheelEventHandler(e.getWheelRotation() > 0);
    }
}

class EREditMainViewListner extends ComponentAdapter{
    private EREditController _controller;

    public EREditMainViewListner(EREditController controller)
    {
        _controller = controller;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        _controller.WindowResizeEventHandler(e);
    }
}