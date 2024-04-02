package it.unicam.cs.asdl2324.mp2;

import java.util.*;

//TODO completare gli import necessari

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * 
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 * 
 * @author Luca Tesei (template) **Davide Mafalda Email: davide.mafalda@studenti.unicam.it(implementazione)
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class KruskalMST<L> {

    /*
     * Struttura dati per rappresentare gli insiemi disgiunti utilizzata
     * dall'algoritmo di Kruskal.
     */
    private ForestDisjointSets<GraphNode<L>> disjointSets;
    private final Comparatore edgesComparator ;
    
    
    private class Comparatore implements Comparator<GraphEdge<L>>{
        @Override
        public int compare(GraphEdge<L> edge1, GraphEdge<L> edge2){
            return Double.compare(edge1.getWeight(), edge2.getWeight());
        }
    }
    /**
     * Costruisce un calcolatore di un albero di copertura minimo che usa
     * l'algoritmo di Kruskal su un grafo non orientato e pesato.
     */
    public KruskalMST() {
        this.disjointSets = new ForestDisjointSets<GraphNode<L>>();
        this.edgesComparator = new Comparatore();
    }

    /**
     * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. L'albero restituito non è radicato, quindi è rappresentato
     * semplicemente con un sottoinsieme degli archi del grafo.
     *
     * @param g un grafo non orientato, pesato, con pesi non negativi
     * @return l'insieme degli archi del grafo g che costituiscono l'albero di
     * copertura minimo trovato
     * @throw NullPointerException se il grafo g è null
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     * con pesi negativi
     */
        public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
           //se l'elemento è null, lancia un'eccezione
            if (g == null)
                throw new NullPointerException(
                        "computerMSP: Grafo nullo ");
            // Per la leggibilità, utilizzo un metodo per controllare i parametri
            checkExceptions(g);

            // Set da ritornare
            Set<GraphEdge<L>> newSet = new HashSet<>();
            //pulisco il set

            //creo un insieme disgiunto per ogni nodo

            //ordino gli archi in base al loro peso
            ArrayList<GraphEdge<L>> archiPesati = new ArrayList<>(g.getEdges());
            archiPesati.sort(edgesComparator);
            ArrayList<GraphNode<L>> nodeSet=new ArrayList<>();
                //per ogni arco del grafo
            for (GraphEdge<L> current : archiPesati) {
                //se l'arco non fa parte dell'albero di copertura minimo
                if (!nodeSet.contains(current.getNode2())) {
                //aggiungo l'arco all'albero di copertura minimo
                    nodeSet.add(current.getNode2());
                    newSet.add(current);
                }
            }
            //ritorno l'albero di copertura minimo
            return newSet;
        }



    private void checkExceptions(Graph<L> g) {
       //verifico che il grafo non sia orientato
        if (g.isDirected())
            throw new IllegalArgumentException("Il grafo deve essere NON orientato.");
        //che ogni arco del grafo sia pesato
        for (GraphEdge<L> e : g.getEdges()) {
            if (!e.hasWeight())
                throw new IllegalArgumentException(
                        "Arco non pesato: " + e);
            //e che il peso dell'arco non sia negativo
            if (e.getWeight() <= 0)
                throw new IllegalArgumentException(
                        "Arco con peso negativo: " + e);
        }
    }
}
