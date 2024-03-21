/**
 * 
 */
package it.unicam.cs.asdl2223.pt2;

import java.util.*;

// TODO completare gli import necessari

// ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe che implementa un grafo orientato tramite matrice di adiacenza. Non
 * sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * 
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * 
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato (che può cambiare nel tempo). Il
 * dominio della mappa rappresenta quindi l'insieme dei nodi.
 * 
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco.
 * 
 * Questa classe supporta i metodi di cancellazione di nodi e archi e supporta
 * tutti i metodi che usano indici, utilizzando l'indice assegnato a ogni nodo
 * in fase di inserimento ed eventualmente modificato successivamente.
 * 
 *
 *  * @author Luca Tesei (template)
 *  * @author Edoardo Conti edoardo.conti@studenti.unicam.it (implementazione)
 * 
 * 
 */
public class AdjacencyMatrixDirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    private int edgeCount;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixDirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
        this.edgeCount = 0;
    }

    @Override
    public int nodeCount() {
        // DONE
        return this.nodesIndex.size();
    }

    @Override
    public int edgeCount() {
        // DONE
        return this.edgeCount;
    }

    @Override
    public void clear() {
        // DONE
        this.nodesIndex.clear(); // O(V)
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.edgeCount = 0;
    }

    @Override
    public boolean isDirected() {
        // DONE
        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
        // DONE
        if(node == null)
            throw new NullPointerException("Il nodo fornito è null.");

        // if the node is already in the graph return false
        if(this.containsNode(node)) // O(1)
            return false;

        // add the new node to the hashmap with its index in the matrix
        this.nodesIndex.put(node, this.nodeCount()); // O(1)

        // add new row in the matrix
        this.addEmptyRow(); // O(V)
        // add new column in the matrix
        this.addEmptyColumn(); // O(V)

        // the node has been added
        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {
        // DONE
        // create new GraphNode
        // the constructor of GraphNode throws a NullPointerException if the given label is null
        GraphNode<L> node = new GraphNode<>(label);

        // add new node to the Graph
        return this.addNode(node); // O(V)
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {
        // DONE
        if(node == null)
            throw new NullPointerException("Il nodo fornito è null.");

        if(!this.containsNode(node))
            throw new IllegalArgumentException("Il nodo fornito non è presente in questo grafo.");

        int nodeIndex = this.getNodeIndexOf(node); // O(1)

        // remove column related to the node
        // O(V^2)
        for(int i = 0; i < this.matrix.size(); i++){ // O(V)
            if(i == nodeIndex) // ignore the row to be removed
                continue;
            ArrayList<GraphEdge<L>> edges = this.matrix.get(i);
            edges.remove(nodeIndex); // O(V)
        }
        // remove row related to the node
        this.matrix.remove(nodeIndex); // O(V)

        // update the indexes of the nodes which are greater than the index of the removed node
        // O(V)
        for(Map.Entry<GraphNode<L>, Integer> entry : this.nodesIndex.entrySet())
            if(entry.getValue() > nodeIndex) // O(1)
                entry.setValue(entry.getValue() - 1); // decrease the value by 1 // O(1)

        // remove node index from the hashtable
        this.nodesIndex.remove(node); // O(1)
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
        // DONE
        // create new GraphNode
        // the constructor of GraphNode throws a NullPointerException if the given label is null
        GraphNode<L> node = new GraphNode<>(label);
        this.removeNode(node); // O(V^2)
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        // TODO implementare
        // DONE
        // get the index of the node
        // the method getNode may throw an IndexOutOfBoundException
        GraphNode<L> node = this.getNode(i); // O(V)
        this.removeNode(node); // O(V^2)
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
        // DONE
        if(node == null)
            throw new NullPointerException("Il nodo fornito è null.");
        if(this.containsNode(node)){
            // get the index of the node
            int nodeIndex = this.getNodeIndexOf(node); // O(1)
            // get the node at the given index
            return this.getNode(nodeIndex); // O(V)
        }
        // if the given node is not in this graph then return null
        return null;
    }

    @Override
    public GraphNode<L> getNode(L label) {
        // DONE
        // create new GraphNode
        // the constructor of GraphNode throws a NullPointerException if the given label is null
        GraphNode<L> node = new GraphNode<>(label);
        return this.getNode(node); // O(V)
    }

    @Override
    public GraphNode<L> getNode(int i) {
        // DONE
        if(i < 0 || i >= this.nodeCount())
            throw new IndexOutOfBoundsException("L'indice fornito non valido.");

        // search the key (GraphNode<L>) that references the given index
        // O(V)
        for(Map.Entry<GraphNode<L>, Integer> entry : this.nodesIndex.entrySet())
            if(entry.getValue() == i)
                return entry.getKey();

        // if there aren't nodes associated with the given index then return null
        // unreachable state
        return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        // DONE
        if(node == null)
            throw new NullPointerException("Il nodo fornito non può essere null.");
        if(!this.containsNode(node))
            throw new IllegalArgumentException("Il nodo fornito non è presente in questo grafo.");

        // get the index related with this node
        return this.nodesIndex.get(node); // O(1)
    }

    @Override
    public int getNodeIndexOf(L label) {
        // DONE
        // create new GraphNode
        // the constructor of GraphNode throws a NullPointerException if the given label is null
        GraphNode<L> node = new GraphNode<>(label);
        // getNodeIndexOf may throw an IllegalArgumentException
        return this.getNodeIndexOf(node); // O(1)
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        // DONE
        return this.nodesIndex.keySet(); // O(1)
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        // DONE
        if(edge == null)
            throw new NullPointerException("L'arco fornito non può essere null.");

        // get source and target nodes from the given edge
        GraphNode<L> sourceNode = edge.getNode1();
        GraphNode<L> targetNode = edge.getNode2();
        // get weight of the given edge
        double edgeWeight = edge.getWeight();

        if(!this.containsNode(sourceNode) || !this.containsNode(targetNode))
            throw new IllegalArgumentException("L'arco fornito contiene nodi che non sono presenti in questo grafo.");
        if(edge.isDirected() != this.isDirected())
            throw new IllegalArgumentException("L'arco fornito non è orientato. Questo grafo può contenere solo archi orientati.");

        // get the indexes of the edge's nodes
        int sourceNodeIndex = this.getNodeIndexOf(sourceNode); // O(1)
        int targetNodeIndex = this.getNodeIndexOf(targetNode); // O(1)

        // add the edge to the adjacency matrix
        if(!edge.hasWeight())
            // add an unweighted edge
            return this.addEdge(sourceNodeIndex, targetNodeIndex); // O(V)
        else
            // add a weighted edge
            return this.addWeightedEdge(sourceNodeIndex, targetNodeIndex, edgeWeight); // O(V)
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // DONE
        // get the indexes of the nodes
        // getNodeIndexOf may throw a NullPointerException or an IllegalArgumentException
        int sourceNodeIndex = this.getNodeIndexOf(node1); // O(1)
        int targetNodeIndex = this.getNodeIndexOf(node2); // O(1)
        // add the edge to the adjacency matrix
        return this.addEdge(sourceNodeIndex, targetNodeIndex); // O(V)
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2, double weight) {
        // DONE
        // get the indexes of the nodes
        // getNodeIndexOf may throw a NullPointerException or an IllegalArgumentException
        int sourceNodeIndex = this.getNodeIndexOf(node1); // O(1)
        int targetNodeIndex = this.getNodeIndexOf(node2); // O(1)
        // add the edge to the adjacency matrix
        return this.addWeightedEdge(sourceNodeIndex, targetNodeIndex, weight); // O(V)
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        // DONE
        // addWeightedEdge may throw a NullPointerException or an IllegalArgumentException
        return this.addWeightedEdge(label1, label2, Double.NaN); // O(V)
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        // DONE
        // getNode may throw a NullPointerException
        GraphNode<L> node1 = this.getNode(label1); // O(V)
        GraphNode<L> node2 = this.getNode(label2); // O(V)

        if(node1 == null || node2 == null)
            throw new IllegalArgumentException("Almeno una delle etichette fornite non è contenuta in nessun nodo del grafo.");

        // add the edge to the adjacency matrix
        return this.addEdgeToMatrix(node1, node2, weight); // O(1)
    }

    @Override
    public boolean addEdge(int i, int j) {
        // DONE
        // create an edge without weight
        // addWeightedEdge may throw an IndexOutOfBoundsException
        return this.addWeightedEdge(i, j, Double.NaN); // O(V)
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        // DONE
        // getNode may throw an IndexOutOfBoundException
        GraphNode<L> sourceNode = this.getNode(i); // O(V)
        GraphNode<L> targetNode = this.getNode(j); // O(V)
        // add the edge to the adjacency matrix
        return this.addEdgeToMatrix(sourceNode, targetNode, weight); // O(1)
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        // DONE
        if(edge == null)
            throw new NullPointerException("L'arco passato è null.");
        if(!this.containsEdge(edge))
            throw new IllegalArgumentException("L'arco fornito non è presente in questo grafo.");
        GraphNode<L> sourceNode = edge.getNode1();
        GraphNode<L> targetNode = edge.getNode2();
        // get the indexes of the nodes in the edge to be removed
        int sourceNodeIndex = this.getNodeIndexOf(sourceNode); // O(1)
        int targetNodeIndex = this.getNodeIndexOf(targetNode); // O(1)
        // remove edge using its indexes in the matrix
        this.removeEdge(sourceNodeIndex, targetNodeIndex); // O(1)
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // DONE
        if(node1 == null || node2 == null)
            throw new NullPointerException("Almeno uno dei nodi forniti è null.");
        // getNodeIndexOf may throw an IllegalArgumentException
        int targetNodeIndex = this.getNodeIndexOf(node2); // O(1)
        int sourceNodeIndex = this.getNodeIndexOf(node1); // O(1)

        if(!this.areConnected(sourceNodeIndex, targetNodeIndex))
            throw new IllegalArgumentException("Non c'è nessun arco che connette i nodi forniti.");

        // remove edge using its indexes in the matrix
        this.removeEdge(sourceNodeIndex, targetNodeIndex); // O(1)
    }

    @Override
    public void removeEdge(L label1, L label2) {
        // get the indexes of the nodes with the given labels
        // getNodeIndexOf may throw a NullPointerException or an IllegalArgumentException
        int sourceNodeIndex = this.getNodeIndexOf(label1); // O(1)
        int targetNodeIndex = this.getNodeIndexOf(label2); // O(1)
        // remove edge using its indexes in the matrix
        // removeEdge may throw an IllegalArgumentException if the given nodes are not connected
        this.removeEdge(sourceNodeIndex, targetNodeIndex);
    }

    @Override
    public void removeEdge(int i, int j) {
        // DONE
        if(i < 0 || i >= this.nodeCount() || j < 0 || j >= this.nodeCount())
            throw new IndexOutOfBoundsException("La coppia di indici forniti non è valida.");
        if(!this.areConnected(i, j))
            throw new IllegalArgumentException("Non c'è nessun arco che connette i nodi corrispondenti agli indici forniti.");

        // remove the edge from the adjacency matrix
        this.matrix.get(i).set(j, null); // O(1)
        // decrease edgeCount
        this.edgeCount--;
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        // DONE
        if(edge == null)
            throw new NullPointerException("L'arco fornito è null.");

        GraphNode<L> sourceNode = edge.getNode1();
        GraphNode<L> targetNode = edge.getNode2();

        if(!this.containsNode(sourceNode) || !this.containsNode(targetNode))
            throw new IllegalArgumentException("L'arco fornito contiene nodi non presenti in questo grafo.");

        // get the indexes of the edge's nodes
        int sourceNodeIndex = this.getNodeIndexOf(sourceNode); // O(1)
        int targetNodeIndex = this.getNodeIndexOf(targetNode); // O(1)

        // getEdge may throw an IndexOutOfBoundException
        return this.getEdge(sourceNodeIndex, targetNodeIndex); // O(1)
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // DONE
        // get the indexes of the nodes with the given labels
        // getNodeIndexOf may throw a NullPointerException or an IllegalArgumentException
        int sourceNodeIndex = this.getNodeIndexOf(node1); // O(1)
        int targetNodeIndex = this.getNodeIndexOf(node2); // O(1)

        return this.getEdge(sourceNodeIndex, targetNodeIndex); // O(1)
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        // DONE
        // get the indexes of the nodes with the given labels
        // getNodeIndexOf may throw a NullPointerException or an IllegalArgumentException
        int sourceNodeIndex = this.getNodeIndexOf(label1); // O(1)
        int targetNodeIndex = this.getNodeIndexOf(label2); // O(1)

        return this.getEdge(sourceNodeIndex, targetNodeIndex); // O(1)
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        // DONE
        if(i < 0 || i >= this.nodeCount() || j < 0 || j >= this.nodeCount())
            throw new IndexOutOfBoundsException("La coppia di indici forniti non è valida.");

        // return the edge if it exists
        // return null otherwise
        return this.matrix.get(i).get(j); // O(1)
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        // DONE
        // get the index of the node in the adjacency matrix
        // getNodeIndexOf may throw a NullPointerException or an IllegalArgumentException
        int nodeIndex = this.getNodeIndexOf(node); // O(1)

        return this.getAdjacentNodesOf(nodeIndex); // O(V)
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        // DONE
        // get the index of the node in the adjacency matrix
        // getNodeIndexOf may throw a NullPointerException or an IllegalArgumentException
        int nodeIndex = this.getNodeIndexOf(label); // O(1)

        return this.getAdjacentNodesOf(nodeIndex); // O(V)
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        // DONE
        if(i < 0 || i >= this.nodeCount())
            throw new IndexOutOfBoundsException("L'indice fornito non è valido.");

        // crate a new Set for storing the result
        Set<GraphNode<L>> adjacentNodes = new HashSet<GraphNode<L>>();

        // for each outgoing edges of this node
        // O(V)
        for(GraphEdge<L> edge : this.matrix.get(i))
            if(edge != null)
                adjacentNodes.add(edge.getNode2()); // add the target node to the result // O(1)

        // return the Set with the adjacent nodes of this node
        return adjacentNodes;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        // get the index of the node in the adjacency matrix
        // getNodeIndexOf may throw a NullPointerException or an IllegalArgumentException
        int nodeIndex = this.getNodeIndexOf(node); // O(1)

        return this.getPredecessorNodesOf(nodeIndex); // O(V)
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        // DONE
        // get the index of the node in the adjacency matrix
        // getNodeIndexOf may throw a NullPointerException or an IllegalArgumentException
        int nodeIndex = this.getNodeIndexOf(label); // O(1)

        return this.getPredecessorNodesOf(nodeIndex); // O(V)
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        // DONE
        if(i < 0 || i >= this.nodeCount())
            throw new IndexOutOfBoundsException("L'indice fornito non è valido");

        // crate a new Set for storing the result
        Set<GraphNode<L>> predecessorNodes = new HashSet<GraphNode<L>>();

        // for each ingoing edges of this node
        for(ArrayList<GraphEdge<L>> edges : this.matrix) { // O(V)
            GraphEdge<L> edge = edges.get(i); // O(1)
            if(edge != null)
                predecessorNodes.add(edge.getNode1()); // O(1)
        }

        // return the Set with the predecessor nodes of this node
        return predecessorNodes;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        // DONE
        // get the index of the node in the adjacency matrix
        // getNodeIndexOf may throw a NullPointerException or an IllegalArgumentException
        int nodeIndex = this.getNodeIndexOf(node); // O(1)

        return this.getEdgesOf(nodeIndex); // O(V)
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        // DONE
        // get the index of the node in the adjacency matrix
        // getNodeIndexOf may throw a NullPointerException or an IllegalArgumentException
        int nodeIndex = this.getNodeIndexOf(label); // O(1)

        return this.getEdgesOf(nodeIndex); // O(V)
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        // TODO implementare
        // DONE
        if(i < 0 || i >= this.nodeCount())
            throw new IndexOutOfBoundsException("L'indice fornito non è valido");

        // crate a new Set for storing the result
        Set<GraphEdge<L>> outgoingEdges = new HashSet<GraphEdge<L>>();

        for(GraphEdge<L> edge : this.matrix.get(i)) // O(V)
            if(edge != null)
                outgoingEdges.add(edge); // O(1)

        // return the set with all the outgoing edges of the node
        return outgoingEdges;
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        // DONE
        // get the index of the node in the adjacency matrix
        // getNodeIndexOf may throw a NullPointerException or an IllegalArgumentException
        int nodeIndex = this.getNodeIndexOf(node); // O(1)

        return getIngoingEdgesOf(nodeIndex); // O(V)
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        // DONE
        // get the index of the node in the adjacency matrix
        // getNodeIndexOf may throw a NullPointerException or an IllegalArgumentException
        int nodeIndex = this.getNodeIndexOf(label); // O(1)

        return getIngoingEdgesOf(nodeIndex); // O(V)
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        // DONE
        if(i < 0 || i >= this.nodeCount())
            throw new IndexOutOfBoundsException("L'indice fornito non è valido");

        // crate a new Set for storing the result
        Set<GraphEdge<L>> ingoingEdges = new HashSet<GraphEdge<L>>();

        // for each ingoing edges of this node
        // O(V)
        for(ArrayList<GraphEdge<L>> edges : this.matrix) {
            GraphEdge<L> edge = edges.get(i); // O(1)
            if(edge != null)
                ingoingEdges.add(edge); // O(1)
        }

        // return the Set with the ingoing edges of this node
        return ingoingEdges;
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        // DONE
        // create a set of GraphEdges to store the result
        Set<GraphEdge<L>> graphEdges = new HashSet<GraphEdge<L>>();
        // O(V^2)
        for(ArrayList<GraphEdge<L>> edges : this.matrix) // O(V)
            for(GraphEdge<L> edge : edges) // O(V)
                if(edge != null)
                    // for each cell of the adjacency matrix, if it stores an edge then add it to the set
                    graphEdges.add(edge);

        // return the set
        return graphEdges;
    }
    
    
    ///// private methods
    
    /*
    * Add a new empty row to the matrix
    * */
    private void addEmptyRow() {
        // create an ArrayList filled by null values (empty row)
        ArrayList<GraphEdge<L>> row = new ArrayList<>();
        // O(V)
        for(int i = 0; i < this.nodeCount(); i++ )
            row.add(null);
        // add the new row to the matrix
        this.matrix.add(row); // O(1)
    }

    /*
    * Add new Empty column to the matrix
    * */
    private void addEmptyColumn(){
        // O(V)
        // add new column that stores a null value to each row of the matrix
        for(ArrayList<GraphEdge<L>> row : this.matrix)
            row.add(null); // O(1)
    }

    /*
    * return true if the given node is present in this graph
    * false otherwise
    * */
    private boolean containsNode(GraphNode<L> node){
        return this.nodesIndex.containsKey(node); // O(1)
    }

    /*
     * return true is the given edge is present in this graph
     * false otherwise
     * */
    private boolean containsEdge(GraphEdge<L> edge){
        int sourceNodeIndex = this.getNodeIndexOf(edge.getNode1()); // O(1)
        int targetNodeIndex = this.getNodeIndexOf(edge.getNode2()); // O(1)

        return edge.equals(this.matrix.get(sourceNodeIndex).get(targetNodeIndex)); // O(1)
    }

    /*
    * return true if the nodes at the given indexes are connected
    * */
    private boolean areConnected(int i, int j) {
        return this.matrix.get(i).get(j) != null;
    }


    /*
    *  add an edge to the matrix assuming that its nodes are actually nodes that belong to this graph
    * */
    private boolean addEdgeToMatrix(GraphNode<L> sourceNode, GraphNode<L> targetNode, double weight) {
        // get the indexes of the nodes to be added
        int sourceNodeIndex = this.getNodeIndexOf(sourceNode); // O(1)
        int targetNodeIndex = this.getNodeIndexOf(targetNode); // O(1)

        // create new GraphEdge with nodes that actually belong to this graph
        GraphEdge<L> edge = new GraphEdge<L>(sourceNode, targetNode, true, weight);

        // if in this graph there is an edge equals to the given one return false
        if(this.containsEdge(edge)) // O(1)
            return false;

        // add the edge to the adjacency matrix
        this.matrix.get(sourceNodeIndex).set(targetNodeIndex, edge); // O(1)
        this.edgeCount++;
        return true;
    }
}
