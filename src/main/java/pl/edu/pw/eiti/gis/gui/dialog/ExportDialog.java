package pl.edu.pw.eiti.gis.gui.dialog;

import pl.edu.pw.eiti.gis.export.ExportTypeEnum;
import pl.edu.pw.eiti.gis.export.ExportUtils;
import pl.edu.pw.eiti.gis.export.MatrixTypeEnum;
import pl.edu.pw.eiti.gis.gui.MainWindow;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ExportDialog extends RadioButtonsDialog {

    private final MainWindow mainWindow;

    private JRadioButton textExport;
    private JRadioButton mathMlExport;
    private JRadioButton graphImageExport;
    private JRadioButton vRepExport;

    private JRadioButton neighbourMatrix;
    private JRadioButton weightMatrix;
    private JRadioButton fullIncidenceMatrix;

    private JCheckBox exportHeadersCheckBox;
    private JTextField noEdgeStringTextInput;

    public ExportDialog(MainWindow mainWindow) {
        super(mainWindow, "Eksport grafu");
        this.mainWindow = mainWindow;
        addComponents("Kopiuj do schowka");
    }

    @Override
    protected JPanel buildOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Opcje eksportu"));

        panel.add(buildRadioButtonsPanel());
        panel.add(buildOtherOptionsPanel());
        panel.setMaximumSize(panel.getPreferredSize());

        addExportTypeChangeListeners();
        textExport.doClick(); // fire listener

        return panel;
    }

    private JPanel buildRadioButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(buildExportTypeOptions());
        panel.add(buildMatrixTypeOptions());

        return panel;
    }

    private JPanel buildExportTypeOptions() {
        textExport = new JRadioButton("macierz tekstowa", true);
        mathMlExport = new JRadioButton("macierz w formacie MathML");
        graphImageExport = new JRadioButton("rysunek grafu");
        vRepExport = new JRadioButton("V-Rep");

        return buildRadioButtonGroup("Typ eksportu", textExport, mathMlExport, graphImageExport, vRepExport);
    }

    private JPanel buildMatrixTypeOptions() {
        neighbourMatrix = new JRadioButton("macierz sąsiedztwa", true);
        weightMatrix = new JRadioButton("macierz wag");
        fullIncidenceMatrix = new JRadioButton("pełna macierz incydencji");

        boolean weighted = mainWindow.getGraph().getType().isWeighted();
        weightMatrix.setEnabled(weighted);
        weightMatrix.setToolTipText(weighted ? null : "Opcja dostępna tylko dla grafu ważonego");

        return buildRadioButtonGroup("Rodzaj macierzy", neighbourMatrix, weightMatrix, fullIncidenceMatrix);
    }

    private JPanel buildOtherOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 2));

        exportHeadersCheckBox = new JCheckBox("");
        addFormItem(panel, "Eksportuj nagłówki macierzy", exportHeadersCheckBox);

        noEdgeStringTextInput = new JTextField("0");
        addFormItem(panel, "Znak oznaczający brak krawędzi", noEdgeStringTextInput);

        return panel;
    }

    private void addFormItem(JPanel panel, String label, JComponent component) {
        panel.add(new JLabel(label + ":", SwingConstants.RIGHT));
        panel.add(component);
    }

    private void addExportTypeChangeListeners() {
        ActionListener exportTypeChangeListener = buildExportTypeChangeListener();
        textExport.addActionListener(exportTypeChangeListener);
        mathMlExport.addActionListener(exportTypeChangeListener);
        graphImageExport.addActionListener(exportTypeChangeListener);
        vRepExport.addActionListener(exportTypeChangeListener);
    }

    private ActionListener buildExportTypeChangeListener() {
        return e -> {
            boolean matrixExportEnabled = textExport.equals(e.getSource()) || mathMlExport.equals(e.getSource());
            neighbourMatrix.setEnabled(matrixExportEnabled);
            weightMatrix.setEnabled(matrixExportEnabled && mainWindow.getGraph().getType().isWeighted());
            fullIncidenceMatrix.setEnabled(matrixExportEnabled);
            exportHeadersCheckBox.setEnabled(matrixExportEnabled);
            noEdgeStringTextInput.setEnabled(matrixExportEnabled);
        };
    }

    @Override
    protected ActionListener buildOkButtonListener() {
        return e -> {
            ExportTypeEnum exportType = getSelectedExportType();
            MatrixTypeEnum matrixType = getSelectedMatrixType();
            closeDialog();
            ExportUtils.graphToClipboard(mainWindow.getGraph(), exportType, matrixType,
                    mainWindow.getDrawingPlane().getWidth(), mainWindow.getDrawingPlane().getHeight(),
                    exportHeadersCheckBox.isSelected(), noEdgeStringTextInput.getText());
            JOptionPane.showMessageDialog(null, "Eksport do schowka udany!\n\nMożna kontynuować pracę z grafem.", "Skopiowano pomyślnie!", JOptionPane.INFORMATION_MESSAGE);
        };
    }

    private ExportTypeEnum getSelectedExportType() {
        ExportTypeEnum exportType = ExportTypeEnum.TEXT;
        if (textExport.isSelected()) {
            exportType = ExportTypeEnum.TEXT;
        } else if (mathMlExport.isSelected()) {
            exportType = ExportTypeEnum.MATH_ML;
        } else if (graphImageExport.isSelected()) {
            exportType = ExportTypeEnum.GRAPH_IMAGE;
        } else if (vRepExport.isSelected()) {
            exportType = ExportTypeEnum.VREP;
        }
        return exportType;
    }

    private MatrixTypeEnum getSelectedMatrixType() {
        MatrixTypeEnum matrixType = MatrixTypeEnum.NEIGHBOUR;
        if (neighbourMatrix.isSelected()) {
            matrixType = MatrixTypeEnum.NEIGHBOUR;
        } else if (weightMatrix.isSelected()) {
            matrixType = MatrixTypeEnum.WEIGHT;
        } else if (fullIncidenceMatrix.isSelected()) {
            matrixType = MatrixTypeEnum.FULL_INCIDENCE;
        }
        return matrixType;
    }
}
