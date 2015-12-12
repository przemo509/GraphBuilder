package pl.edu.pw.eiti.gis.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ExportDialog extends JDialog {

    private final MainWindow mainWindow;
    private final JRadioButton textExport = new JRadioButton("macierz tekstowa");
    private final JRadioButton wordExport = new JRadioButton("macierz w formacie MathML");
    private final JRadioButton imageExport = new JRadioButton("rysunek macierzy");
    private final JRadioButton graphImageExport = new JRadioButton("rysunek grafu");
    private final JRadioButton neighbourMatrix = new JRadioButton("macierz sąsiedztwa");
    private final JRadioButton fullIncidenceMatrix = new JRadioButton("pełna macierz incydencji");
    private final JRadioButton weightMatrix = new JRadioButton("macierz wag");

    private final JButton exportBtn = new JButton("Kopiuj do schowka");

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
        panel.setBorder(new TitledBorder("Opcje eksportu"));

        panel.add(getExportTypeOptions());
        panel.add(getExportMatrixOptions());

        return panel;
    }

    private JPanel getExportTypeOptions() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Typ eksportu"));

        panel.add(textExport);
        panel.add(wordExport);
        panel.add(imageExport);
        panel.add(graphImageExport);

        ButtonGroup group = new ButtonGroup();
        group.add(textExport);
        group.add(wordExport);
        group.add(imageExport);
        group.add(graphImageExport);

        textExport.setSelected(true);

        return panel;
    }

    private JPanel getExportMatrixOptions() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Rodzaj macierzy"));

        panel.add(neighbourMatrix);
        panel.add(fullIncidenceMatrix);
        panel.add(weightMatrix);

        ButtonGroup group = new ButtonGroup();
        group.add(neighbourMatrix);
        group.add(fullIncidenceMatrix);
        group.add(weightMatrix);

        neighbourMatrix.setSelected(true);

        return panel;
    }

    private JPanel getButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(exportBtn);
        return panel;
    }
}
