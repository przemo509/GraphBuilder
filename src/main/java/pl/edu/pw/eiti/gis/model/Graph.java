package pl.edu.pw.eiti.gis.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Graph {
    private static final Logger logger = Logger.getLogger(Graph.class.getName());
    private String lastError = "";

    private final GraphType type;

    private SortedMap<Integer, GraphVertex> vertices = new TreeMap<>();
    private SortedMap<Integer, GraphEdge> edges = new TreeMap<>();
    private SortedMap<GraphEdgeVerticesIndexes, List<GraphEdge>> adjacency = new TreeMap<>();

    private GraphVertex selectedVertex;

    public Graph(GraphType type) {
        this.type = type;
    }

    public GraphType getType() {
        return type;
    }

    public GraphVertex addVertex(Point position) {
        GraphVertex vertex = new GraphVertex(vertices.size() + 1, position);
        vertices.put(vertex.getIndex(), vertex);
        logger.log(Level.FINE, "Vertex {0} added", vertex.getIndex());
        return vertex;
    }

    public GraphVertex getVertex(Point position) {
        GraphVertex closestVertex = null;
        for (GraphVertex vertex : vertices.values()) {
            if (vertex.getPosition().distance(position) <= GraphVertex.SIZE / 2) {
                closestVertex = vertex;
            }
        }
        return closestVertex;
    }

    public GraphEdge getEdge(Point position) {
        GraphEdge closestEdge = null;
        for (List<GraphEdge> graphEdges : adjacency.values()) {
            for (GraphEdge edge : graphEdges) {
                if (edge.getLabelPosition().distance(position) <= GraphEdge.SIZE / 2) {
                    closestEdge = edge;
                }
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
        tryToAddEdge(clickedVertex, edges.size() + 1, 0.5, false);
    }

    private void tryToAddEdge(GraphVertex clickedVertex, int edgeWeight, double labelPositionFactor, boolean flipEdgeLabelSide) {
        if (selectedVertex == null) {
            selectVertex(clickedVertex);
        } else {
            GraphEdge edge = new GraphEdge(edges.size() + 1, edgeWeight, labelPositionFactor, flipEdgeLabelSide, selectedVertex, clickedVertex);
            if (type.isVRep()) {
                edge.refreshVRepWeight();
            }
            if (type.isVRep() && planarIsDamagedBy(edge)) {
                setLastError("Graf dla V-Rep powinien być planarny");
            } else {
                addEdge(edge);
            }
            deselectVertex();
        }
    }

    public void selectVertex(GraphVertex clickedVertex) {
        selectedVertex = clickedVertex;
        selectedVertex.setHighlighted(true);
        logger.log(Level.FINE, "Vertex {0} selected", selectedVertex.getIndex());
    }

    public void deselectVertex() {
        if (selectedVertex != null) {
            logger.log(Level.FINE, "Vertex {0} deselected", selectedVertex.getIndex());
            selectedVertex.setHighlighted(false);
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

        if (type.isMulti()) {
            if (edgesList == null) {
                edgesList = new ArrayList<>();
                edgesList.add(edge);
                edges.put(edgeIndex, edge);

                adjacency.put(verticesIndexesIndexes, edgesList);
            } else if (startVertexIndex == endVertexIndex) {
                logger.log(Level.WARNING, "self edge in multigraph for vertex {0} already exist", startVertexIndex);
                setLastError("Obsługiwana jest co najwyżej jedna pętla własna wierzchołka");
            } else if (edgesList.size() < 3) {
                edgesList.add(edge);
                edges.put(edgeIndex, edge);
            } else {
                logger.log(Level.WARNING, "between vertices {0} and {1} exist already {2} edges, cannot add more in multigraph", new Object[]{startVertexIndex, endVertexIndex, edgesList.size()});
                setLastError("Obsługiwane są co najwyżej trzy krawędzie między wierzchołkami");
            }
        } else {
            if (edgesList == null) {
                if (startVertexIndex == endVertexIndex) {
                    logger.log(Level.WARNING, "self edge in simple graph not allowed for vertex {0}", startVertexIndex);
                    setLastError("W grafie prostym pętle własne nie są dozwolone");
                } else {
                    edgesList = new ArrayList<>();
                    edgesList.add(edge);
                    edges.put(edgeIndex, edge);

                    adjacency.put(verticesIndexesIndexes, edgesList);
                }
            } else {
                logger.log(Level.WARNING, "between vertices {0} and {1} exists already edge {2}, cannot add more in simple graph", new Object[]{startVertexIndex, endVertexIndex, edgesList.get(0).getIndex()});
                setLastError("W grafie prostym może istnieć co najwyżej jedna krawędź między wierzchołkami");
            }
        }
    }

    public void moveSelectedVertex(Point position) {
        if (selectedVertex != null) {
            selectedVertex.getPosition().setLocation(position);
            edges.values().stream()
                    .filter(edge -> edge.touches(selectedVertex))
                    .forEach(GraphEdge::refreshVRepWeight);
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

    public void removeVertex(GraphVertex vertexToRemove) {
        SortedMap<Integer, GraphVertex> oldVertices = vertices;
        SortedMap<Integer, GraphEdge> oldEdges = edges;

        vertices = new TreeMap<>();
        edges = new TreeMap<>();
        adjacency = new TreeMap<>();

        oldVertices.forEach((index, vertex) -> {
            if (!vertex.equals(vertexToRemove)) {
                addVertex(vertex.getPosition());
            }
        });
        oldEdges.forEach((index, edge) -> {
            int edgeStart = edge.getStartVertex().getIndex();
            int edgeEnd = edge.getEndVertex().getIndex();
            if (edgeStart == vertexToRemove.getIndex() || edgeEnd == vertexToRemove.getIndex()) {
                // nie dodajemy krawędzi
            } else {
                GraphVertex vStart;
                GraphVertex vEnd;
                if (edgeStart < vertexToRemove.getIndex()) {
                    vStart = vertices.get(edgeStart);
                } else {
                    vStart = vertices.get(edgeStart - 1);
                }
                if (edgeEnd < vertexToRemove.getIndex()) {
                    vEnd = vertices.get(edgeEnd);
                } else {
                    vEnd = vertices.get(edgeEnd - 1);
                }
                tryToAddEdge(vStart, edge.getWeight(), edge.getLabelPositionFactor(), edge.getFlipEdgeLabelSide());
                tryToAddEdge(vEnd, edge.getWeight(), edge.getLabelPositionFactor(), edge.getFlipEdgeLabelSide());
            }
        });
    }

    public void removeEdge(GraphEdge edgeToRemove) {
        SortedMap<Integer, GraphEdge> oldEdges = edges;

        edges = new TreeMap<>();
        adjacency = new TreeMap<>();

        oldEdges.forEach((index, edge) -> {
            if (edge.getIndex() != edgeToRemove.getIndex()) {
                tryToAddEdge(edge.getStartVertex(), edge.getWeight(), edge.getLabelPositionFactor(), edge.getFlipEdgeLabelSide());
                tryToAddEdge(edge.getEndVertex(), edge.getWeight(), edge.getLabelPositionFactor(), edge.getFlipEdgeLabelSide());
            }
        });
    }

    public boolean planarIsDamagedBy(GraphVertex vertex) {
        return edges.values().stream()
                .filter(edge -> edge.touches(vertex))
                .anyMatch(this::planarIsDamagedBy);
    }

    private boolean planarIsDamagedBy(GraphEdge edge) {
        return edges.values().stream()
                .anyMatch(edge::intersects);
    }
}
