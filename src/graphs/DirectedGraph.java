package graphs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import structures.Bijection;

/**
 * @param <V> Vertex type
 * @author Jhakon Pappoe
 * @version 0.1
 */
public class DirectedGraph<V> implements IGraph<V> {

    private Stack<Integer> stack = new Stack<>();
    private Bijection<V, Integer> map = new Bijection<>();
    private int[][] matrix;
    private int edgeSize;
    private int vertexSize;

    /**
     * Creates a new graph with space initially for 10 vertices.
     */
    public DirectedGraph() {
        int rows = 10;
        int columns = 10;
        matrix = new int[rows][columns];
        emptyWeights();
    }

    private void emptyWeights() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] = -1;
            }
        }
    }

//    /**
//     * Creates a new graph with enough space for the requested number of vertices.
//     *
//     * @param initialSize the initial number of rows/columns in adjacency matrix
//
//    public DirectedGraph(int initialSize) {
//        matrix = new int[initialSize][initialSize];
//    }

    private void resize() {
        int[][] oldMatrix = matrix;
        int newSize = matrix.length * 2;
        matrix = new int[newSize][newSize];
        emptyWeights();
        for (int i = 0; i < oldMatrix.length; i++) {
            for (int j = 0; j < oldMatrix.length; j++) {
                matrix[i][j] = oldMatrix[i][j];
            }
        }
    }

    /**
     * Adds a new vertex to the graph. If the vertex already exists, then no change is made to the
     * graph.
     *
     * @param vertex the new vertex
     * @return true if the vertex was added, otherwise false
     */
    @Override
    public boolean addVertex(V vertex) {
        if (this.containsVertex(vertex)) {
            return false;
        }
        if (vertexSize() == matrix.length) {
            resize();
        }
        map.add(vertex, vertexSize());
        vertexSize++;
        return true;
    }

    /**
     * Adds a new edge to the graph. If the edge already exists, then no change is made to the
     * graph.
     *
     * Edges are considered to be directed.
     *
     * @param source the source vertex of the edge
     * @param destination the destination vertex of the edge
     * @param weight the edge weight, throws an IllegalArgumentException if the weight is negative
     * @return true if the edge was added, otherwise false
     */
    @Override
    public boolean addEdge(V source, V destination, int weight) throws IllegalArgumentException {
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }

        if (containsEdge(source, destination)) {
            return false;
        }

        if (map.getValue(source) != null && map.getValue(destination) != null) {
            matrix[map.getValue(source)][map.getValue(destination)] = weight;
            edgeSize++;
            return true;
        }
        return false;
    }

    /**
     * Returns the number of vertices in the graph.
     *
     * @return the vertex count.
     */
    @Override
    public int vertexSize() {
        return vertexSize;
    }

    /**
     * Returns the number of edges in the graph.
     *
     * @return the edge count
     */
    @Override
    public int edgeSize() {
        return edgeSize;
    }

    /**
     * Reports whether a vertex is in the graph or not.
     *
     * @param vertex a vertex to search for
     * @return true if the vertex is in the graph, or false otherwise
     */
    @Override
    public boolean containsVertex(V vertex) {
        return map.containsKey(vertex);
    }

    /**
     * Reports whether an edge is in the graph or not.
     *
     * @param source the source vertex of the edge
     * @param destination the destination vertex of the edge
     * @return true if edge is in the graph, or false otherwise
     */
    @Override
    public boolean containsEdge(V source, V destination) {
        if (map.getValue(source) == null || map.getValue(destination) == null) {
            return false;
        }
        return (matrix[map.getValue(source)][map.getValue(destination)] != -1);
    }

    /**
     * Returns the edge weight of an edge in the graph.
     *
     * @param source the source vertex of the edge
     * @param destination the destination vertex of the edge
     * @return the edge weight, or -1 if the edge weight is not found
     */
    @Override
    public int edgeWeight(V source, V destination) {
        return matrix[map.getValue(source)][map.getValue(destination)];
    }

    /**
     * Returns a set with all vertices in the graph.
     *
     * @return a vertex set
     */
    @Override
    public Set<V> vertices() {
        Set<V> newSet = new HashSet<>();
        for (int i = 0; i < vertexSize; i++) {
            newSet.add(map.getKey(i));
        }
        return newSet;
    }

    /**
     * Returns a set with all edges in the graph.
     *
     * @return an edge set
     */
    @Override
    public Set<Edge<V>> edges() {
        Set<Edge<V>> set = new HashSet<>();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] != -1) {
                    Edge<V> newEdge = new Edge<>(map.getKey(i), map.getKey(j), matrix[i][j]);
                    set.add(newEdge);
                }
            }
        }
        return set;
    }

    /**
     * Removes a vertex from the graph.
     *
     * @param vertex the vertex to search for and remove
     * @return true if the vertex was found and removed, otherwise false
     */
    @Override
    public boolean removeVertex(V vertex) {
        if (containsVertex(vertex)) {
            stack.push(map.getValue(vertex));
            vertexSize--;
        }
        return map.removeKey(vertex);
    }

    /**
     * Removes a vertex from the graph.
     *
     * @param source the source vertex of the edge to search for and remove
     * @param destination the destination vertex of the edge to search for and remove
     * @return true if the edge was found and removed, otherwise false
     */
    @Override
    public boolean removeEdge(V source, V destination) {
        if (containsEdge(source, destination)) {
            matrix[map.getValue(source)][map.getValue(destination)] = -1;
            edgeSize--;
            return true;
        }
        return false;
    }

    /**
     * Removes all vertices and edges from the graph.
     */
    @Override
    public void clear() {
        map = new Bijection<>();
        stack.clear();
        emptyWeights();
        vertexSize = 0;
        edgeSize = 0;
    }

    /**
     * toString method callable by the
     * stack, map, and matrix
     * @return prints the data within stack,
     * map, matrix, edgeSize, and vertexSize
     */
    @Override
    public String toString() {
        return "DirectedGraph{" +
            "stack=" + stack +
            ", map=" + map +
            ", matrix=" + Arrays.toString(matrix) +
            ", edgeSize=" + edgeSize +
            ", vertexSize=" + vertexSize +
            '}';
    }
}