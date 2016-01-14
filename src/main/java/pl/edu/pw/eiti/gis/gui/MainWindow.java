package pl.edu.pw.eiti.gis.gui;

import pl.edu.pw.eiti.gis.gui.dialog.EditingEdgeDialog;
import pl.edu.pw.eiti.gis.gui.listener.DrawingPlaneMouseListener;
import pl.edu.pw.eiti.gis.gui.listener.DrawingPlaneMouseMotionListener;
import pl.edu.pw.eiti.gis.model.Graph;
import pl.edu.pw.eiti.gis.model.GraphEdge;
import pl.edu.pw.eiti.gis.model.GraphType;
import pl.edu.pw.eiti.gis.model.GraphVertex;
import pl.edu.pw.eiti.gis.options.Options;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private Graph graph;
    private final JPanel drawingPlane = new JPanel(true);
    private final MainToolBar mainToolBar = new MainToolBar();
    private final MainMenuBar mainMenuBar = new MainMenuBar(this);
    private final DrawingPlaneMouseMotionListener mouseMotionListener;

    public MainWindow() {
        super("GraphBuilder");
        setIconImage(new ImageIcon(MainWindow.class.getResource("/program_icon.png")).getImage());
        setSize(800, 600);
        centerWindow();

        addComponents();

        drawingPlane.addMouseListener(new DrawingPlaneMouseListener(this));
        mouseMotionListener = new DrawingPlaneMouseMotionListener(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        newGraph(false, false, false);
        mainMenuBar.showNewGraphDialogForTheFirstTime();
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }

    private void addComponents() {
        setJMenuBar(mainMenuBar);

        setLayout(new BorderLayout());
        add(mainToolBar, BorderLayout.PAGE_START);
        add(drawingPlane, BorderLayout.CENTER);
    }

    public void onMouseClick(Point position) {
        GraphVertex clickedVertex = graph.getVertex(position);
        if (mainToolBar.getToolAddingVertices().isSelected() && clickedVertex == null) {
            graph.addVertex(position);
        } else if (mainToolBar.getToolAddingEdges().isSelected() && clickedVertex != null) {
            graph.tryToAddEdge(clickedVertex);
        } else if (mainToolBar.getToolMovingVertices().isSelected()) {
            if (clickedVertex != null) {
                if (graph.getSelectedVertex() == null) {
                    graph.selectVertex(clickedVertex);
                    drawingPlane.addMouseMotionListener(mouseMotionListener);
                } else {
                    drawingPlane.removeMouseMotionListener(mouseMotionListener);
                    graph.moveSelectedVertex(position);
                    graph.deselectVertex();
                }
            }
        } else if (mainToolBar.getToolEditingEdges().isSelected() && Options.getInstance().showEdgeLabels()) {
            GraphEdge clickedEdge = graph.getEdge(position);
            if (clickedEdge != null) {
                showEditingEdgeWeightDialog(clickedEdge);
            }
        } else if (mainToolBar.getToolRemovingVertices().isSelected() && clickedVertex != null) {
            graph.removeVertex(clickedVertex);
        }
        repaint();
    }

    private void showEditingEdgeWeightDialog(GraphEdge edge) {
        EditingEdgeDialog dialog = new EditingEdgeDialog(this, edge);
        dialog.setVisible(true);
    }

    public void onMouseMoved(Point position) {
        graph.moveSelectedVertex(position);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponents(g);
        GraphDrawingUtils.drawGraph((Graphics2D) drawingPlane.getGraphics(), graph, drawingPlane.getWidth(), drawingPlane.getHeight());
    }

    public Graph getGraph() {
        return graph;
    }

    public JPanel getDrawingPlane() {
        return drawingPlane;
    }

    public void newGraph(boolean multiGraph, boolean directedGraph, boolean weightedGraph) {
        GraphType graphType = new GraphType(multiGraph, directedGraph, weightedGraph);
        graph = new Graph(graphType);
        mainToolBar.reset();
        mainMenuBar.setInitialState(graphType);
        repaint();
    }
}
