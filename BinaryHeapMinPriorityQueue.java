/**
 * 
 */
package it.unicam.cs.asdl2223.pt2;

import java.util.ArrayList;
import java.util.NoSuchElementException;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Implementazione di una coda con priorità tramite heap binario. Gli oggetti
 * inseriti in coda implementano l'interface PriorityQueueElement che permette
 * di gestire la priorità e una handle dell'elemento. La handle è fondamentale
 * per realizzare in tempo logaritmico l'operazione di decreasePriority che,
 * senza la handle, dovrebbe cercare l'elemento all'interno dello heap e poi
 * aggiornare la sua posizione. Nel caso di heap binario rappresentato con una
 * ArrayList la handle è semplicemente l'indice dove si trova l'elemento
 * nell'ArrayList. Tale campo naturalmente va tenuto aggiornato se l'elemento
 * viene spostato in un'altra posizione.
 * 
 * @author Luca Tesei (template)
 * @author Edoardo Conti edoardo.conti@studenti.unicam.it (implementazione)
 * 
 */
public class BinaryHeapMinPriorityQueue {

    /*
     * ArrayList per la rappresentazione dello heap. Vengono usate tutte le
     * posizioni (la radice dello heap è quindi in posizione 0).
     */
    private ArrayList<PriorityQueueElement> heap;

    /**
     * Crea una coda con priorità vuota.
     *
     */
    public BinaryHeapMinPriorityQueue() {
        this.heap = new ArrayList<PriorityQueueElement>();
    }

    /**
     * Add an element to this min-priority queue. The current priority
     * associated with the element will be used to place it in the correct
     * position in the heap. The handle of the element will also be set
     * accordingly.
     * 
     * @param element
     *                    the new element to add
     * @throws NullPointerException
     *                                  if the element passed is null
     */
    public void insert(PriorityQueueElement element) {
        // DONE
        if(element == null)
            throw new NullPointerException("Questa coda non accetta elementi null.");
        // append the element to the heap
        this.heap.add(element); // O(1)
        // get the index of the element
        int elementIndex = this.size() - 1; // O(1)
        // initialize the handle of the element just inserted
        element.setHandle(elementIndex);
        // rebuild the heap
        this.bubbleUpElement(elementIndex); // O(log(n))
    }



    /**
     * Returns the current minimum element of this min-priority queue without
     * extracting it. This operation does not affect the heap.
     * 
     * @return the current minimum element of this min-priority queue
     * 
     * @throws NoSuchElementException
     *                                    if this min-priority queue is empty
     */
    public PriorityQueueElement minimum() {
        // DONE
        if(this.isEmpty()) // O(1)
            throw new NoSuchElementException("Non ci sono elementi nella coda");
        // The element with the minimum priority is in the root of the heap
        return this.heap.get(0); // O(1)
    }

    /**
     * Extract the current minimum element from this min-priority queue. The
     * ternary heap will be updated accordingly.
     * 
     * @return the current minimum element
     * @throws NoSuchElementException
     *                                    if this min-priority queue is empty
     */
    public PriorityQueueElement extractMinimum() {
        // DONE
        if(this.isEmpty())
            throw new NoSuchElementException("Non ci sono elementi nella coda");

        // get the element with min priority
        PriorityQueueElement min = this.minimum(); // O(1)

        int lastElementIndex = this.size() - 1;
        // set the last element of the heap as the root of the heap
        this.heap.set(0, this.heap.get(lastElementIndex));
        this.heap.get(lastElementIndex).setHandle(0);
        // Remove the last element in the heap
        // the method below run in constant time O(1) because there are no subsequent elements to be shifted to the left
        this.heap.remove(lastElementIndex);

        // re-build the heap
        this.heapify(0); // O(log(n))

        // return the extracted element
        return min;
    }

    /**
     * Decrease the priority associated to an element of this min-priority
     * queue. The position of the element in the heap must be changed
     * accordingly. The changed element may become the minimum element. The
     * handle of the element will also be changed accordingly.
     * 
     * @param element
     *                        the element whose priority will be decreased, it
     *                        must currently be inside this min-priority queue
     * @param newPriority
     *                        the new priority to assign to the element
     * 
     * @throws NoSuchElementException
     *                                      if the element is not currently
     *                                      present in this min-priority queue
     * @throws IllegalArgumentException
     *                                      if the specified newPriority is not
     *                                      strictly less than the current
     *                                      priority of the element
     */
    public void decreasePriority(PriorityQueueElement element, double newPriority) {
        // DONE
        int elementHandle = element.getHandle();

        if(elementHandle >= this.size() || !this.heap.get(elementHandle).equals(element)) // O(1)
            throw new NoSuchElementException("L'elemento non è presente nella coda");
        if(newPriority > element.getPriority())
            throw new IllegalArgumentException("La priorità delle elemento in coda non può essere aumentata. " + "Priorità corrente: " + element.getPriority());

        PriorityQueueElement realElement = this.heap.get(elementHandle); // O(1)
        // set new element priority
        realElement.setPriority(newPriority); // O(1)
        // rebuild the heap
        this.bubbleUpElement(elementHandle); // O(log(n))
    }

    /**
     * Determines if this priority queue is empty.
     * 
     * @return true if this priority queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Return the current size of this queue.
     * 
     * @return the number of elements currently in this queue.
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Erase all the elements from this min-priority queue. After this operation
     * this min-priority queue is empty.
     */
    public void clear() {
        this.heap.clear();
    } // O(n)

    /*
     *  Build an heap starting from the element at rootIndex assuming that all its subtrees are min-heap
     * */
    private void heapify(int rootIndex) {
        if(rootIndex >= this.size())
            return;
        // get the element at the given index
        PriorityQueueElement currentElement = this.heap.get(rootIndex); // O(1)
        // get the index of its minor child
        int minChildIndex = this.getMinorChildIndex(rootIndex); // O(1)
        // if the current element has no children, then do nothing
        if(minChildIndex < 0)
            return;
        // if the current element has a lower priority than its children, then do nothing
        if(currentElement.getPriority() < this.heap.get(minChildIndex).getPriority()) // O(1)
            return;
        // swap the current element with its child with minor priority
        this.swap(rootIndex, minChildIndex); // O(1)
        // call heapify at the child's index of the current element (minChildIndex)
        this.heapify(minChildIndex); // O(1)
    }

    /*
    *   Return the index of the minor child of the element at the given index, if it exists.
    *   Return -1 otherwise.
    * */
    private int getMinorChildIndex(int index) { // O(1)
        if(!this.hasLeftChild(index))
            return -1;
        int leftChildIndex = this.leftChildIndex(index);
        if(!this.hasRightChild(index))
            return leftChildIndex;
        int rightChildIndex = this.rightChildIndex(index);
        if(this.heap.get(leftChildIndex).getPriority() <= this.heap.get(rightChildIndex).getPriority())
            return leftChildIndex;
        return rightChildIndex;
    }

    /*
     * Return the index of the left child of the element at the given index
     */
    private int leftChildIndex(int i) {
        return i * 2 + 1;
    } // O(1)

    /*
     * return index of the right child of the element at the given index
     */
    private int rightChildIndex(int i) {
        return i * 2 + 2;
    } // O(1)

    /*
     * return the index of the parent of the element at the given index
     */
    private int parentIndex(int i) {
        return (i - 1) / 2;
    } // O(1)

    /*
     * return true if the element at the given index has a left child.
     * return false otherwise.
     */
    private boolean hasLeftChild(int index) {
        return this.leftChildIndex(index) < this.size();
    } // O(1)

    /*
     * return true if the element at the given index has a right child.
     * return false otherwise.
     */
    private boolean hasRightChild(int index) {
        return this.rightChildIndex(index) < this.size();
    } // O(1)

    /*
     * return true if the element at the given index has a parent.
     * return false otherwise.
     */
    private boolean hasParent(int index) {
        return this.parentIndex(index) >= 0;
    } // O(1)

    /*
    * swap the element at firstIndex with the element at secondIndex in the heap
    * and sets the handles accordingly
    * */
    private void swap(int firstIndex, int secondIndex) { // O(1)
        PriorityQueueElement first = this.heap.get(firstIndex);
        PriorityQueueElement second = this.heap.get(secondIndex);
        this.heap.set(firstIndex, second);
        second.setHandle(firstIndex);
        this.heap.set(secondIndex, first);
        first.setHandle(secondIndex);
    }

    /*
    * move up the element at the given index until it is in the correct position in the heap
    * */
    private void bubbleUpElement(int elementIndex) {
        // get the index of the element's parent
        int parentIndex = this.parentIndex(elementIndex);
        // while the element has a parent (it is not in the root of the heap)
        // and its priority is lower or equal than the priority of the parent
        // O(log(n))
        while(this.hasParent(elementIndex) && this.heap.get(parentIndex).getPriority() > this.heap.get(elementIndex).getPriority()){
            // swap the element with its parent
            this.swap(parentIndex, elementIndex); // O(1)
            // update working variables
            elementIndex = parentIndex;
            parentIndex = this.parentIndex(elementIndex);
        }
    }

    /*
     * Metodo inserito per fini di test JUnit
     */
    protected ArrayList<PriorityQueueElement> getBinaryHeap() {
        return this.heap;
    } // O(1)

}

