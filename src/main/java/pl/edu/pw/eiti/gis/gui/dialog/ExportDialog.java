package pl.edu.pw.eiti.gis.gui.dialog;

import pl.edu.pw.eiti.gis.export.ExportTypeEnum;
import pl.edu.pw.eiti.gis.export.ExportUtils;
import pl.edu.pw.eiti.gis.export.MatrixTypeEnum;
import pl.edu.pw.eiti.gis.gui.MainWindow;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;

public class ExportDialog extends RadioButtonsDialog {

    private final MainWindow mainWindow;

    private JRadioButton textExport;
    private JRadioButton mathMlExport;
    private JRadioButton graphImageExport;

    private JRadioButton neighbourMatrix;
    private JRadioButton weightMatrix;
    private JRadioButton fullIncidenceMatrix;

    public ExportDialog(MainWindow mainWindow) {
        super(mainWindow, "Eksport grafu");
        this.mainWindow = mainWindow;
        addComponents("Kopiuj do schowka");
    }

    @Override
    protected JPanel buildOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(new TitledBorder("Opcje eksportu"));

        panel.add(buildExportTypeOptions());
        panel.add(buildMatrixTypeOptions());

        addExportTypeChangeListeners();
        textExport.doClick(); // fire listener

        return panel;
    }

    private JPanel buildExportTypeOptions() {
        textExport = new JRadioButton("macierz tekstowa", true);
        mathMlExport = new JRadioButton("macierz w formacie MathML");
        graphImageExport = new JRadioButton("rysunek grafu");

        return buildRadioButtonGroup("Typ eksportu", textExport, mathMlExport, graphImageExport);
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

    private void addExportTypeChangeListeners() {
        ActionListener exportTypeChangeListener = buildExportTypeChangeListener();
        textExport.addActionListener(exportTypeChangeListener);
        mathMlExport.addActionListener(exportTypeChangeListener);
        graphImageExport.addActionListener(exportTypeChangeListener);
    }

    private ActionListener buildExportTypeChangeListener() {
        return e -> {
            boolean matrixExportEnabled = !graphImageExport.equals(e.getSource());
            neighbourMatrix.setEnabled(matrixExportEnabled);
            weightMatrix.setEnabled(matrixExportEnabled && mainWindow.getGraph().getType().isWeighted());
            fullIncidenceMatrix.setEnabled(matrixExportEnabled);

        };
    }

    @Override
    protected ActionListener buildOkButtonListener() {
        return e -> {
            ExportTypeEnum exportType = getSelectedExportType();
            MatrixTypeEnum matrixType = getSelectedMatrixType();
            closeDialog();
            ExportUtils.graphToClipboard(mainWindow.getGraph(), exportType, matrixType, mainWindow.getDrawingPlane().getWidth(), mainWindow.getDrawingPlane().getHeight());
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
