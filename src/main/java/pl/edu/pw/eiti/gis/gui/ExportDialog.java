package pl.edu.pw.eiti.gis.gui;

import javax.swing.*;

public class ExportDialog extends JDialog {

    private final MainWindow mainWindow;

    public ExportDialog(MainWindow mainWindow) {
        super(mainWindow, "Eksport grafu", true);
        this.mainWindow = mainWindow;

        setSize(500, 300);
        centerWindow();

        addComponents();

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }

    private void addComponents() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(getOptionsPanel());
        add(getButtonsPanel());
    }

    private JPanel getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(getMatrixOptions());
        panel.add(getTypeOptions());
        panel.add(getLineBreakOptions());
        return panel;
    }

    private JPanel getMatrixOptions() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        ButtonGroup group = new ButtonGroup();
        addRadioButton(panel, group, "macierz sąsiedztwa").setSelected(true);
        addRadioButton(panel, group, "pełna macierz incydencji");
        addRadioButton(panel, group, "macierz wag");

        return panel;
    }

    private JPanel getTypeOptions() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        ButtonGroup group = new ButtonGroup();
        addRadioButton(panel, group, "tekst").setSelected(true);
        addRadioButton(panel, group, "obraz");
        addRadioButton(panel, group, "Word");

        return panel;
    }

    private JPanel getLineBreakOptions() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        ButtonGroup group = new ButtonGroup();
        addRadioButton(panel, group, "Windows (CR+LF)").setSelected(true);
        addRadioButton(panel, group, "Unix (LF)");
        addRadioButton(panel, group, "MacOS (CR)");

        return panel;
    }

    private JRadioButton addRadioButton(JPanel panel, ButtonGroup group, String label) {
        JRadioButton btn = new JRadioButton(label);
        group.add(btn);
        panel.add(btn);
        return btn;
    }

    private JPanel getButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(getCopyToClipboardButton());
        panel.add(getSaveToFileButton());
        return panel;
    }

    private JButton getCopyToClipboardButton() {
        JButton btn = new JButton("Kopiuj do schowka");
        return btn;
    }

    private JButton getSaveToFileButton() {
        JButton btn = new JButton("Zapisz do pliku");
        return btn;
    }
}
