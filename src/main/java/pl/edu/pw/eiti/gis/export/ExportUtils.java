package pl.edu.pw.eiti.gis.export;

import pl.edu.pw.eiti.gis.gui.GraphDrawingUtils;
import pl.edu.pw.eiti.gis.model.Graph;
import pl.edu.pw.eiti.gis.model.GraphEdge;
import pl.edu.pw.eiti.gis.model.GraphType;

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

    private static void exportGraphAsImage(Graph graph, int graphImageWidth, int graphImageHeight) {
        imageToClipboard(
                graphToImage(graph, graphImageWidth, graphImageHeight));
    }

    static int[][] graphToMatrix(Graph graph, MatrixTypeEnum matrixType) {
        int[][] matrix = new int[graph.getVertices().size()][MatrixTypeEnum.FULL_INCIDENCE.equals(matrixType) ? graph.getEdges().size() : graph.getVertices().size()];
        graph.getEdges().forEach((edgeIndex, edge) -> {
            int startVertex = edge.getStartVertex().getIndex();
            int endVertex = edge.getEndVertex().getIndex();

            if(MatrixTypeEnum.FULL_INCIDENCE.equals(matrixType)) {
                if(startVertex == endVertex) {
                    // loop
                    matrix[startVertex - 1][edgeIndex - 1] = 2;
                } else {
                    matrix[startVertex - 1][edgeIndex - 1] = graph.getType().isDirected() ? -1 : 1;
                    matrix[endVertex - 1][edgeIndex - 1] = 1;
                }
            } else {
                matrix[startVertex - 1][endVertex - 1] += getNeighbourOrWeightMatrixValue(graph.getType(), edge, matrixType);

                if (!graph.getType().isDirected() && startVertex != endVertex) {
                    matrix[endVertex - 1][startVertex - 1] += getNeighbourOrWeightMatrixValue(graph.getType(), edge, matrixType);
                }
            }
        });

        return matrix;
    }

    private static int getNeighbourOrWeightMatrixValue(GraphType graphType, GraphEdge edge, MatrixTypeEnum matrixType) {
        return MatrixTypeEnum.WEIGHT.equals(matrixType) && graphType.isWeighted() ? edge.getWeight() : 1;
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

    private static String graphToMathML(int[][] matrix) {
        StringBuilder sb = new StringBuilder("<mml:math xmlns:mml=\"http://www.w3.org/1998/Math/MathML\">\n")
                .append("    <mml:mfenced open=\"[\" close=\"]\" separators=\"|\">\n")
                .append("        <mml:mrow>\n")
                .append("            <mml:mtable>\n");
        for (int[] matrixRow : matrix) {
            sb.append("                <mml:mtr>\n");
            for (int matrixCell : matrixRow) {
                sb
                .append("                    <mml:mtd>\n")
                .append("                        <mml:mn>")
                .append(matrixCell)
                .append("</mml:mn>\n")
                .append("                    </mml:mtd>\n");
            }
            sb.append("                </mml:mtr>\n");
        }
        sb
        .append("            </mml:mtable>\n")
        .append("        </mml:mrow>\n")
        .append("    </mml:mfenced>\n")
        .append("</mml:math>");
        return sb.toString();
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
