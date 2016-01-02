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

    private SortedMap<Integer, GraphVertex> vertices = new TreeMap<>();
    private SortedMap<Integer, GraphEdge> edges = new TreeMap<>();
    private SortedMap<GraphEdgeVerticesIndexes, List<GraphEdge>> adjacency = new TreeMap<>();

    private GraphVertex selectedVertex;

    public Graph() {
        this(false, false, false);
    }

    public Graph(boolean multi, boolean directed, boolean weighted) {
        this.multi = multi;
        this.directed = directed;
        this.weighted = weighted;
    }

    public GraphVertex addVertex(Point position) {
        GraphVertex vertex = new GraphVertex(vertices.size() + 1, position, GraphVertex.COLOR_NEW);
        vertices.put(vertex.getIndex(), vertex);
        logger.debug("Vertex {} added", vertex.getIndex());
        return vertex;
    }

    public GraphVertex getVertex(Point position) {
        GraphVertex closestVertex = null;
        for (GraphVertex vertex : vertices.values()) {
            if(vertex.getPosition().distance(position) <= GraphVertex.SIZE / 2) {
                closestVertex = vertex;
            }
        }
        return closestVertex;
    }

    public GraphEdge getEdge(Point position) {
        GraphEdge closestEdge = null;
        for (GraphEdge edge : edges.values()) {
            if(edge.getLabelPosition().distance(position) <= GraphVertex.SIZE / 2) {
                closestEdge = edge;
            }
        }
        return closestEdge;
    }

    public SortedMap<Integer, GraphVertex> getVertices() {
        return vertices;
    }

    public SortedMap<GraphEdgeVerticesIndexes, List<GraphEdge>> getAdjacency() {
        return adjacency;
    }

    public void tryToAddEdge(GraphVertex clickedVertex) {
        if (selectedVertex == null) {
            selectVertex(clickedVertex);
        } else {
            GraphEdge edge = new GraphEdge(edges.size() + 1, selectedVertex, clickedVertex);
            addEdge(edge);
            deselectVertex();
        }
    }

    public void selectVertex(GraphVertex clickedVertex) {
        selectedVertex = clickedVertex;
        selectedVertex.setColor(GraphVertex.COLOR_SELECTED);
        logger.debug("Vertex {} selected", selectedVertex.getIndex());
    }

    public void deselectVertex() {
        if(selectedVertex != null) {
            logger.debug("Vertex {} deselected", selectedVertex.getIndex());
            selectedVertex.setColor(GraphVertex.COLOR_NEW);
            selectedVertex = null;
        }
    }

    public GraphVertex getSelectedVertex() {
        return selectedVertex;
    }

    private void addEdge(GraphEdge edge) {

        int edgeIndex = edge.getIndex();
        int startVertexIndex = edge.getStartVertex().getIndex();
        int endVertexIndex = edge.getEndVertex().getIndex();
        logger.debug("trying to add new edge {} from vertex {} to vertex {}", edgeIndex, startVertexIndex, endVertexIndex);

        GraphEdgeVerticesIndexes verticesIndexesIndexes = new GraphEdgeVerticesIndexes(startVertexIndex, endVertexIndex);
        List<GraphEdge> edgesList = adjacency.get(verticesIndexesIndexes);

        if(edgesList == null) {
            logger.debug("no edges between vertices {} and {} exist", startVertexIndex, endVertexIndex);
            edgesList = new ArrayList<>();
            edgesList.add(edge);
            edges.put(edgeIndex, edge);
            logger.debug("added new edge {} from vertex {} to vertex {}", edgeIndex, startVertexIndex, endVertexIndex);

            adjacency.put(verticesIndexesIndexes, edgesList);
        } else if(startVertexIndex == endVertexIndex) {
            logger.warn("self edge for vertex {} already exist", startVertexIndex);
        } else if(edgesList.size() < 3) {
            logger.debug("between vertices {} and {} exist {} edges", startVertexIndex, endVertexIndex, edgesList.size());
            edgesList.add(edge);
            edges.put(edgeIndex, edge);
            logger.debug("added new edge {} from vertex {} to vertex {}", edgeIndex, startVertexIndex, endVertexIndex);
        } else {
            logger.warn("between vertices {} and {} exist already {} edges, cannot add more", startVertexIndex, endVertexIndex, edgesList.size());
        }
    }

    public void moveSelectedVertex(Point position) {
        if(selectedVertex != null) {
            selectedVertex.getPosition().setLocation(position);
        }
    }
}
