package pl.edu.pw.eiti.gis.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<GraphNode> nodes = new ArrayList<>();

    public GraphNode addNode(Point position) {
        GraphNode node = new GraphNode(nodes.size(), position);
        nodes.add(node);
        return node;
    }
}
