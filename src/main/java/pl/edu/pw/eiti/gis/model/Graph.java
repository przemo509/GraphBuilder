package pl.edu.pw.eiti.gis.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Graph {
    private List<GraphNode> nodes = new ArrayList<>();

    public GraphNode addNode(Point position) {
        GraphNode node = new GraphNode(nodes.size(), position, GraphNode.COLOR_NEW);
        nodes.add(node);
        return node;
    }

    public GraphNode getNode(Point position) {
        List<GraphNode> closeNodes = nodes.stream()
                .filter(node -> node.getPosition().distance(position) <= GraphNode.SIZE / 2)
                .collect(Collectors.toList());
        switch (closeNodes.size()) {
            case 0:
                return null;
            case 1:
                return closeNodes.get(0);
            default:
                throw new IllegalStateException(String.format("Znaleziono nieprawidłową liczbę węzłów w punkcie [%1$d, %2$d]: %3$d", position.x, position.y, closeNodes.size()));
        }
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }
}
