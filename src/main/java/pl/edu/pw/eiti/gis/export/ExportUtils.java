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

    public static void graphToClipboard(Graph graph, ExportTypeEnum exportType, MatrixTypeEnum matrixType, int graphImageWidth, int graphImageHeight, boolean exportHeaders, String noEdgeString) {
        switch (exportType) {
            case TEXT:
                exportGraphAsText(graph, matrixType, exportHeaders, noEdgeString);
                break;
            case MATH_ML:
                exportGraphAsMathML(graph, matrixType, exportHeaders, noEdgeString);
                break;
            case GRAPH_IMAGE:
                exportGraphAsImage(graph, graphImageWidth, graphImageHeight);
                break;
            case VREP:
                exportGraphAsVRep(graph);
                break;
        }
    }

    private static void exportGraphAsText(Graph graph, MatrixTypeEnum matrixType, boolean exportHeaders, String noEdgeString) {
        textToClipboard(
                graphMatrixToText(
                        graphToMatrix(graph, matrixType), exportHeaders, noEdgeString));
    }

    private static void exportGraphAsMathML(Graph graph, MatrixTypeEnum matrixType, boolean exportHeaders, String noEdgeString) {
        textToClipboard(
                mathMLToText(
                        graphToMathML(
                                graphToMatrix(graph, matrixType), exportHeaders, noEdgeString)));
    }

    private static void exportGraphAsImage(Graph graph, int graphImageWidth, int graphImageHeight) {
        imageToClipboard(
                graphToImage(graph, graphImageWidth, graphImageHeight));
    }

    private static void exportGraphAsVRep(Graph graph) {
        textToClipboard(
                graphToVRepText(graph));
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

    private static String graphMatrixToText(int[][] matrix, boolean exportHeaders, String noEdgeString) {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = "";

        if(exportHeaders && matrix.length > 0) {
            builder.append(""); // row headers header
            for (int i = 0; i < matrix[0].length; ++i) {
                builder.append(CELL_SEPARATOR).append(i + 1);
            }
            lineSeparator = LINE_SEPARATOR;
        }

        for (int i = 0; i < matrix.length; ++i) {
            int[] matrixRow = matrix[i];
            builder.append(lineSeparator);
            String cellSeparator = "";

            if(exportHeaders && matrixRow.length > 0) {
                builder.append(i + 1);
                cellSeparator = CELL_SEPARATOR;
            }

            for (int cell : matrixRow) {
                builder.append(cellSeparator).append(matrixValueToString(cell, noEdgeString));
                cellSeparator = CELL_SEPARATOR;
            }
            lineSeparator = LINE_SEPARATOR;
        }
        return builder.toString();
    }

    private static String matrixValueToString(int value, String noEdgeString) {
        return value != 0 ? String.valueOf(value) : noEdgeString;
    }

    private static String mathMLToText(String mathML) {
        return mathML;
    }

    private static String graphToMathML(int[][] matrix, boolean exportHeaders, String noEdgeString) {
        StringBuilder sb = new StringBuilder("<mml:math xmlns:mml=\"http://www.w3.org/1998/Math/MathML\">\n")
                .append("    <mml:mfenced open=\"[\" close=\"]\" separators=\"|\">\n")
                .append("        <mml:mrow>\n")
                .append("            <mml:mtable>\n");
        if(exportHeaders && matrix.length > 0) {
            sb
            .append("                <mml:mtr>\n")
            .append("                    <mml:mtd>\n")
            .append("                        <mml:mn></mml:mn>\n")
            .append("                    </mml:mtd>\n");
            for (int i = 0; i < matrix[0].length; ++i) {
                sb
                .append("                    <mml:mtd>\n")
                .append("                        <mml:mn>").append(i + 1).append("</mml:mn>\n")
                .append("                    </mml:mtd>\n");
            }
            sb.append("                </mml:mtr>\n");
        }
        for (int i = 0; i < matrix.length; ++i) {
            int[] matrixRow = matrix[i];
            sb.append("                <mml:mtr>\n");
            if(exportHeaders && matrixRow.length > 0) {
                sb
                .append("                    <mml:mtd>\n")
                .append("                        <mml:mn>").append(i + 1).append("</mml:mn>\n")
                .append("                    </mml:mtd>\n");
            }
            for (int matrixCell : matrixRow) {
                sb
                .append("                    <mml:mtd>\n")
                .append("                        <mml:mn>").append(matrixValueToString(matrixCell, noEdgeString)).append("</mml:mn>\n")
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

    private static String graphToVRepText(Graph graph) {
        StringBuilder sb = new StringBuilder(graph.getVertices().size() * 11 + graph.getEdges().size() * 5);
        graph.getVertices().values().forEach(vertex ->
                sb
                .append(vertex.getIndex())
                .append("\t")
                .append(vertex.getPosition().x)
                .append("\t")
                .append(vertex.getPosition().y)
                .append("\n")
        );
        sb.append("#\n");
        graph.getEdges().values().forEach(edge ->
                sb
                .append(edge.getIndex())
                .append("\t")
                .append(edge.getStartVertex().getIndex())
                .append("\t")
                .append(edge.getEndVertex().getIndex())
                .append("\n")
        );
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
