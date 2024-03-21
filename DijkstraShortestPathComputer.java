package it.unicam.cs.asdl2223.pt2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
//TODO completare gli import necessari

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Gli oggetti di questa classe sono calcolatori di cammini minimi con sorgente
 * singola su un certo graph orientato e pesato dato. Il graph su cui lavorare
 * deve essere passato quando l'oggetto calcolatore viene costruito e non può
 * contenere archi con pesi negativi. Il calcolatore implementa il classico
 * algoritmo di Dijkstra per i cammini minimi con sorgente singola utilizzando
 * una coda con priorità che estrae l'elemento con priorità minima e aggiorna le
 * priorità con l'operazione decreasePriority in tempo logaritmico (coda
 * realizzata con uno heap binario). In questo caso il tempo di esecuzione
 * dell'algoritmo di Dijkstra è {@code O(n log m)} dove {@code n} è il numero di
 * nodi del graph e {@code m} è il numero di archi.
 * 
 * @author Luca Tesei (template)
 * @author Edoardo Conti edoardo.conti@studenti.unicam.it (implementazione)
 *
 * @param <L>
 *                il tipo delle etichette dei nodi del graph
 */
public class DijkstraShortestPathComputer<L>
        implements SingleSourceShortestPathComputer<L> {

    private GraphNode<L> lastSource;

    private final Graph<L> graph;

    private boolean isComputed = false;

    // Coda con priorità usata dall'algoritmo
    private BinaryHeapMinPriorityQueue queue;

    /**
     * Crea un calcolatore di cammini minimi a sorgente singola per un graph
     * diretto e pesato privo di pesi negativi.
     * 
     * @param graph
     *                  il graph su cui opera il calcolatore di cammini minimi
     * @throws NullPointerException
     *                                      se il graph passato è nullo
     * 
     * @throws IllegalArgumentException
     *                                      se il graph passato è vuoto
     * 
     * @throws IllegalArgumentException
     *                                      se il graph passato non è orientato
     * 
     * @throws IllegalArgumentException
     *                                      se il graph passato non è pesato,
     *                                      cioè esiste almeno un arco il cui
     *                                      peso è {@code Double.NaN}
     * @throws IllegalArgumentException
     *                                      se il graph passato contiene almeno
     *                                      un peso negativo
     */
    public DijkstraShortestPathComputer(Graph<L> graph) {
        // DONE
        if(graph == null)
            throw new NullPointerException("Il grafo fornito è null.");
        if(graph.isEmpty())
            throw new IllegalArgumentException("Il grafo fornito è vuoto.");
        if(!graph.isDirected())
            throw new IllegalArgumentException("Il grafo fornito non è orientato. L'algoritmo di Dijkstra lavora solamente con grafi orientati.");

        // O(V)
        for(GraphEdge<L> edge : graph.getEdges()) // O(V^2)
            if(!edge.hasWeight() || edge.getWeight() < 0)
                throw new IllegalArgumentException("Il grafo fornito contiene archi non pesati o con peso negativo. L'algoritmo di Dijkstra lavora solamente con grafi i cui archi hanno tutti un peso non negativo.");


        this.graph = graph;
        this.queue = new BinaryHeapMinPriorityQueue();
    }

    @Override
    public void computeShortestPathsFrom(GraphNode<L> sourceNode) {
        // DONE
        if(sourceNode == null)
            throw new NullPointerException("Il nodo fornito è null.");
        if(this.graph.getNode(sourceNode) == null) // O(V)
            throw new IllegalArgumentException("Il grafo su cui lavora questo algoritmo non contiene il nodo fornito.");

        // initialize the shortest-path estimate
        this.initializeSingleSource(sourceNode); // O(V)
        // get all graph's nodes and insert them into the min-priority queue
        Set<GraphNode<L>> graphNodes = this.graph.getNodes(); // O(1)
        // O(V*log(V))
        for(GraphNode<L> node : graphNodes) // O(V)
            this.queue.insert(node); // O(log(V))

        // O((V + E) * log(V)) AGGEGATE ANALYSIS
        // O(E * log(V)) if all the vertices are reachable from the source
        // for each element in the queue (all vertices) // O(V)
        while(!this.queue.isEmpty()) {
            // extract the node closest to the source
            GraphNode<L> closest = (GraphNode<L>) this.queue.extractMinimum(); // O(log(V))
            // iterate over each adjacent nodes of the closest one
            // each edge is examined exactly once during the run of the algorithm (AGGEGATE ANALYSIS)
            for(GraphNode<L> adjacentNode : graph.getAdjacentNodesOf(closest)) { // O(E)
                // relax the adjacent node (may update distance and priority)
                this.relax(closest, adjacentNode); // O(log(V))
            }
        }
        this.isComputed = true;
        this.lastSource = sourceNode;
    }

    @Override
    public boolean isComputed() {
        return this.isComputed;
    }

    @Override
    public GraphNode<L> getLastSource() {
        // DONE
        if(!this.isComputed())
            throw new IllegalStateException("Non è ancora stato eseguito il calcolo dei cammini minimi per nessun nodo sorgente.");
        return this.lastSource;
    }

    @Override
    public Graph<L> getGraph() {
        return this.graph;
    }

    @Override
    public List<GraphEdge<L>> getShortestPathTo(GraphNode<L> targetNode) {
        // DONE
        if(targetNode == null)
            throw new NullPointerException("Il nodo fornito è null.");
        if(this.graph.getNode(targetNode) == null) // O(V)
            throw new IllegalArgumentException("Il grafo su cui lavora questo algoritmo non contiene il nodo fornito.");
        if(!this.isComputed())
            throw new IllegalStateException("Non è ancora stato eseguito il calcolo dei cammini minimi per nessun nodo sorgente.");

        // create a stack to store partial result
        Stack<GraphEdge<L>> stack = new Stack<GraphEdge<L>>();
        // create variables to keep track of the traversed nodes
        GraphNode<L> currentNode = targetNode;
        GraphNode<L> prevNode = currentNode.getPrevious();
        // O(V)
        // traceback the shortest path from the source to the target node
        while (prevNode != null) {
            GraphEdge<L> edge = this.graph.getEdge(prevNode, currentNode); // O(1)
            stack.push(edge); // O(1)
            currentNode = prevNode;
            prevNode = currentNode.getPrevious();
        }

        // create a list with the correct order (source --> target) by popping elements from the stack
        ArrayList<GraphEdge<L>> shortestPath = new ArrayList<GraphEdge<L>>();
        // O(V)
        while(!stack.isEmpty()) {
            shortestPath.add(stack.pop()); // O(1)
        }

        // return the shortest-path List if the source node is reachable from this target node
        // return null otherwise
        return currentNode.equals(this.getLastSource()) ? shortestPath : null;
    }

    /*
     * Metodo inserito per scopi di test JUnit
     */
    protected BinaryHeapMinPriorityQueue getQueue() {
        return this.queue;
    }


    // private methods

    /*
     * Method to initialize the shortest-path estimates and previous
     */
    private void initializeSingleSource(GraphNode<L> source) {
        // O(V)
        // set each node's priority to infinity and previous to null
        for(GraphNode<L> node : graph.getNodes()) { // O(1)
            node.setPriority(Double.MAX_VALUE);
            node.setPrevious(null);
        }
        // set source's priority to 0
        source.setPriority(0.0);
    }

    /*
     * Method to perform the relaxation step of an edge (u, v)
     */
    private void relax(GraphNode<L> u, GraphNode<L> v) {
        // get the edge which connects u and v
        GraphEdge<L> edge = this.graph.getEdge(u, v); // O(1)
        // compute the distance from source of the adjacent node
        double newDistance = u.getFloatingPointDistance() + edge.getWeight();
        // if the newDistance is lower than the current one then update distance, priority and previous accordingly
        if(v.getPriority() > newDistance){
            this.queue.decreasePriority(v, newDistance); // O(log(V))
            v.setPrevious(u);
        }
    }

}
