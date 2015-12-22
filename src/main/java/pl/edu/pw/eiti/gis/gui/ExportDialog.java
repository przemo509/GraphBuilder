package pl.edu.pw.eiti.gis.gui;

import pl.edu.pw.eiti.gis.export.ExportTypeEnum;
import pl.edu.pw.eiti.gis.export.ExportUtils;
import pl.edu.pw.eiti.gis.export.MatrixTypeEnum;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;

public class ExportDialog extends JDialog {

    private final MainWindow mainWindow;

    private final JRadioButton textExport = new JRadioButton("macierz tekstowa");
    private final JRadioButton mathMlExport = new JRadioButton("macierz w formacie MathML");
    private final JRadioButton matrixImageExport = new JRadioButton("rysunek macierzy");
    private final JRadioButton graphImageExport = new JRadioButton("rysunek grafu");

    private final JRadioButton neighbourMatrix = new JRadioButton("macierz sąsiedztwa");
    private final JRadioButton weightMatrix = new JRadioButton("macierz wag");
    private final JRadioButton fullIncidenceMatrix = new JRadioButton("pełna macierz incydencji");

    private final JButton exportButton = new JButton("Kopiuj do schowka");

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
        add(buildOptionsPanel());
        add(buildButtonsPanel());
    }

    private JPanel buildOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(new TitledBorder("Opcje eksportu"));

        panel.add(buildExportTypeOptions());
        panel.add(buildMatrixTypeOptions());

        return panel;
    }

    private JPanel buildExportTypeOptions() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Typ eksportu"));

        panel.add(textExport);
        panel.add(mathMlExport);
        panel.add(matrixImageExport);
        panel.add(graphImageExport);

        ButtonGroup group = new ButtonGroup();
        group.add(textExport);
        group.add(mathMlExport);
        group.add(matrixImageExport);
        group.add(graphImageExport);

        addExportTypeChangeListeners();
        textExport.doClick();

        return panel;
    }

    private JPanel buildMatrixTypeOptions() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Rodzaj macierzy"));

        panel.add(neighbourMatrix);
        panel.add(weightMatrix);
        panel.add(fullIncidenceMatrix);

        ButtonGroup group = new ButtonGroup();
        group.add(neighbourMatrix);
        group.add(weightMatrix);
        group.add(fullIncidenceMatrix);

        neighbourMatrix.setSelected(true);

        return panel;
    }

    private JPanel buildButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        exportButton.addActionListener(buildExportButtonListener());
        panel.add(exportButton);

        return panel;
    }

    private void addExportTypeChangeListeners() {
        ActionListener exportTypeChangeListener = buildExportTypeChangeListener();
        textExport.addActionListener(exportTypeChangeListener);
        mathMlExport.addActionListener(exportTypeChangeListener);
        matrixImageExport.addActionListener(exportTypeChangeListener);
        graphImageExport.addActionListener(exportTypeChangeListener);
    }

    private ActionListener buildExportTypeChangeListener() {
        return e -> {
            boolean matrixExportEnabled = !graphImageExport.equals(e.getSource());
            neighbourMatrix.setEnabled(matrixExportEnabled);
            weightMatrix.setEnabled(matrixExportEnabled);
            fullIncidenceMatrix.setEnabled(matrixExportEnabled);

        };
    }

    private ActionListener buildExportButtonListener() {
        return e -> {
            ExportTypeEnum exportType = getSelectedExportType();
            MatrixTypeEnum matrixType = getSelectedMatrixType();
            ExportUtils.graphToClipboard(mainWindow.getGraph(), exportType, matrixType, mainWindow.getDrawingPlane().getWidth(), mainWindow.getDrawingPlane().getHeight());
        };
    }

    private ExportTypeEnum getSelectedExportType() {
        ExportTypeEnum exportType = ExportTypeEnum.TEXT;
        if (textExport.isSelected()) {
            exportType = ExportTypeEnum.TEXT;
        } else if (mathMlExport.isSelected()) {
            exportType = ExportTypeEnum.MATH_ML;
        } else if (matrixImageExport.isSelected()) {
            exportType = ExportTypeEnum.MATRIX_IMAGE;
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
