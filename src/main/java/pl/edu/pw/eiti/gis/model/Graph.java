package pl.edu.pw.eiti.gis.model;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Graph {
    private SortedMap<Integer, GraphNode> nodes = new TreeMap<>();
    private SortedMap<Integer, GraphEdge> edges = new TreeMap<>();
    private SortedMap<Integer, SortedMap<Integer, List<GraphEdge>>> adjacency = new TreeMap<>();

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

    public SortedMap<Integer, SortedMap<Integer, List<GraphEdge>>> getAdjacency() {
        return adjacency;
    }

    public void selectNode(GraphNode clickedNode) {
        if (newEdgeStartNode == null) {
            newEdgeStartNode = clickedNode;
            newEdgeStartNode.setColor(GraphNode.COLOR_SELECTED);
        } else {
            GraphEdge edge = new GraphEdge(edges.size() + 1, newEdgeStartNode, clickedNode);
            addEdge(edge);
            newEdgeStartNode.setColor(GraphNode.COLOR_NEW);
            newEdgeStartNode = null;
        }
    }

    private void addEdge(GraphEdge edge) {
        edges.put(edge.getIndex(), edge);

        int startNodeIndex = edge.getStartNode().getIndex();
        int endNodeIndex = edge.getEndNode().getIndex();
        SortedMap<Integer, List<GraphEdge>> adjacencyEnd = adjacency.get(startNodeIndex);

        if(adjacencyEnd == null) {
            ArrayList<GraphEdge> edgesList = new ArrayList<>();
            edgesList.add(edge);

            adjacencyEnd = new TreeMap<>();
            adjacencyEnd.put(endNodeIndex, edgesList);

            adjacency.put(startNodeIndex, adjacencyEnd);
        } else {
            List<GraphEdge> edgesList = adjacencyEnd.get(endNodeIndex);
            if(edgesList == null) {
                edgesList = new ArrayList<>();
                edgesList.add(edge);

                adjacencyEnd.put(endNodeIndex, edgesList);
            } else {
                edgesList.add(edge);
            }
        }
    }
}
