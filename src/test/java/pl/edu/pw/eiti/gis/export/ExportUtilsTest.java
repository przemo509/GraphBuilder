package pl.edu.pw.eiti.gis.export;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.edu.pw.eiti.gis.model.Graph;
import pl.edu.pw.eiti.gis.model.GraphType;
import pl.edu.pw.eiti.gis.model.GraphVertex;

import java.awt.*;
import java.util.logging.LogManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class ExportUtilsTest {

    @BeforeClass
    public static void setUp() throws Exception {
        LogManager.getLogManager().readConfiguration(ExportUtilsTest.class.getResourceAsStream("/logging.properties"));
    }

    private int[][] exportFullGraphMatrix(int verticesCount, GraphType graphType, MatrixTypeEnum matrixType) {
        Graph graph = buildFullGraph(verticesCount, graphType);
        return ExportUtils.graphToMatrix(graph, matrixType);
    }

    private Graph buildFullGraph(int verticesCount, GraphType graphType) {
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
        return graph;
    }

    private int[][] buildFullGraphExpectedMatrix(int verticesCount, GraphType graphType, MatrixTypeEnum matrixType) {
        int[][] matrix = new int[verticesCount][verticesCount];
        int edgeIndex;
        int previousEdgeIndex = 0;
        for (int i = 0; i < verticesCount; i++) {
            for (int j = 0; j < verticesCount; j++) {
                int count = expectedEdgesCount(i, j, graphType);

                if (count == 0) {
                    edgeIndex = 0;
                } else if (matrix[j][i] != 0) {
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
        if (v2 == v1) {
            return graphType.isMulti() ? 1 : 0;
        } else if (v2 < v1) { //
            return !graphType.isDirected() ? (graphType.isMulti() ? 3 : 1) : 0;
        } else {
            return (graphType.isMulti() ? 3 : 1);
        }
    }

    private void testFullGraphMatrix(GraphType graphType, MatrixTypeEnum matrixType) {
        for (int verticesCount = 2; verticesCount < 30; ++verticesCount) {
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

    /**
     * NEIGHBOUR MATRIX
     */

    @Test
    public void testSimpleNotDirectedNotWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(false, false, false, false), MatrixTypeEnum.NEIGHBOUR);
    }

    @Test
    public void testSimpleDirectedNotWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(false, true, false, false), MatrixTypeEnum.NEIGHBOUR);
    }

    @Test
    public void testMultiNotDirectedNotWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(true, false, false, false), MatrixTypeEnum.NEIGHBOUR);
    }

    @Test
    public void testMultiDirectedNotWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(true, true, false, false), MatrixTypeEnum.NEIGHBOUR);
    }

    @Test
    public void testSimpleNotDirectedWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(false, false, true, false), MatrixTypeEnum.NEIGHBOUR);
    }

    @Test
    public void testSimpleDirectedWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(false, true, true, false), MatrixTypeEnum.NEIGHBOUR);
    }

    @Test
    public void testMultiNotDirectedWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(true, false, true, false), MatrixTypeEnum.NEIGHBOUR);
    }

    @Test
    public void testMultiDirectedWeightedGraphToNeighbourMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(true, true, true, false), MatrixTypeEnum.NEIGHBOUR);
    }

    /**
     * WEIGHT MATRIX
     */

    @Test
    public void testSimpleNotDirectedWeightedGraphToWeightMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(false, false, true, false), MatrixTypeEnum.WEIGHT);
    }

    @Test
    public void testSimpleDirectedWeightedGraphToWeightMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(false, true, true, false), MatrixTypeEnum.WEIGHT);
    }

    @Test
    public void testMultiNotDirectedWeightedGraphToWeightMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(true, false, true, false), MatrixTypeEnum.WEIGHT);
    }

    @Test
    public void testMultiDirectedWeightedGraphToWeightMatrix() throws Exception {
        testFullGraphMatrix(new GraphType(true, true, true, false), MatrixTypeEnum.WEIGHT);
    }

    /**
     * FULL INCIDENCE MATRIX
     */

    @Test
    public void testSimpleNotDirectedNotWeightedGraphToFullIncidenceMatrix2() throws Exception {
        int[][] actual = exportFullGraphMatrix(2, new GraphType(false, false, false, false), MatrixTypeEnum.FULL_INCIDENCE);
        int[][] expected = {
                {1},
                {1}};
        assertArrayEquals(buildMessage(actual, expected), expected, actual);
    }

    @Test
    public void testSimpleNotDirectedNotWeightedGraphToFullIncidenceMatrix3() throws Exception {
        int[][] actual = exportFullGraphMatrix(3, new GraphType(false, false, false, false), MatrixTypeEnum.FULL_INCIDENCE);
        int[][] expected = {
                {1, 1, 0},
                {1, 0, 1},
                {0, 1, 1}};
        assertArrayEquals(buildMessage(actual, expected), expected, actual);
    }

    @Test
    public void testSimpleDirectedNotWeightedGraphToFullIncidenceMatrix2() throws Exception {
        int[][] actual = exportFullGraphMatrix(2, new GraphType(false, true, false, false), MatrixTypeEnum.FULL_INCIDENCE);
        int[][] expected = {
                {-1},
                {1}};
        assertArrayEquals(buildMessage(actual, expected), expected, actual);
    }

    @Test
    public void testSimpleDirectedNotWeightedGraphToFullIncidenceMatrix3() throws Exception {
        int[][] actual = exportFullGraphMatrix(3, new GraphType(false, true, false, false), MatrixTypeEnum.FULL_INCIDENCE);
        int[][] expected = {
                {-1, -1, 0},
                {1, 0, -1},
                {0, 1, 1}};
        assertArrayEquals(buildMessage(actual, expected), expected, actual);
    }

    @Test
    public void testMultiNotDirectedNotWeightedGraphToFullIncidenceMatrix2() throws Exception {
        int[][] actual = exportFullGraphMatrix(2, new GraphType(true, false, false, false), MatrixTypeEnum.FULL_INCIDENCE);
        int[][] expected = {
                {2, 1, 1, 1, 0},
                {0, 1, 1, 1, 2}};
        assertArrayEquals(buildMessage(actual, expected), expected, actual);
    }

    @Test
    public void testMultiNotDirectedNotWeightedGraphToFullIncidenceMatrix3() throws Exception {
        int[][] actual = exportFullGraphMatrix(3, new GraphType(true, false, false, false), MatrixTypeEnum.FULL_INCIDENCE);
        int[][] expected = {
                {2, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 0, 0, 0, 2, 1, 1, 1, 0},
                {0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 2}};
        assertArrayEquals(buildMessage(actual, expected), expected, actual);
    }

    @Test
    public void testMultiDirectedNotWeightedGraphToFullIncidenceMatrix2() throws Exception {
        int[][] actual = exportFullGraphMatrix(2, new GraphType(true, true, false, false), MatrixTypeEnum.FULL_INCIDENCE);
        int[][] expected = {
                {2, -1, -1, -1, 0},
                {0, 1, 1, 1, 2}};
        assertArrayEquals(buildMessage(actual, expected), expected, actual);
    }

    @Test
    public void testMultiDirectedNotWeightedGraphToFullIncidenceMatrix3() throws Exception {
        int[][] actual = exportFullGraphMatrix(3, new GraphType(true, true, false, false), MatrixTypeEnum.FULL_INCIDENCE);
        int[][] expected = {
                {2, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 0, 0, 0, 2, -1, -1, -1, 0},
                {0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 2}};
        assertArrayEquals(buildMessage(actual, expected), expected, actual);
    }

    /**
     * V-Rep
     */
    @Test
    public void testVRepExport() {
        String actual = ExportUtils.graphToVRepText(buildFullGraph(3, new GraphType(false, false, true, true)));
        String expected =
                "1\t0\t0\n" +
                "2\t0\t0\n" +
                "3\t0\t0\n" +
                "#\n" +
                "1\t1\t2\n" +
                "2\t1\t3\n" +
                "3\t2\t3\n";
        assertEquals(actual, expected);
    }
}