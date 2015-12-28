package pl.edu.pw.eiti.gis.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Graph {
    private static final Logger logger = LogManager.getLogger();

    private final boolean multi;
    private final boolean directed;
    private final boolean weighted;

    private SortedMap<Integer, GraphNode> nodes = new TreeMap<>();
    private SortedMap<Integer, GraphEdge> edges = new TreeMap<>();
    private SortedMap<GraphEdgeNodesIndexes, List<GraphEdge>> adjacency = new TreeMap<>();

    private GraphNode selectedNode;

    public Graph() {
        this(false, false, false);
    }

    public Graph(boolean multi, boolean directed, boolean weighted) {
        this.multi = multi;
        this.directed = directed;
        this.weighted = weighted;
    }

    public GraphNode addNode(Point position) {
        GraphNode node = new GraphNode(nodes.size() + 1, position, GraphNode.COLOR_NEW);
        nodes.put(node.getIndex(), node);
        logger.debug("Node {} added", node.getIndex());
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

    public GraphEdge getEdge(Point position) {
        GraphEdge closestEdge = null;
        for (GraphEdge edge : edges.values()) {
            if(edge.getLabelPosition().distance(position) <= GraphNode.SIZE / 2) {
                closestEdge = edge;
            }
        }
        return closestEdge;
    }

    public SortedMap<Integer, GraphNode> getNodes() {
        return nodes;
    }

    public SortedMap<GraphEdgeNodesIndexes, List<GraphEdge>> getAdjacency() {
        return adjacency;
    }

    public void tryToAddEdge(GraphNode clickedNode) {
        if (selectedNode == null) {
            selectNode(clickedNode);
        } else {
            GraphEdge edge = new GraphEdge(edges.size() + 1, selectedNode, clickedNode);
            addEdge(edge);
            deselectNode();
        }
    }

    public void selectNode(GraphNode clickedNode) {
        selectedNode = clickedNode;
        selectedNode.setColor(GraphNode.COLOR_SELECTED);
        logger.debug("Node {} selected", selectedNode.getIndex());
    }

    public void deselectNode() {
        if(selectedNode != null) {
            logger.debug("Node {} deselected", selectedNode.getIndex());
            selectedNode.setColor(GraphNode.COLOR_NEW);
            selectedNode = null;
        }
    }

    public GraphNode getSelectedNode() {
        return selectedNode;
    }

    private void addEdge(GraphEdge edge) {

        int edgeIndex = edge.getIndex();
        int startNodeIndex = edge.getStartNode().getIndex();
        int endNodeIndex = edge.getEndNode().getIndex();
        logger.debug("trying to add new edge {} from node {} to node {}", edgeIndex, startNodeIndex, endNodeIndex);

        GraphEdgeNodesIndexes nodesIndexes = new GraphEdgeNodesIndexes(startNodeIndex, endNodeIndex);
        List<GraphEdge> edgesList = adjacency.get(nodesIndexes);

        if(edgesList == null) {
            logger.debug("no edges between nodes {} and {} exist", startNodeIndex, endNodeIndex);
            edgesList = new ArrayList<>();
            edgesList.add(edge);
            edges.put(edgeIndex, edge);
            logger.debug("added new edge {} from node {} to node {}", edgeIndex, startNodeIndex, endNodeIndex);

            adjacency.put(nodesIndexes, edgesList);
        } else if(startNodeIndex == endNodeIndex) {
            logger.warn("self edge for node {} already exist", startNodeIndex);
        } else if(edgesList.size() < 3) {
            logger.debug("between nodes {} and {} exist {} edges", startNodeIndex, endNodeIndex, edgesList.size());
            edgesList.add(edge);
            edges.put(edgeIndex, edge);
            logger.debug("added new edge {} from node {} to node {}", edgeIndex, startNodeIndex, endNodeIndex);
        } else {
            logger.warn("between nodes {} and {} exist already {} edges, cannot add more", startNodeIndex, endNodeIndex, edgesList.size());
        }
    }

    public void moveSelectedNode(Point position) {
        if(selectedNode != null) {
            selectedNode.getPosition().setLocation(position);
        }
    }
}
