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
        for (int i = 0; i < verticesCount; i++) {
            for (int j = 0; j < verticesCount; j++) {
                matrix[i][j] += matrixCellValue(i, j, graphType, matrixType);
            }
        }
        return matrix;
    }

    private int matrixCellValue(int v1, int v2, GraphType graphType, MatrixTypeEnum matrixType) {
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
            assertArrayEquals(expected, actual);
        }
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
}