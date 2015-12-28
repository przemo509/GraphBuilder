package pl.edu.pw.eiti.gis.gui.dialog;

import pl.edu.pw.eiti.gis.gui.MainWindow;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;

public abstract class RadioButtonsDialog extends JDialog {

    protected final MainWindow mainWindow;

    public RadioButtonsDialog(MainWindow mainWindow, String dialogTitle, String okButtonLabel) {
        super(mainWindow, dialogTitle, true);
        this.mainWindow = mainWindow;

        setSize(500, 300);
        centerWindow();

        addComponents(okButtonLabel);

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }

    private void addComponents(String okButtonLabel) {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(buildOptionsPanel());
        add(buildButtonsPanel(okButtonLabel));
    }

    abstract protected JPanel buildOptionsPanel();

    protected JPanel buildRadioButtonGroup(String groupLabel, JRadioButton... buttons) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder(groupLabel));

        ButtonGroup group = new ButtonGroup();

        for (JRadioButton button : buttons) {
            panel.add(button);
            group.add(button);
        }

        return panel;
    }

    private JPanel buildButtonsPanel(String okButtonLabel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JButton okButton = new JButton(okButtonLabel);
        okButton.addActionListener(buildOkButtonListener());
        panel.add(okButton);

        return panel;
    }

    abstract protected ActionListener buildOkButtonListener();
}
