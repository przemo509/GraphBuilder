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
                graph.tryToAddEdge(v1);
                graph.tryToAddEdge(v2);
            }
        }
        return ExportUtils.graphToMatrix(graph, matrixType);
    }

    private int[][] buildFullGraphExpectedMatrix(int verticesCount, GraphType graphType, MatrixTypeEnum matrixType) {
        int[][] matrix = new int[verticesCount][verticesCount];
        for (int i = 0; i < verticesCount; i++) {
            for (int j = 0; j < verticesCount; j++) {
                matrix[i][j] = matrixCellValue(i, j, graphType, matrixType);
            }
        }
        return matrix;
    }

    private int matrixCellValue(int v1, int v2, GraphType graphType, MatrixTypeEnum matrixType) {
        if(v2 == v1) {
            return graphType.isMulti() ? 1 : 0;
        } else if(v2 < v1) { //
            return !graphType.isDirected() ? 1 : 0;
        } else {
            return 1;
        }
    }

    @Test
    public void testSimpleNotDirectedNotWeightedGraphToNeighbourMatrix2() throws Exception {
        int verticesCount = 2;
        GraphType graphType = new GraphType(false, false, false);
        MatrixTypeEnum matrixType = MatrixTypeEnum.NEIGHBOUR;
        int[][] actual = exportFullGraphMatrix(verticesCount, graphType, matrixType);
        int[][] expected = buildFullGraphExpectedMatrix(verticesCount, graphType, matrixType);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSimpleNotDirectedNotWeightedGraphToNeighbourMatrix3() throws Exception {
        int verticesCount = 3;
        GraphType graphType = new GraphType(false, false, false);
        MatrixTypeEnum matrixType = MatrixTypeEnum.NEIGHBOUR;
        int[][] actual = exportFullGraphMatrix(verticesCount, graphType, matrixType);
        int[][] expected = buildFullGraphExpectedMatrix(verticesCount, graphType, matrixType);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSimpleNotDirectedNotWeightedGraphToNeighbourMatrix4() throws Exception {
        int verticesCount = 4;
        GraphType graphType = new GraphType(false, false, false);
        MatrixTypeEnum matrixType = MatrixTypeEnum.NEIGHBOUR;
        int[][] actual = exportFullGraphMatrix(verticesCount, graphType, matrixType);
        int[][] expected = buildFullGraphExpectedMatrix(verticesCount, graphType, matrixType);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSimpleDirectedNotWeightedGraphToNeighbourMatrix2() throws Exception {
        int verticesCount = 2;
        GraphType graphType = new GraphType(false, true, false);
        MatrixTypeEnum matrixType = MatrixTypeEnum.NEIGHBOUR;
        int[][] actual = exportFullGraphMatrix(verticesCount, graphType, matrixType);
        int[][] expected = buildFullGraphExpectedMatrix(verticesCount, graphType, matrixType);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSimpleDirectedNotWeightedGraphToNeighbourMatrix3() throws Exception {
        int verticesCount = 3;
        GraphType graphType = new GraphType(false, true, false);
        MatrixTypeEnum matrixType = MatrixTypeEnum.NEIGHBOUR;
        int[][] actual = exportFullGraphMatrix(verticesCount, graphType, matrixType);
        int[][] expected = buildFullGraphExpectedMatrix(verticesCount, graphType, matrixType);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSimpleDirectedNotWeightedGraphToNeighbourMatrix4() throws Exception {
        int verticesCount = 4;
        GraphType graphType = new GraphType(false, true, false);
        MatrixTypeEnum matrixType = MatrixTypeEnum.NEIGHBOUR;
        int[][] actual = exportFullGraphMatrix(verticesCount, graphType, matrixType);
        int[][] expected = buildFullGraphExpectedMatrix(verticesCount, graphType, matrixType);
        assertArrayEquals(expected, actual);
    }
}