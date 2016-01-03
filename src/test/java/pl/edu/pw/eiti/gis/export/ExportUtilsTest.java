package pl.edu.pw.eiti.gis.export;

import org.junit.Test;
import pl.edu.pw.eiti.gis.model.Graph;
import pl.edu.pw.eiti.gis.model.GraphType;
import pl.edu.pw.eiti.gis.model.GraphVertex;

import java.awt.*;

import static org.junit.Assert.*;

public class ExportUtilsTest {

    private int[][] buildFullGraphMatrix(int verticesCount, boolean multi, boolean directed, boolean weighted, MatrixTypeEnum matrixType) {
        Graph graph = new Graph(new GraphType(multi, directed, weighted));
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

    @Test
    public void testSimpleNotDirectedNotWeightedGraphToNeighbourMatrix2() throws Exception {
        int[][] actual = buildFullGraphMatrix(2, false, false, false, MatrixTypeEnum.NEIGHBOUR);
        int[][] expected = {
                {0, 1},
                {1, 0}};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSimpleNotDirectedNotWeightedGraphToNeighbourMatrix3() throws Exception {
        int[][] actual = buildFullGraphMatrix(3, false, false, false, MatrixTypeEnum.NEIGHBOUR);
        int[][] expected = {
                {0, 1, 1},
                {1, 0, 1},
                {1, 1, 0}};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSimpleNotDirectedNotWeightedGraphToNeighbourMatrix4() throws Exception {
        int[][] actual = buildFullGraphMatrix(4, false, false, false, MatrixTypeEnum.NEIGHBOUR);
        int[][] expected = {
                {0, 1, 1, 1},
                {1, 0, 1, 1},
                {1, 1, 0, 1},
                {1, 1, 1, 0}};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSimpleDirectedNotWeightedGraphToNeighbourMatrix2() throws Exception {
        int[][] actual = buildFullGraphMatrix(2, false, true, false, MatrixTypeEnum.NEIGHBOUR);
        int[][] expected = {
                {0, 1},
                {0, 0}};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSimpleDirectedNotWeightedGraphToNeighbourMatrix3() throws Exception {
        int[][] actual = buildFullGraphMatrix(3, false, true, false, MatrixTypeEnum.NEIGHBOUR);
        int[][] expected = {
                {0, 1, 1},
                {0, 0, 1},
                {0, 0, 0}};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSimpleDirectedNotWeightedGraphToNeighbourMatrix4() throws Exception {
        int[][] actual = buildFullGraphMatrix(4, false, true, false, MatrixTypeEnum.NEIGHBOUR);
        int[][] expected = {
                {0, 1, 1, 1},
                {0, 0, 1, 1},
                {0, 0, 0, 1},
                {0, 0, 0, 0}};
        assertArrayEquals(expected, actual);
    }
}