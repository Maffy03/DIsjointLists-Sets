package it.unicam.cs.asdl2324.mp2;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.util.HashMap;
//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Implementazione dell'interfaccia <code>DisjointSets<E></code> tramite una
 * foresta di alberi ognuno dei quali rappresenta un insieme disgiunto. Si
 * vedano le istruzioni o il libro di testo Cormen et al. (terza edizione)
 * Capitolo 21 Sezione 3.
 * 
 * @author Luca Tesei (template) **Davide Mafalda Email: davide.mafalda@studenti.unicam.it** (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi degli insiemi disgiunti
 */
public class ForestDisjointSets<E> implements DisjointSets<E> {

    /*
     * Mappa che associa ad ogni elemento inserito il corrispondente nodo di un
     * albero della foresta. La variabile è protected unicamente per permettere
     * i test JUnit.
     */
    protected Map<E, Node<E>> currentElements;
    
    /*
     * Classe interna statica che rappresenta i nodi degli alberi della foresta.
     * Gli specificatori sono tutti protected unicamente per permettere i test
     * JUnit.
     */
    protected static class Node<E> {
        /*
         * L'elemento associato a questo nodo
         */
        protected E item;

        /*
         * Il parent di questo nodo nell'albero corrispondente. Nel caso in cui
         * il nodo sia la radice allora questo puntatore punta al nodo stesso.
         */
        protected Node<E> parent;

        /*
         * Il rango del nodo definito come limite superiore all'altezza del
         * (sotto)albero di cui questo nodo è radice.
         */
        protected int rank;

        /**
         * Costruisce un nodo radice con parent che punta a se stesso e rango
         * zero.
         * 
         * @param item
         *                 l'elemento conservato in questo nodo
         * 
         */
        public Node(E item) {
            this.item = item;
            this.parent = this;
            this.rank = 0;
        }

    }

    /**
     * Costruisce una foresta vuota di insiemi disgiunti rappresentati da
     * alberi.
     */
    public ForestDisjointSets() {
        currentElements = new HashMap<>();
    }

    @Override
    public boolean isPresent(E e) {
        //se è nullo lancio un'eccezione
        if(e==null) throw new NullPointerException("isPresent: e is null");
        //se l'elemento è presente allora ritorno true
        if(currentElements.containsKey(e))
            return true;
        else
            return false;
    }

    /*
     * Crea un albero della foresta consistente di un solo nodo di rango zero il
     * cui parent è se stesso.
     */
    @Override
    public void makeSet(E e) {
        //effettuo le dovute verifiche in modo che l'elemento passato non sia null e che non sia già esistente
        if(e == null)
            throw new NullPointerException("Elemento nullo.");
        if (isPresent(e))
            throw new IllegalArgumentException("Elemento già presente.");
        // creo il nodo e lo inserisco nella mappa
        Node<E> node = new Node<>(e);
        currentElements.put(e, node);
    }

    /*
     * L'implementazione del find-set deve realizzare l'euristica
     * "compressione del cammino". Si vedano le istruzioni o il libro di testo
     * Cormen et al. (terza edizione) Capitolo 21 Sezione 3.
     */
    @Override
    public E findSet(E e) {
        if (e == null)
            throw new NullPointerException("Findset: e is null");

        if (!isPresent(e))
            return null;

        Node<E> node = currentElements.get(e); // ottengo il nodo corrispondente all'elemento passato

        // se il nodo è la radice allora il rappresentante dell'insieme è l'elemento
        if (!node.equals(node.parent))
            // se il nodo non è la radice allora devo ricorrere al findset del parent
            node.parent = currentElements.get(findSet(node.parent.item));
            //return della radice dell'albero
        return node.parent.item;

    }

    /*
     * L'implementazione dell'unione deve realizzare l'euristica
     * "unione per rango". Si vedano le istruzioni o il libro di testo Cormen et
     * al. (terza edizione) Capitolo 21 Sezione 3. In particolare, il
     * rappresentante dell'unione dovrà essere il rappresentante dell'insieme il
     * cui corrispondente albero ha radice con rango più alto. Nel caso in cui
     * il rango della radice dell'albero di cui fa parte e1 sia uguale al rango
     * della radice dell'albero di cui fa parte e2 il rappresentante dell'unione
     * sarà il rappresentante dell'insieme di cui fa parte e2.
     */
    @Override
    public void union(E e1, E e2) {
        //effettuo i dovuti controlli in modo che gli elementi passati non siano null, che siano presenti e che siano diversi
        if (e1 == null || e2 == null)
            throw new NullPointerException("Elementi nulli.");
        if (!isPresent(e1) || !isPresent(e2))
            throw new IllegalArgumentException("Elemento non presente.");
        if (findSet(e1).equals(findSet(e2)))
            return;
        //salvo nelle nuove variabili i rappresentanti dell'insieme di cui fa parte e1 e e2
        Node<E> rad1 = currentElements.get(findSet(e1));
        Node<E> rad2 = currentElements.get(findSet(e2));

        //se il rango della radice dell'albero di cui fa parte e1 è maggiore del rango della radice dell'albero 2
        if (rad1.rank > rad2.rank)
           //il primo rappresentante diventa la radice del secondo albero
            rad2.parent = rad1;
        else {
            //viceversa
            rad1.parent = rad2;
            if (rad1.rank == rad2.rank)
                rad2.rank++;
        }
    }

    @Override
    public Set<E> getCurrentRepresentatives() {
        //creo un nuovo set di elementi
        Set<E> newSet = new HashSet<>();
        //scorro attraverso gli insiemi disgiunti e aggiungo alla nuova lista ogni rappresentante dell'insieme
        for (E element : currentElements.keySet())
            if (findSet(element).equals(element))
                newSet.add(element);
        //ritorno il nuovo set
        return newSet;
    }

    @Override
    public Set<E> getCurrentElementsOfSetContaining(E e) {
        if (e == null)
            throw new NullPointerException("Elemento nullo.");
        if (!isPresent(e))
            throw new IllegalArgumentException("Elemento non presente.");
        //salvo il rappresentante dell'insieme di cui fa parte e
        E rapp = findSet(e);

        //creo un nuovo set di elementi
        Set<E> newSet = new HashSet<>();

        //scorro attraverso gli insiemi disgiunti e aggiungo alla nuova lista ogni elemento dell'insieme interessato
        for (E element : currentElements.keySet())
            if (rapp.equals(findSet(element)))
                newSet.add(element);

        //ritorno il nuovo set
        return newSet;

    }

    @Override
    public void clear() {
        //svuoto l'hashmap
        currentElements.clear();
    }

}
