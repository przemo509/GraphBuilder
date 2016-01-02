package pl.edu.pw.eiti.gis.gui;

import pl.edu.pw.eiti.gis.gui.dialog.MovingEdgeLabelDialog;
import pl.edu.pw.eiti.gis.gui.listener.DrawingPlaneMouseListener;
import pl.edu.pw.eiti.gis.gui.listener.DrawingPlaneMouseMotionListener;
import pl.edu.pw.eiti.gis.model.Graph;
import pl.edu.pw.eiti.gis.model.GraphEdge;
import pl.edu.pw.eiti.gis.model.GraphType;
import pl.edu.pw.eiti.gis.model.GraphVertex;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private Graph graph = new Graph();
    private final JPanel drawingPlane = new JPanel(true);
    private MainMenuBar mainMenuBar = new MainMenuBar(this);
    private final DrawingPlaneMouseMotionListener mouseMotionListener;

    public MainWindow() {
        super("GraphBuilder");
        setSize(800, 600);
        centerWindow();

        addComponents();

        drawingPlane.addMouseListener(new DrawingPlaneMouseListener(this));
        mouseMotionListener = new DrawingPlaneMouseMotionListener(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }

    private void addComponents() {
        setJMenuBar(mainMenuBar);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        add(drawingPlane);
    }

    public void onMouseClick(Point position) {
        GraphVertex clickedVertex = graph.getVertex(position);
        GraphEdge clickedEdge = graph.getEdge(position);
        if(mainMenuBar.getToolAddingVertices().isSelected() && clickedVertex == null) {
            graph.addVertex(position);
        } else if(mainMenuBar.getToolAddingEdges().isSelected() && clickedVertex != null) {
            graph.tryToAddEdge(clickedVertex);
        } else if(mainMenuBar.getToolMovingVertices().isSelected()) {
            if(clickedVertex != null ) {
                if(graph.getSelectedVertex() == null) {
                    graph.selectVertex(clickedVertex);
                    drawingPlane.addMouseMotionListener(mouseMotionListener);
                } else {
                    drawingPlane.removeMouseMotionListener(mouseMotionListener);
                    graph.moveSelectedVertex(position);
                    graph.deselectVertex();
                }
            }
        } else if(mainMenuBar.getToolMovingEdges().isSelected() && clickedEdge != null) {
            showMovingEdgeDialog(clickedEdge);
        }
        repaint();
    }

    private void showMovingEdgeDialog(GraphEdge edge) {
        MovingEdgeLabelDialog dialog = new MovingEdgeLabelDialog(this, edge);
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
        graph = new Graph(new GraphType(multiGraph, directedGraph, weightedGraph));
        repaint();
    }
}
