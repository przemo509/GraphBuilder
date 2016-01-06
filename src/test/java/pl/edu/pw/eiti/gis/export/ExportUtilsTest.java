package pl.edu.pw.eiti.gis.export;

import org.junit.Test;
import pl.edu.pw.eiti.gis.model.Graph;
import pl.edu.pw.eiti.gis.model.GraphType;
import pl.edu.pw.eiti.gis.model.GraphVertex;

import java.awt.*;

import static org.junit.Assert.*;

public class ExportUtilsTest {

    private int[][] exportFullGraphMatrix(int verticesCount, GraphType graphType, MatrixTypeEnum matrixType) {
        Graph graph = new Graph(graphType);
        GraphVertex[] vertices = new GraphVertex[verticesCount];
        for (int i = 0; i < verticesCount; i++) {
            vertices[i] = graph.addVertex(new Point());
        }
        for (GraphVertex v1 : vertices) {
            for (GraphVertex v2 : vertices) {
                for (int i = 0; i < 3; i++) {
                    graph.tryToAddEdge(v1);
                    graph.tryToAddEdge(v2);
                }
            }
        }
        return ExportUtils.graphToMatrix(graph, matrixType);
    }

    private int[][] buildFullGraphExpectedMatrix(int verticesCount, GraphType graphType, MatrixTypeEnum matrixType) {
        int[][] matrix = new int[verticesCount][verticesCount];
        int edgeIndex;
        int previousEdgeIndex = 0;
        for (int i = 0; i < verticesCount; i++) {
            for (int j = 0; j < verticesCount; j++) {
                int count = expectedEdgesCount(i, j, graphType);

                if(count == 0) {
                    edgeIndex = 0;
                } else if(matrix[j][i] != 0) {
                    edgeIndex = matrix[j][i];
                } else {
                    int newEdgeIndex = 0;
                    for (int k = 1; k <= count; k++) {
                        previousEdgeIndex++;
                        newEdgeIndex += previousEdgeIndex;
                    }
                    edgeIndex = newEdgeIndex;
                }

                switch (matrixType) {
                    case NEIGHBOUR:
                        matrix[i][j] += count;
                        break;
                    case WEIGHT:
                        matrix[i][j] += edgeIndex;
                        break;
                    case FULL_INCIDENCE:
                        break;
                }
            }
        }
        return matrix;
    }

    private int expectedEdgesCount(int v1, int v2, GraphType graphType) {
        if(v2 == v1) {
            return graphType.isMulti() ? 1 : 0;
        } else if(v2 < v1) { //
            return !graphType.isDirected() ? (graphType.isMulti() ? 3 : 1) : 0;
        } else {
            return (graphType.isMulti() ? 3 : 1);
        }
    }

    private void testFullGraphMatrix(GraphType graphType, MatrixTypeEnum matrixType) {
        for(int verticesCount = 2; verticesCount < 30; ++verticesCount) {
            int[][] actual = exportFullGraphMatrix(verticesCount, graphType, matrixType);
            int[][] expected = buildFullGraphExpectedMatrix(verticesCount, graphType, matrixType);
            assertArrayEquals(buildMessage(actual, expected), expected, actual);
        }
    }

    private String buildMessage(int[][] actual, int[][] expected) {
        return "actual:\n" + matrixToString(actual) + "\n" +
                "expected:\n" + matrixToString(expected) + "\n";
    }

    private String matrixToString(int[][] matrix) {
        String s = "";
        String lineSeparator = "";
        for (int[] matrixRow : matrix) {
            s += lineSeparator;
            String cellSeparator = "";
            for (int i = 0; i < matrix[0].length; i++) {
                s += cellSeparator + matrixRow[i];
                cellSeparator = " ";
            }
            lineSeparator = "\n";
        }
        return s;
    }

    @Test
    public void testSimpleNotDirectedNotWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(false, false, false), MatrixTypeEnum.NEIGHBOUR);
    }

    @Test
    public void testSimpleDirectedNotWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(false, true, false), MatrixTypeEnum.NEIGHBOUR);
    }

    @Test
    public void testMultiNotDirectedNotWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(true, false, false), MatrixTypeEnum.NEIGHBOUR);
    }

    @Test
    public void testMultiDirectedNotWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(true, true, false), MatrixTypeEnum.NEIGHBOUR);
    }

    @Test
    public void testSimpleNotDirectedWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(false, false, true), MatrixTypeEnum.NEIGHBOUR);
    }

    @Test
    public void testSimpleDirectedWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(false, true, true), MatrixTypeEnum.NEIGHBOUR);
    }

    @Test
    public void testMultiNotDirectedWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(true, false, true), MatrixTypeEnum.NEIGHBOUR);
    }

    @Test
    public void testMultiDirectedWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(true, true, true), MatrixTypeEnum.NEIGHBOUR);
    }

    @Test
    public void testSimpleNotDirectedWeightedGraphToWeightMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(false, false, true), MatrixTypeEnum.WEIGHT);
    }

    @Test
    public void testSimpleDirectedWeightedGraphToWeightMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(false, true, true), MatrixTypeEnum.WEIGHT);
    }

    @Test
    public void testMultiNotDirectedWeightedGraphToWeightMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(true, false, true), MatrixTypeEnum.WEIGHT);
    }

    @Test
    public void testMultiDirectedWeightedGraphToWeightMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(true, true, true), MatrixTypeEnum.WEIGHT);
    }
}