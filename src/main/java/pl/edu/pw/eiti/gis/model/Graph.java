package pl.edu.pw.eiti.gis.model;

import java.awt.*;
import java.util.*;

public class Graph {
    private SortedMap<Integer, GraphNode> nodes = new TreeMap<>();
    private SortedMap<Integer, GraphEdge> edges = new TreeMap<>();

    private GraphNode newEdgeStartNode;

    public GraphNode addNode(Point position) {
        GraphNode node = new GraphNode(nodes.size() + 1, position, GraphNode.COLOR_NEW);
        nodes.put(node.getIndex(), node);
        return node;
    }

    public GraphNode getNode(Point position) {
        GraphNode closestNode = null;
        for (GraphNode node : nodes.values()) {
            if(node.getPosition().distance(position) <= GraphNode.SIZE / 2) {
                closestNode = node;
            }
        }
        return closestNode;
    }

    public SortedMap<Integer, GraphNode> getNodes() {
        return nodes;
    }

    public SortedMap<Integer, GraphEdge> getEdges() {
        return edges;
    }

    public void selectNode(GraphNode clickedNode) {
        if (newEdgeStartNode == null) {
            newEdgeStartNode = clickedNode;
            newEdgeStartNode.setColor(GraphNode.COLOR_SELECTED);
        } else {
            GraphEdge edge = new GraphEdge(edges.size() + 1, newEdgeStartNode, clickedNode);
            edges.put(edge.getIndex(), edge);
            newEdgeStartNode.setColor(GraphNode.COLOR_NEW);
            newEdgeStartNode = null;
        }
    }
}
