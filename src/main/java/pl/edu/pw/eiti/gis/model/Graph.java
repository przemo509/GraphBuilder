package pl.edu.pw.eiti.gis.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Graph {
    private static final Logger logger = LogManager.getLogger();
    private String lastError = "";

    private final GraphType type;

    private SortedMap<Integer, GraphVertex> vertices = new TreeMap<>();
    private SortedMap<Integer, GraphEdge> edges = new TreeMap<>();
    private SortedMap<GraphEdgeVerticesIndexes, List<GraphEdge>> adjacency = new TreeMap<>();

    private GraphVertex selectedVertex;

    public Graph() {
        this(new GraphType());
    }

    public Graph(GraphType type) {
        this.type = type;
    }

    public GraphType getType() {
        return type;
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
            if(edge.getLabelPosition().distance(position) <= GraphEdge.SIZE / 2) {
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

        GraphEdgeVerticesIndexes verticesIndexesIndexes = new GraphEdgeVerticesIndexes(startVertexIndex, endVertexIndex);
        List<GraphEdge> edgesList = adjacency.get(verticesIndexesIndexes);

        if(type.isMulti()) {
            if (edgesList == null) {
                edgesList = new ArrayList<>();
                edgesList.add(edge);
                edges.put(edgeIndex, edge);

                adjacency.put(verticesIndexesIndexes, edgesList);
            } else if (startVertexIndex == endVertexIndex) {
                logger.warn("self edge in multigraph for vertex {} already exist", startVertexIndex);
                setLastError("Obsługiwana jest co najwyżej jedna pętla własna wierzchołka");
            } else if (edgesList.size() < 3) {
                edgesList.add(edge);
                edges.put(edgeIndex, edge);
            } else {
                logger.warn("between vertices {} and {} exist already {} edges, cannot add more in multigraph", startVertexIndex, endVertexIndex, edgesList.size());
                setLastError("Obsługiwane są co najwyżej trzy krawędzie między wierzchołkami");
            }
        } else {
            if (edgesList == null) {
                if (startVertexIndex == endVertexIndex) {
                    logger.warn("self edge in simple graph not allowed for vertex {}", startVertexIndex);
                    setLastError("W grafie prostym pętle własne nie są dozwolone");
                } else {
                    edgesList = new ArrayList<>();
                    edgesList.add(edge);
                    edges.put(edgeIndex, edge);

                    adjacency.put(verticesIndexesIndexes, edgesList);
                }
            } else {
                logger.warn("between vertices {} and {} exists already edge {}, cannot add more in simple graph", startVertexIndex, endVertexIndex, edgesList.get(0).getIndex());
                setLastError("W grafie prostym może istnieć co najwyżej jedna krawędź między wierzchołkami");
            }
        }
    }

    public void moveSelectedVertex(Point position) {
        if(selectedVertex != null) {
            selectedVertex.getPosition().setLocation(position);
        }
    }

    public String consumeLastError() {
        String tmp = lastError;
        lastError = "";
        return tmp;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public SortedMap<Integer, GraphEdge> getEdges() {
        return edges;
    }
}
