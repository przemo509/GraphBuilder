package pl.edu.pw.eiti.gis.export;

import pl.edu.pw.eiti.gis.gui.GraphDrawingUtils;
import pl.edu.pw.eiti.gis.model.Graph;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;

public class ExportUtils {

    private static final String CELL_SEPARATOR = "\t";
    private static final String LINE_SEPARATOR = System.lineSeparator();

    public static void graphToClipboard(Graph graph, ExportTypeEnum exportType, MatrixTypeEnum matrixType, int graphImageWidth, int graphImageHeight) {
        switch (exportType) {
            case TEXT:
                exportGraphAsText(graph, matrixType);
                break;
            case MATH_ML:
                exportGraphAsMathML(graph, matrixType);
                break;
            case MATRIX_IMAGE:
                exportGraphAsMathMLImage(graph, matrixType);
                break;
            case GRAPH_IMAGE:
                exportGraphAsImage(graph, graphImageWidth, graphImageHeight);
                break;
        }
    }

    private static void exportGraphAsText(Graph graph, MatrixTypeEnum matrixType) {
        textToClipboard(
                graphMatrixToText(
                        graphToMatrix(graph, matrixType)));
    }

    private static void exportGraphAsMathML(Graph graph, MatrixTypeEnum matrixType) {
        textToClipboard(
                mathMLToText(
                        graphToMathML(
                                graphToMatrix(graph, matrixType))));
    }

    private static void exportGraphAsMathMLImage(Graph graph, MatrixTypeEnum matrixType) {
        imageToClipboard(
                mathMLToImage(
                        graphToMathML(
                                graphToMatrix(graph, matrixType))));
    }

    private static void exportGraphAsImage(Graph graph, int graphImageWidth, int graphImageHeight) {
        imageToClipboard(
                graphToImage(graph, graphImageWidth, graphImageHeight));
    }

    private static int[][] graphToMatrix(Graph graph, MatrixTypeEnum matrixType) {
        int[][] matrix = new int[graph.getVertices().size()][graph.getVertices().size()];
        graph.getAdjacency().forEach((adjacencyIndexes, edgesList) -> {
            matrix[adjacencyIndexes.getIndex1() - 1][adjacencyIndexes.getIndex2() - 1] = 1;
            if(!graph.getType().isDirected()) {
                matrix[adjacencyIndexes.getIndex2() - 1][adjacencyIndexes.getIndex1() - 1] = 1;
            }
        });

        return matrix;
    }

    private static String graphMatrixToText(int[][] graphMatrix) {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = "";
        for (int[] columns : graphMatrix) {
            builder.append(lineSeparator);
            String cellSeparator = "";
            for (int cell : columns) {
                builder.append(cellSeparator).append(cell);
                cellSeparator = CELL_SEPARATOR;
            }
            lineSeparator = LINE_SEPARATOR;
        }
        return builder.toString();
    }

    private static String mathMLToText(String mathML) {
        return mathML;
    }

    private static String graphToMathML(int[][] graphMatrix) {
        // TODO
        return "<MathML>: " + graphMatrix.length + "x" + graphMatrix[0].length;
    }

    private static Image mathMLToImage(String mathML) {
        // TODO
        return null;
    }

    private static Image graphToImage(Graph graph, int imageWidth, int imageHeight) {
        BufferedImage bi = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        GraphDrawingUtils.drawGraph(g2d, graph, imageWidth, imageHeight);
        g2d.dispose();
        return bi;
    }

    private static void textToClipboard(String data) {
        dataToClipboard(new StringSelection(data));
    }

    private static void imageToClipboard(Image image) {
        dataToClipboard(new ClipboardImage(image));
    }

    private static void dataToClipboard(Transferable data) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(data, null);
    }
}
