package pl.edu.pw.eiti.gis.gui;

import pl.edu.pw.eiti.gis.gui.listener.DrawingPlaneMouseListener;
import pl.edu.pw.eiti.gis.model.Graph;
import pl.edu.pw.eiti.gis.model.GraphNode;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final Graph graph = new Graph();
    private final JPanel drawingPlane = new JPanel(true);
    private MainMenuBar mainMenuBar = new MainMenuBar(this);

    public MainWindow() {
        super("GraphBuilder");
        setSize(800, 600);
        centerWindow();

        addComponents();

        drawingPlane.addMouseListener(new DrawingPlaneMouseListener(this));
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
        GraphNode clickedNode = graph.getNode(position);
        if(mainMenuBar.getToolAddingNodes().isSelected() && clickedNode == null) {
            graph.addNode(position);
        } else if(mainMenuBar.getToolAddingEdges().isSelected() && clickedNode != null) {
            graph.tryToAddEdge(clickedNode);
        } else if(mainMenuBar.getToolMovingNodes().isSelected()) {
            if(clickedNode != null ) {
                graph.selectNode(clickedNode);
            } else {
                graph.moveSelectedNode(position);
            }
        }
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
}
