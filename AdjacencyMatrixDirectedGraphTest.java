package it.unicam.cs.asdl2223.pt2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

//TODO completare gli import necessari

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe di test per la classe AdjacencyMatrixDirectedGraph.
 * 
 * @author Luca Tesei (template)
 * 
 */
class AdjacencyMatrixDirectedGraphTest {

    @Test
    final void testAdjacencyMatrixUndirectedGraph() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertTrue(g.isEmpty());
    }

    @Test
    final void testNodeCount() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertEquals(0, g.nodeCount());
        g.addNode(new GraphNode<String>("s"));
        assertEquals(1, g.nodeCount());
        g.addNode(new GraphNode<String>("u"));
        assertEquals(2, g.nodeCount());
    }

    @Test
    final void testEdgeCount() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertEquals(0, g.edgeCount());
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        assertEquals(0, g.edgeCount());
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, true, 10.1);
        g.addEdge(esu);
        assertEquals(1, g.edgeCount());
        g.addEdge(esu);
        assertEquals(1, g.edgeCount());
        GraphNode<String> nx = new GraphNode<String>("x");
        g.addNode(nx);
        GraphEdge<String> esx = new GraphEdge<String>(ns, nx, true, 5.12);
        g.addEdge(esx);
        assertEquals(2, g.edgeCount());
    }

    @Test
    final void testSize() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertTrue(g.size() == 0);
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        assertTrue(g.size() == 1);
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        assertTrue(g.size() == 2);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, true);
        g.addEdge(esu);
        assertTrue(g.size() == 3);
        GraphNode<String> nx = new GraphNode<String>("x");
        g.addNode(nx);
        assertTrue(g.size() == 4);
        GraphEdge<String> esx = new GraphEdge<String>(ns, nx, true, 5.12);
        g.addEdge(esx);
        assertTrue(g.size() == 5);
        GraphEdge<String> eux = new GraphEdge<String>(nu, nx, true, 2.05);
        g.addEdge(eux);
        assertTrue(g.size() == 6);
        g.addEdge(new GraphEdge<String>(nu, nx, true, 5.05));
        assertTrue(g.size() == 6);
        g.clear();
        assertTrue(g.size() == 0);
    }

    @Test
    final void testIsEmpty() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertTrue(g.isEmpty());
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        assertFalse(g.isEmpty());
        g.clear();
        assertTrue(g.isEmpty());
    }

    @Test
    final void testClear() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertTrue(g.isEmpty());
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        assertFalse(g.isEmpty());
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, true, 10.1);
        g.addEdge(esu);
        assertFalse(g.isEmpty());
        g.clear();
        assertTrue(g.isEmpty());
    }

    @Test
    final void testIsDirected() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertTrue(g.isDirected());
    }

    @Test
    final void testAddNode() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertThrows(NullPointerException.class,
                () -> g.addNode((GraphNode<String>) null));
        assertThrows(NullPointerException.class,
                () -> g.addNode((String) null));
        GraphNode<String> ns = new GraphNode<String>("s");
        GraphNode<String> nsTest = new GraphNode<String>("s");
        assertTrue(g.getNode(ns) == null);
        g.addNode(ns);
        assertTrue(g.getNode(nsTest) != null);
        String lu = "u";
        String luTest = "u";
        assertTrue(g.getNode(luTest) == null);
        g.addNode(lu);
        assertTrue(g.getNode(luTest) != null);
    }

    @Test
    final void testRemoveNode() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertThrows(NullPointerException.class,
                () -> g.removeNode((GraphNode<String>) null));
        assertThrows(NullPointerException.class,
                () -> g.removeNode((String) null));
        assertThrows(IndexOutOfBoundsException.class, () -> g.removeNode(0));
        g.addNode("a");
        g.addNode("b");
        g.addNode(new GraphNode<String>("c"));
        g.addNode("d");
        g.addEdge("a", "b");
        g.addEdge("b", "c");
        g.addEdge("a", "a");
        g.addEdge("b", "d");
        g.addEdge("a", "d");
        g.addEdge("c", "d");
        assertTrue(g.getNodeIndexOf("a") == 0);
        assertTrue(g.getNodeIndexOf("b") == 1);
        assertTrue(g.getNodeIndexOf("c") == 2);
        assertTrue(g.getNodeIndexOf("d") == 3);
        assertTrue(g.nodeCount() == 4);
        assertThrows(IllegalArgumentException.class, () -> g.removeNode("e"));
        assertThrows(IndexOutOfBoundsException.class, () -> g.removeNode(4));
        g.removeNode("b");
        assertTrue(g.getNodeIndexOf("a") == 0);
        assertTrue(g.getNodeIndexOf("c") == 1);
        assertTrue(g.getNodeIndexOf("d") == 2);
        assertTrue(g.nodeCount() == 3);
        assertTrue(g.getNode("b") == null);
        // Controlla che la matrice sia ancora quadrata e non ci siano buchi
        assertDoesNotThrow(() -> {
            for (int i = 0; i < g.nodeCount(); i++)
                for (int j = 0; j < g.nodeCount(); j++)
                    g.getEdge(i, j);
        });
        assertTrue(g.getEdge("a", "a") != null);
        assertTrue(g.getEdge("a", "d") != null);
        assertTrue(g.getEdge("c", "d") != null);
        assertTrue(g.getEdge("c", "a") == null);
        assertTrue(g.getEdge("d", "d") == null);
        assertTrue(g.getEdge("c", "c") == null);
        g.removeNode(0);
        assertTrue(g.getNodeIndexOf("c") == 0);
        assertTrue(g.getNodeIndexOf("d") == 1);
        assertTrue(g.nodeCount() == 2);
        assertTrue(g.getNode("a") == null);
        // Controlla che la matrice sia ancora quadrata e non ci siano buchi
        assertDoesNotThrow(() -> {
            for (int i = 0; i < g.nodeCount(); i++)
                for (int j = 0; j < g.nodeCount(); j++)
                    g.getEdge(i, j);
        });
        assertTrue(g.getEdge("c", "d") != null);
        assertTrue(g.getEdge("d", "d") == null);
        assertTrue(g.getEdge("c", "c") == null);
    }

    @Test
    final void testGetNode() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertThrows(NullPointerException.class,
                () -> g.getNode((GraphNode<String>) null));
        assertThrows(NullPointerException.class,
                () -> g.getNode((String) null));
        GraphNode<String> ns = new GraphNode<String>("s");
        GraphNode<String> nsTest = new GraphNode<String>("s");
        assertTrue(g.getNode(nsTest) == null);
        g.addNode(ns);
        assertTrue(g.getNode(nsTest) != null);
        g.addNode("a");
        GraphNode<String> na = g.getNode("a");
        assertTrue(na != null);
        na.setColor(GraphNode.COLOR_BLACK);
        assertTrue(g.getNode("a").getColor() == GraphNode.COLOR_BLACK);
        assertFalse(g.addNode("a"));
        assertTrue(g.getNode(na).getColor() == GraphNode.COLOR_BLACK);
        assertTrue(g.getNode("b") == null);
    }

    @Test
    final void testGetNodeInt() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertThrows(IndexOutOfBoundsException.class, () -> g.getNode(0));
        GraphNode<String> ns = new GraphNode<String>("s");
        ns.setColor(1);
        g.addNode(ns);
        assertThrows(IndexOutOfBoundsException.class, () -> g.getNode(1));
        GraphNode<String> nsTest = new GraphNode<String>("s");
        assertTrue(nsTest.equals(g.getNode(0)));
        assertTrue(g.getNode(0).getColor() == 1);
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        assertThrows(IndexOutOfBoundsException.class, () -> g.getNode(2));
        GraphNode<String> nuTest = new GraphNode<String>("u");
        assertTrue(nuTest.equals(g.getNode(1)));
    }

    @Test
    final void testGetNodeIndexOf() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertThrows(NullPointerException.class,
                () -> g.getNodeIndexOf((GraphNode<String>) null));
        assertThrows(NullPointerException.class,
                () -> g.getNodeIndexOf((String) null));
        GraphNode<String> ns = new GraphNode<String>("s");
        ns.setColor(1);
        g.addNode(ns);
        assertTrue(g.getNodeIndexOf("s") == 0);
        assertThrows(IllegalArgumentException.class,
                () -> g.getNodeIndexOf("u"));
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        assertTrue(g.getNodeIndexOf("u") == 1);
        assertTrue(g.getNodeIndexOf("s") == 0);
        g.addNode("x");
        assertTrue(g.getNodeIndexOf("x") == 2);
        g.addEdge("s", "x");
        assertTrue(g.getNodeIndexOf("s") == 0);
        g.removeNode(nu);
        assertThrows(IllegalArgumentException.class,
                () -> g.getNodeIndexOf("u"));
        assertTrue(g.getNodeIndexOf("s") == 0);
        assertFalse(g.addNode("s"));
        assertFalse(g.addNode("x"));
        assertTrue(g.getNodeIndexOf("x") == 1);
        g.removeNode("s");
        assertThrows(IllegalArgumentException.class,
                () -> g.getNodeIndexOf("s"));
        assertTrue(g.getNodeIndexOf("x") == 0);
    }

    @Test
    final void testGetNodes() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        Set<GraphNode<String>> nodes = g.getNodes();
        assertTrue(nodes.isEmpty());
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        nodes = g.getNodes();
        Set<GraphNode<String>> testNodes = new HashSet<GraphNode<String>>();
        GraphNode<String> nsTest = new GraphNode<String>("s");
        GraphNode<String> nuTest = new GraphNode<String>("u");
        testNodes.add(nuTest);
        testNodes.add(nsTest);
        assertTrue(nodes.equals(testNodes));
        GraphNode<String> nuTestBis = new GraphNode<String>("u");
        g.addNode(nuTestBis);
        nodes = g.getNodes();
        assertTrue(nodes.equals(testNodes));
    }

    @Test
    final void testAddEdge() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertThrows(NullPointerException.class, () -> g.addEdge(null));
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        GraphNode<String> nu = new GraphNode<String>("u");
        assertThrows(IllegalArgumentException.class,
                () -> g.addEdge(new GraphEdge<String>(ns, nu, true)));
        assertThrows(IllegalArgumentException.class,
                () -> g.addEdge(new GraphEdge<String>(nu, ns, true)));
        g.addNode(nu);
        assertThrows(IllegalArgumentException.class,
                () -> g.addEdge(new GraphEdge<String>(ns, nu, false)));
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, true);
        assertTrue(g.addEdge(esu));
        assertTrue(g.getEdge(new GraphEdge<String>(ns, nu, true)) != null);
        assertFalse(g.addEdge(new GraphEdge<String>(ns, nu, true, 6.0)));
        g.addNode("x");
        assertTrue(g.addEdge("x", "s"));
        assertTrue(g.getEdge("x", "s") != null);
        g.addNode("t");
        assertTrue(g.addWeightedEdge("s", "t", 5.0));
        GraphEdge<String> est = g.getEdge("s", "t");
        assertTrue(est != null);
        assertTrue(est.getWeight() == 5);
        GraphNode<String> nw = new GraphNode<String>("w");
        g.addNode(nw);
        assertTrue(g.addWeightedEdge(nw, nu, 4.0));
        assertTrue(g.getEdge("w", "u").getWeight() == 4);
        assertFalse(g.addEdge("w", "u"));
    }

    @Test
    final void testRemoveEdge() {
        Graph<String> g = new AdjacencyMatrixDirectedGraph<String>();
        assertThrows(NullPointerException.class,
                () -> g.removeEdge((GraphEdge<String>) null));
        GraphNode<String> ns = new GraphNode<String>("s");
        assertThrows(NullPointerException.class,
                () -> g.removeEdge((GraphNode<String>) null, ns));
        assertThrows(NullPointerException.class,
                () -> g.removeEdge(ns, (GraphNode<String>) null));
        g.addNode(ns);
        g.addNode("a");
        g.addEdge("s", "a");
        GraphNode<String> nt = new GraphNode<String>("t");
        assertThrows(IllegalArgumentException.class,
                () -> g.removeEdge(ns, nt));
        assertThrows(IllegalArgumentException.class,
                () -> g.removeEdge(nt, ns));
        g.addNode(nt);
        assertThrows(IllegalArgumentException.class,
                () -> g.removeEdge(ns, nt));
        g.addEdge("t", "s");
        assertTrue(g.getEdge("s", "a") != null);
        g.removeEdge("s", "a");
        assertTrue(g.getEdge("s", "a") == null);
        GraphEdge<String> ets = new GraphEdge<String>(nt, ns, true);
        assertTrue(g.getEdge(ets) != null);
        g.removeEdge(ets);
        assertTrue(g.getEdge(ets) == null);
        g.addEdge("a", "t");
        int i = g.getNodeIndexOf("a");
        int j = g.getNodeIndexOf(nt);
        assertTrue(g.getEdge(i, j) != null);
        g.removeEdge(i, j);
        assertTrue(g.getEdge(i, j) == null);

    }

    @Test
    final void testGetEdge() {
        // DONE
        AdjacencyMatrixDirectedGraph<String> graph = new AdjacencyMatrixDirectedGraph<String>();
        GraphNode<String> nA = new GraphNode<String>("A");
        GraphNode<String> nB = new GraphNode<String>("B");
        graph.addNode(nA);
        GraphEdge<String> eAB = new GraphEdge<String>(nA, nB, true);
        // null edge, or edge with null nodes, should throw NullPointerException
        assertThrows(NullPointerException.class,
                () -> graph.getEdge(null));
        assertThrows(NullPointerException.class,
                () -> graph.getEdge(null, nB));
        assertThrows(NullPointerException.class,
                () -> graph.getEdge(nA, null));
        assertThrows(NullPointerException.class,
                () -> graph.getEdge("A", null));
        assertThrows(NullPointerException.class,
                () -> graph.getEdge(null, "B"));
        // eAB should throw an IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> graph.getEdge(eAB));
        graph.addNode(nB);
        // eAB should be null
        assertNull(graph.getEdge(eAB));
        graph.addEdge(eAB);
        // eAB should be returned
        assertEquals(eAB, graph.getEdge(eAB));
        // test method overloading
        assertEquals(graph.getEdge(eAB), graph.getEdge(nA, nB));
        assertEquals(graph.getEdge(eAB), graph.getEdge("A", "B"));
        int indexA = graph.getNodeIndexOf(nA);
        int indexB = graph.getNodeIndexOf(nB);
        assertEquals(graph.getEdge(eAB), graph.getEdge(indexA, indexB));
        // test invalid index
        assertThrows(IndexOutOfBoundsException.class,
                () -> graph.getEdge(indexA, 100));
        assertThrows(IndexOutOfBoundsException.class,
                () -> graph.getEdge(100, indexB));
    }

    @Test
    final void testGetAdjacentNodesOf() {
        // DONE
        AdjacencyMatrixDirectedGraph<String> graph = new AdjacencyMatrixDirectedGraph<String>();
        GraphNode<String> nA = new GraphNode<String>("A");
        GraphNode<String> nB = new GraphNode<String>("B");
        GraphNode<String> nC = new GraphNode<String>("C");
        // null node should throw NullPointerException
        assertThrows(NullPointerException.class,
                () -> graph.getAdjacentNodesOf((GraphNode<String>) null));
        assertThrows(NullPointerException.class,
                () -> graph.getAdjacentNodesOf((String) null));
        // nA or "A" should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> graph.getAdjacentNodesOf(nA));
        assertThrows(IllegalArgumentException.class,
                () -> graph.getAdjacentNodesOf("A"));
        graph.addNode(nA);
        graph.addNode(nB);
        graph.addNode(nC);
        Set<GraphNode<String>> adjacentNodesA = graph.getAdjacentNodesOf(nA);
        // test method overloading
        assertEquals(adjacentNodesA, graph.getAdjacentNodesOf("A"));
        // the adjacent nodes of nA should be an empty set
        assertTrue(adjacentNodesA.isEmpty());
        graph.addEdge(nA, nB);
        graph.addEdge(nA, nC);
        adjacentNodesA = graph.getAdjacentNodesOf(nA);
        // the adjacent nodes of nA should be a set with size of 2
        assertEquals(2, adjacentNodesA.size());
        // the adjacent nodes of nA should be a set containing nB and nC
        assertTrue(adjacentNodesA.contains(nB));
        assertTrue(adjacentNodesA.contains(nC));
        // test method overloading
        assertEquals(adjacentNodesA, graph.getAdjacentNodesOf("A"));
        // test invalid index
        assertThrows(IndexOutOfBoundsException.class,
                () -> graph.getAdjacentNodesOf(100));
    }

    @Test
    final void testGetEdgesOf() {
        // DONE
        AdjacencyMatrixDirectedGraph<String> graph = new AdjacencyMatrixDirectedGraph<String>();
        GraphNode<String> nA = new GraphNode<String>("A");
        GraphNode<String> nB = new GraphNode<String>("B");
        GraphNode<String> nC = new GraphNode<String>("C");
        // null node should throw NullPointerException
        assertThrows(NullPointerException.class,
                () -> graph.getEdgesOf((GraphNode<String>) null));
        assertThrows(NullPointerException.class,
                () -> graph.getEdgesOf((String) null));
        // nA or "A" should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> graph.getEdgesOf(nA));
        assertThrows(IllegalArgumentException.class,
                () -> graph.getEdgesOf("A"));
        graph.addNode(nA);
        graph.addNode(nB);
        graph.addNode(nC);
        Set<GraphEdge<String>> edgesOfA = graph.getEdgesOf(nA);
        // test method overloading
        assertEquals(edgesOfA, graph.getEdgesOf("A"));
        // the edges connected with nA should be an empty set
        assertTrue(edgesOfA.isEmpty());
        GraphEdge<String> eAB = new GraphEdge<String>(nA, nB, true);
        graph.addEdge(eAB);
        GraphEdge<String> eBA = new GraphEdge<String>(nB, nA, true);
        graph.addEdge(eBA);
        edgesOfA = graph.getEdgesOf(nA);
        // outgoing edges of A should be a set with size of 1
        assertEquals(1, edgesOfA.size());
        // outgoing edges of A should contains eAB
        assertTrue(edgesOfA.contains(eAB));
        GraphEdge<String> eAC = new GraphEdge<String>(nA, nC, true);
        graph.addEdge(eAC);
        edgesOfA = graph.getEdgesOf(nA);
        // outgoing edges of A should be a set with size of 2
        assertEquals(2, edgesOfA.size());
        // outgoing edges of A should contains eAB and eAC
        assertTrue(edgesOfA.contains(eAB));
        assertTrue(edgesOfA.contains(eAC));
        // test method overloading
        assertEquals(edgesOfA, graph.getEdgesOf("A"));
        // test invalid index
        assertThrows(IndexOutOfBoundsException.class,
                () -> graph.getEdgesOf(100));
    }

    @Test
    final void testGetEdges() {
        // DONE
        AdjacencyMatrixDirectedGraph<String> graph = new AdjacencyMatrixDirectedGraph<String>();
        Set<GraphEdge<String>> graphEdges = graph.getEdges();
        // graphEdges should be an empty set
        assertTrue(graphEdges.isEmpty());
        GraphNode<String> nA = new GraphNode<String>("A");
        GraphNode<String> nB = new GraphNode<String>("B");
        GraphNode<String> nC = new GraphNode<String>("C");
        graph.addNode(nA);
        graph.addNode(nB);
        graph.addNode(nC);
        GraphEdge<String> eAB = new GraphEdge<>(nA, nB, true);
        GraphEdge<String> eBC = new GraphEdge<>(nB, nC, true);
        GraphEdge<String> eCA = new GraphEdge<>(nC, nA, true);
        graph.addEdge(eAB);
        graph.addEdge(eBC);
        graph.addEdge(eCA);
        graphEdges = graph.getEdges();
        // graphEdges should be a set with size of 3
        assertEquals(3, graphEdges.size());
        // graphEdges should contains eAB, eBC, eCA
        assertTrue(graphEdges.contains(eAB));
        assertTrue(graphEdges.contains(eBC));
        assertTrue(graphEdges.contains(eCA));
        GraphEdge<String> eAC = new GraphEdge<>(nA, nC, true);
        graph.addEdge(eAC);
        graphEdges = graph.getEdges();
        // graphEdges should be a set with size of 4
        assertEquals(4, graphEdges.size());
        // graphEdges should contains eAC
        assertTrue(graphEdges.contains(eAC));
    }

    @Test
    final void testGetDegreeOf() {
        // DONE
        AdjacencyMatrixDirectedGraph<String> graph = new AdjacencyMatrixDirectedGraph<String>();
        GraphNode<String> nA = new GraphNode<String>("A");
        GraphNode<String> nB = new GraphNode<String>("B");
        GraphNode<String> nC = new GraphNode<String>("C");
        // null node should throw NullPointerException
        assertThrows(NullPointerException.class,
                () -> graph.getDegreeOf((GraphNode<String>) null));
        assertThrows(NullPointerException.class,
                () -> graph.getDegreeOf((String) null));
        // nA or "A" node should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> graph.getDegreeOf(nA));
        assertThrows(IllegalArgumentException.class,
                () -> graph.getDegreeOf("A"));
        graph.addNode(nA);
        graph.addNode(nB);
        graph.addNode(nC);
        GraphEdge<String> eAB = new GraphEdge<>(nA, nB, true);
        GraphEdge<String> eAC = new GraphEdge<>(nA, nC, true);
        GraphEdge<String> eCA = new GraphEdge<>(nC, nA, true);
        // degree of nA should be 0
        assertEquals(0, graph.getDegreeOf(nA));
        // test method overloading
        assertEquals(0, graph.getDegreeOf("A"));
        graph.addEdge(eAB);
        graph.addEdge(eAC);
        graph.addEdge(eCA);
        // degree of nA should be 3
        assertEquals(3, graph.getDegreeOf(nA));
        // test method overloading
        assertEquals(3, graph.getDegreeOf("A"));
        graph.removeEdge(eAB);
        // degree of nA should be 2
        assertEquals(2, graph.getDegreeOf(nA));
        // test method overloading
        assertEquals(2, graph.getDegreeOf("A"));
        graph.removeEdge(eAC);
        // degree of nA should be 1
        assertEquals(1, graph.getDegreeOf(nA));
        // test method overloading
        assertEquals(1, graph.getDegreeOf("A"));
        graph.removeEdge(eCA);
        // degree of nA should be 0
        assertEquals(0, graph.getDegreeOf(nA));
        // test method overloading
        assertEquals(0, graph.getDegreeOf("A"));
        graph.removeNode(nA);
        // nA or "A" node should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> graph.getDegreeOf(nA));
        assertThrows(IllegalArgumentException.class,
                () -> graph.getDegreeOf("A"));
    }

    @Test
    final void testGetIngoingEdgesOf() {
        // DONE
        AdjacencyMatrixDirectedGraph<String> graph = new AdjacencyMatrixDirectedGraph<String>();
        GraphNode<String> nA = new GraphNode<String>("A");
        GraphNode<String> nB = new GraphNode<String>("B");
        GraphNode<String> nC = new GraphNode<String>("C");
        // null node should throw NullPointerException
        assertThrows(NullPointerException.class,
                () -> graph.getIngoingEdgesOf((GraphNode<String>) null));
        assertThrows(NullPointerException.class,
                () -> graph.getIngoingEdgesOf((String) null));
        // nA or "A" node should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> graph.getIngoingEdgesOf(nA));
        assertThrows(IllegalArgumentException.class,
                () -> graph.getIngoingEdgesOf("A"));
        graph.addNode(nA);
        graph.addNode(nB);
        graph.addNode(nC);
        GraphEdge<String> eAB = new GraphEdge<>(nA, nB, true);
        GraphEdge<String> eAC = new GraphEdge<>(nA, nC, true);
        graph.addEdge(eAB);
        graph.addEdge(eAC);
        Set<GraphEdge<String>> ingoingEdgesOfA = graph.getIngoingEdgesOf(nA);
        // test method overloading
        assertEquals(ingoingEdgesOfA, graph.getIngoingEdgesOf("A"));
        // ingoingEdgesOfA should be an empty set
        assertTrue(ingoingEdgesOfA.isEmpty());
        GraphEdge<String> eCA = new GraphEdge<>(nC, nA, true);
        graph.addEdge(eCA);
        ingoingEdgesOfA = graph.getIngoingEdgesOf(nA);
        // ingoingEdgesOfA should be a set with size of 1
        assertEquals(1, ingoingEdgesOfA.size());
        // ingoingEdgesOfA should contain eCA
        assertTrue(ingoingEdgesOfA.contains(eCA));
        GraphEdge<String> eBA = new GraphEdge<>(nB, nA, true);
        graph.addEdge(eBA);
        ingoingEdgesOfA = graph.getIngoingEdgesOf(nA);
        // ingoingEdgesOfA should be a set with size of 2
        assertEquals(2, ingoingEdgesOfA.size());
        // ingoingEdgesOfA should contain eBA
        assertTrue(ingoingEdgesOfA.contains(eBA));
        // test method overloading
        assertEquals(ingoingEdgesOfA, graph.getIngoingEdgesOf("A"));
        // test invalid index
        assertThrows(IndexOutOfBoundsException.class,
                () -> graph.getIngoingEdgesOf(100));
    }

    @Test
    final void testGetPredecessorNodesOf() {
        // DONE
        AdjacencyMatrixDirectedGraph<String> graph = new AdjacencyMatrixDirectedGraph<String>();
        GraphNode<String> nA = new GraphNode<String>("A");
        GraphNode<String> nB = new GraphNode<String>("B");
        GraphNode<String> nC = new GraphNode<String>("C");
        // null node should throw NullPointerException
        assertThrows(NullPointerException.class,
                () -> graph.getPredecessorNodesOf((GraphNode<String>) null));
        assertThrows(NullPointerException.class,
                () -> graph.getPredecessorNodesOf((String) null));
        // nA node should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> graph.getPredecessorNodesOf(nA));
        assertThrows(IllegalArgumentException.class,
                () -> graph.getPredecessorNodesOf("A"));
        graph.addNode(nA);
        graph.addNode(nB);
        graph.addNode(nC);
        GraphEdge<String> eAB = new GraphEdge<>(nA, nB, true);
        GraphEdge<String> eBA = new GraphEdge<>(nB, nA, true);
        GraphEdge<String> eCA = new GraphEdge<>(nC, nA, true);
        graph.addEdge(eAB);
        Set<GraphNode<String>> predecessorNodesOfA = graph.getPredecessorNodesOf(nA);
        // test method overloading
        assertEquals(predecessorNodesOfA, graph.getPredecessorNodesOf("A"));
        // predecessorNodesOfA should be an empty set
        assertTrue(predecessorNodesOfA.isEmpty());
        graph.addEdge(eBA);
        graph.addEdge(eCA);
        predecessorNodesOfA = graph.getPredecessorNodesOf(nA);
        // predecessorNodesOfA should be a set with size of 2
        assertEquals(2, predecessorNodesOfA.size());
        // predecessorNodesOfA should contain nB and nC
        assertTrue(predecessorNodesOfA.contains(nB));
        assertTrue(predecessorNodesOfA.contains(nC));
        // test method overloading
        assertEquals(predecessorNodesOfA, graph.getPredecessorNodesOf("A"));
        // test invalid index
        assertThrows(IndexOutOfBoundsException.class,
                () -> graph.getPredecessorNodesOf(100));
    }
    
    // TODO aggiungere altri test

}
