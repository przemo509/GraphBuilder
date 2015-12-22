package pl.edu.pw.eiti.gis.export;

import pl.edu.pw.eiti.gis.gui.GraphDrawingUtils;
import pl.edu.pw.eiti.gis.model.Graph;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;

public class ExportUtils {

    public static void graphToClipboard(Graph graph, ExportTypeEnum exportType, MatrixTypeEnum matrixType, int graphImageWidth, int graphImageHeight) {
        switch (exportType) {
            case TEXT:
                textToClipboard(graphToText(graph, matrixType));
                break;
            case MATH_ML:
                textToClipboard(mathMLToText(graphToMathML(graph, matrixType)));
                break;
            case MATRIX_IMAGE:
                imageToClipboard(mathMLToImage(graphToMathML(graph, matrixType)));
                break;
            case GRAPH_IMAGE:
                imageToClipboard(graphToImage(graph, graphImageWidth, graphImageHeight));
                break;
        }
    }

    private static String graphToText(Graph graph, MatrixTypeEnum matrixType) {
        return "TEXT: " + matrixType.name();
    }

    private static String mathMLToText(String mathML) {
        return mathML;
    }

    private static String graphToMathML(Graph graph, MatrixTypeEnum matrixType) {
        return "<MathML>: " + matrixType.name();
    }

    private static Image mathMLToImage(String mathML) {
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
