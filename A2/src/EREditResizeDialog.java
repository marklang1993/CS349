import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Created by LangChen on 2016/11/3.
 */

public class EREditResizeDialog extends JDialog {

    // Widgets, Containers
    private JPanel dialogPane;
    private JPanel _contentsPanel;
    private JLabel _labelWidth;
    private JTextField _textFieldWidth;
    private JLabel _labelHeight;
    private JTextField _textFieldHeight;
    private JPanel _buttonPanel;
    private JButton _okBtn;
    private JButton _cancelBtn;

    // Data
    private final int _UpperBound = 10000;
    private final int _LowerBound = 100;
    private Size _graphSize;

    public EREditResizeDialog(JFrame owner, Size graphSize) {
        super(owner, true);

        _graphSize = graphSize;

        initializeWidgets();
    }

    private void initializeWidgets() {
        dialogPane = new JPanel();
        _contentsPanel = new JPanel();
        _labelWidth = new JLabel();
        _textFieldWidth = new JTextField();
        _labelHeight = new JLabel();
        _textFieldHeight = new JTextField();
        _buttonPanel = new JPanel();
        _okBtn = new JButton();
        _cancelBtn = new JButton();

        //this
        setName("resizeDialog");
        setResizable(false);
        setTitle("Resize Canvas");
        setFont(new Font("Times New Roman", Font.ITALIC, 14));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // 1.dialogPane
        dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
        dialogPane.setLayout(new BorderLayout());

        //_contentsPanel
        _contentsPanel.setLayout(new GridBagLayout());
        ((GridBagLayout)_contentsPanel.getLayout()).columnWidths = new int[] {115, 0, 0};
        ((GridBagLayout)_contentsPanel.getLayout()).rowHeights = new int[] {0, 0, 9, 0, 0, 0};
        ((GridBagLayout)_contentsPanel.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
        ((GridBagLayout)_contentsPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

        //_labelWidth
        _labelWidth.setText("Width (px.) :");
        _labelWidth.setHorizontalAlignment(SwingConstants.CENTER);
        _labelWidth.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        _contentsPanel.add(_labelWidth, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));

        //_textFieldWidth
        _textFieldWidth.setText(new Integer(_graphSize.Width).toString());
        _textFieldWidth.setMinimumSize(new Dimension(179, 25));
        _textFieldWidth.setPreferredSize(new Dimension(179, 25));
        _textFieldWidth.setHorizontalAlignment(SwingConstants.CENTER);
        _contentsPanel.add(_textFieldWidth, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
            new Insets(0, 0, 0, 0), 0, 0));

        //_labelHeight
        _labelHeight.setText("Height (px.) :");
        _labelHeight.setHorizontalAlignment(SwingConstants.CENTER);
        _labelHeight.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        _contentsPanel.add(_labelHeight, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));

        //_textFieldHeight
        _textFieldHeight.setText(new Integer(_graphSize.Height).toString());
        _textFieldHeight.setMinimumSize(new Dimension(179, 25));
        _textFieldHeight.setPreferredSize(new Dimension(179, 25));
        _textFieldHeight.setHorizontalAlignment(SwingConstants.CENTER);
        _contentsPanel.add(_textFieldHeight, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
            new Insets(0, 0, 0, 0), 0, 0));

        dialogPane.add(_contentsPanel, BorderLayout.CENTER);

        // 2._buttonPanel
        _buttonPanel.setBorder(new EmptyBorder(12, 0, 0, 0));
        _buttonPanel.setLayout(new GridBagLayout());
        ((GridBagLayout)_buttonPanel.getLayout()).columnWidths = new int[] {0, 85, 80};
        ((GridBagLayout)_buttonPanel.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

        //_okBtn
        _okBtn.setText("OK");
        _okBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setValue();
            }
        });
        _buttonPanel.add(_okBtn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 5), 0, 0));

        //_cancelBtn
        _cancelBtn.setText("Cancel");
        _cancelBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
                dispose();
            }
        });
        _buttonPanel.add(_cancelBtn, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));

        dialogPane.add(_buttonPanel, BorderLayout.SOUTH);

        // 3.Final
        contentPane.add(dialogPane, BorderLayout.CENTER);
        this.pack();
        this.setLocationRelativeTo(getOwner());
    }

    private void setValue(){
        try{
            int tempWidth = Integer.parseInt(_textFieldWidth.getText());
            int tempHeight = Integer.parseInt(_textFieldHeight.getText());

            // Verify Range
            if(tempWidth > _UpperBound || tempWidth < _LowerBound ||
                    tempHeight > _UpperBound || tempHeight < _LowerBound){
                throw new ArithmeticException();
            }

            _graphSize.Width = tempWidth;
            _graphSize.Height = tempHeight;
        }
        catch (NumberFormatException e){
            // Wrong format
            JOptionPane.showMessageDialog(this,
                    "Input value is not a numeric value!",
                    "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;

        }
        catch (ArithmeticException e){
            // Wrong range ( _LowerBound < val < _UpperBound)
            JOptionPane.showMessageDialog(this,
                    "Out of Bound! Value should between " + _LowerBound + " and " + _UpperBound + ".",
                    "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        setVisible(false);
        dispose();
    }
}
