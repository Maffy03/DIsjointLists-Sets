/**
 * 
 */
package it.unicam.cs.asdl2324.mp2;

import java.util.*;

// TODO completare gli import necessari

// ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
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
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 * 
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 * 
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 * 
 * @author Luca Tesei (template) **DAVIDE MAFALDA,Email: davide.mafalda@studenti.unicam.it** (implementazione)
 *
 * 
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
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

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public int nodeCount() {
        return nodesIndex.size();
    }

    @Override
    public int edgeCount() {
        //
        int edgecount = 0;
        //scorro la matrice di adiacenza per contare gli archi
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
               //se l'elemento della matrice di adiacenza è null non è presente un arco
                if (matrix.get(i).get(j) != null) {
                    edgecount++;
                }
            }
        }
        return edgecount/2;

    }

    @Override
    public void clear() {
        // reinizializzando gli oggetti, ne creiamo dei nuovi vuoti
        this.nodesIndex = new HashMap<>();
        this.matrix = new ArrayList<>();
    }

    @Override
    //il grafo non è orientato, quindi ritorniamo false
    public boolean isDirected() {
        return false;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
        if(node==null) throw new NullPointerException("addNode: node passato null");
        //se il nodo è già presente nel grafo ritorno false
        if(nodesIndex.containsKey(node)) return false;
        //aggiungo il nodo passato come parametro alla mappa dei nodi
        this.nodesIndex.put(node, nodeCount());
        ArrayList<GraphEdge<L>> nuovaRiga = new ArrayList<>(matrix.size());
        //creo una nuova riga che riempirò di null
        for (int current = 0; current < nodeCount() - 1; current++)
        {
            nuovaRiga.add(null);
        }

        //aggiungo la riga alla matrice di adiacenza
        matrix.add(nuovaRiga);
        //completo la colonna
        for (int index = 0; index < nodeCount(); index++)
            matrix.get(index).add(null);


        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {
        //ritornerà true se il nodo è stato aggiunto correttamente
        return addNode(new GraphNode<>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Nodo nullo.");
        if (!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Nodo non presente nel grafo.");

        //Set d'appoggio
        Set<GraphNode<L>> newSet = nodesIndex.keySet();

        //Servirà per tenere memorizzato value dopo la rimozione del nodo dall nodesIndex
        int newSetValue = nodesIndex.get(node);

        //scorro all'arraylist della matrice di adiacenza per rimuovere tutti gli archi interessati
        for (int index = 0; index < nodeCount(); index++)
            matrix.get(index).remove(newSetValue);
        matrix.remove(newSetValue);
        //rimuovo il nodo dalla mappa dei nodi
        nodesIndex.remove(node);

        //aumento il valore di toSetValue per tutti i nodi con indice maggiore di toSetValue
        for (GraphNode<L> element : newSet)
            if (nodesIndex.get(element) > newSetValue) {
                nodesIndex.put(element, newSetValue);
                newSetValue++;
            }
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
        removeNode(new GraphNode<>(label));
    }


    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        if (i < 0)
            throw new IndexOutOfBoundsException("Valore minore di 0.");
        if (i > nodeCount() - 1)
            throw new IndexOutOfBoundsException("Valore maggiore di: " + nodeCount() + ".");

        GraphNode<L> node = matrix.get(i).get(i).getNode1();
        removeNode(node);    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
        //se il nodo è null tiriamo un'eccezione
        if (node == null) throw new NullPointerException("getNode(node): Nodo passato nullo.");
        //con un'iterazione cerco il nostro nodo, se lo trovo, lo ritorno, altrimenti ritorno null
        for (GraphNode<L> element : nodesIndex.keySet()) {
            if (element.equals(node)) {
                return element;
            }
        }
        return null;
    }

    @Override
    public GraphNode<L> getNode(L label) {
        if(label==null) throw new NullPointerException("getNode(label): label nulla");
        return getNode(new GraphNode<>(label));
    }

    @Override
    public GraphNode<L> getNode(int i) {
        if (i < 0 || i > nodeCount() - 1) throw new IndexOutOfBoundsException("getNode(i): valore fuori dai limiti");
        //con un'iterazione cerco il nostro nodo, se lo trovo, lo ritorno, altrimenti ritorno null
        for (GraphNode<L> node : nodesIndex.keySet()) {
            if (nodesIndex.get(node) == i) {
                return node;
            }
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        //dopo aver effettuato i controlli
        if(node==null) throw new NullPointerException("getNodeIndexOf: parametro passato nullo");

        //se troviamo il nodo corrispondente all'indice lo ritorniamo, altrimenti tiriamo un'eccezione
        for (GraphNode<L> index : nodesIndex.keySet())
            if (index.equals(node))
                return nodesIndex.get(index);

        throw new IllegalArgumentException("Nodo non appartenente al grafo.");

    }

    @Override
    public int getNodeIndexOf(L label) {
        if(label==null) throw new NullPointerException("getNodeIndexOf(label): parametro passato nullo");
        return getNodeIndexOf(new GraphNode<>(label));
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        //se l'arco è nullo
        if (edge == null)
            throw new NullPointerException("Arco nullo.");
        //oppure è orientato
        if (edge.isDirected())
            throw new IllegalArgumentException("L' arco incompatibile con il grafo.");
        //o se uno dei due noi dell'arco non esiste, tiro delle eccezioni
        if (!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2()))
            throw new IllegalArgumentException("L'arco non appartenente al grafo.");
        //creo due variabili cne salvano i valori dei due nodi interessati
        int index1 = nodesIndex.get(edge.getNode1());
        int index2 = nodesIndex.get(edge.getNode2());

        //se esiste gia un arco identico, return false
        if (matrix.get(index1).get(index2)!=null|| matrix.get(index2).get(index1)!=null) return false;

        //unisco i due nodi con lo stesso arco
        matrix.get(index1).set(index2, edge);
        matrix.get(index2).set(index1, edge);
        return true;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if(node1==null||node2==null) throw new NullPointerException("ddEdge: nodo passato null");
        //se uno dei due nodi non esiste, tiro delle eccezioni
        GraphNode<L> n1 = getNode(node1);
        GraphNode<L> n2 = getNode(node2);
        //creo due variabili cne salvano i valori dei due nodi interessati
        if(n1 == null || n2 == null) throw new IllegalArgumentException("addEdge: uno dei nodi non esiste nel grafo");

        return addEdge(new GraphEdge<>(node1, node2, false));
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2,
            double weight) {
        if(node1==null||node2==null) throw new NullPointerException("addWeightedEdge: nodo passato null");

        if(!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) {
            throw new IllegalArgumentException("Nodo non presente nel grafo");
        }

        return addEdge(new GraphEdge<>(node1, node2, false, weight));
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        if(label1==null||label2==null) throw new NullPointerException("addWeightedEdge: nodo passato null");
        return addEdge((new GraphEdge<>(new GraphNode<>(label1), new GraphNode<>(label2), false)));
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        if(label1==null||label2==null) throw new NullPointerException("addWeightedEdge: nodo passato null");

        GraphNode<L> node1 = getNode(label1);
        GraphNode<L> node2 = getNode(label2);
        if(node1==null || node2==null) throw new IllegalArgumentException("addWeightedEdge: uno dei nodi non esiste nel grafo");

        return addEdge((new GraphEdge<>(new GraphNode<>(label1), new GraphNode<>(label2), false, weight)));
    }

    @Override
    public boolean addEdge(int i, int j) {
        if(i<0||i>nodeCount()-1) throw new IndexOutOfBoundsException ("addEdge(i,j): valore fuori dai limiti");
        if(j<0||j>nodeCount()-1) throw new IndexOutOfBoundsException ("addEdge(i,j): valore fuori dai limiti");

        return addWeightedEdge(i, j, Double.NaN);
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        if (i < 0 || j < 0||i>nodeCount()-1||j>nodeCount()-1)
            throw new IndexOutOfBoundsException ("addWeightedEdge(i,j): valore fuori dai limiti");

        GraphNode<L> n1 = matrix.get(i).get(i).getNode1();
        GraphNode<L> n2 = matrix.get(j).get(j).getNode1();
        return addEdge((new GraphEdge<>(n1, n2, false, weight)));
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
       //se l'arco è nullo
        if (edge == null)
            throw new NullPointerException("removeEdge: Nodo nullo");
       //o è orientato
        if (edge.isDirected())
            throw new IllegalArgumentException("remove edge: L' arco non può essere orientato");
        //o se uno dei due nodi dell'arco non esiste
        if (!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2()))
            throw new IllegalArgumentException("removeEdge: uno degli archi non appartiene al grafo");
        // o se l'arco non esiste lancio delle eccezioni
        if (matrix.get(nodesIndex.get(edge.getNode1())).get(nodesIndex.get(edge.getNode2())) == null)
            throw new IllegalArgumentException("removeEdge: uno dei nodi non è presente nel grafo");

        //salvo i due nodi interessati
        int index1 = nodesIndex.get(edge.getNode1());
        int index2 = nodesIndex.get(edge.getNode2());

        //unisco i due nodi con lo stesso arco, così da rimuovere l'arco
        matrix.get(index1).set(index2, null);
        matrix.get(index2).set(index1, null);
    }


    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null||node2 == null)
            throw new NullPointerException("removeEdge(node1, node1): Nodi nullo");
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2))
            throw new IllegalArgumentException("removeEdge: uno dei nodi non appartiene al grafo");

        removeEdge(new GraphEdge<>(node1, node2, false));

    }

    @Override
    public void removeEdge(L label1, L label2) {
        removeEdge(new GraphNode<>(label1), new GraphNode<>(label2));
    }

    @Override
    public void removeEdge(int i, int j) {
        if (i < 0 || j < 0)
            throw new IndexOutOfBoundsException("removeEdge(i,j): valore fuori dai limiti");
        if (i > nodeCount() || j > nodeCount())
            throw new IndexOutOfBoundsException("removeEdge(i,j): valore fuori dai limiti");

        //setto il nuovo arco a null
        matrix.get(i).set(j, null);
        matrix.get(j).set(i, null);
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        if (edge == null)
            throw new NullPointerException("getEdge: Nodo nullo");
        if (!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2()))
            throw new IllegalArgumentException("getEdge: arco non presente nel grafo");

        //salvo i due nodi interessati
        int n1 = nodesIndex.get(edge.getNode1());
        int n2 = nodesIndex.get(edge.getNode2());

        //se l'arco è presente nel grafo
        if ((matrix.get(n1).get(n2))!=null)
            //ritorno l'arco
            return matrix.get(n1).get(n2);

        return null;
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null)
            throw new NullPointerException("getEdge(node): nodo passato nullo");
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2))
            throw new IllegalArgumentException("getEdge: l'arco non è presente nel grafo");

        return getEdge(new GraphEdge<>(node1, node2, false));
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        if(label1==null||label2==null) throw new NullPointerException("addWeightedEdge: nodo passato null");

        if(label1 == null || label2 == null) {
            throw new NullPointerException("getEdge: etichetta nodo nulla");
        }
        //salvo i due nodi interessati
        GraphNode<L> node1 = getNode(label1);
        GraphNode<L> node2 = getNode(label2);
        //se sono null lancio eccezione
        if(node1 == null || node2 == null) {
            throw new IllegalArgumentException("getEdge: nodo non presente");
        }
        //salvo i due nodi interessati
        int index1 = nodesIndex.get(node1);
        int index2 = nodesIndex.get(node2);
        //creo un arco e lo ritorno
        GraphEdge<L> edge = matrix.get(index1).get(index2);

        if(edge == null) {
            edge = matrix.get(index2).get(index1);
        }

        return edge;

    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        if (i < 0 || j < 0)
            throw new IndexOutOfBoundsException("Valore minore di 0.");
        if (i > nodeCount() || j > nodeCount())
            throw new IndexOutOfBoundsException("Valore maggiore " + nodeCount() + ".");

        return matrix.get(i).get(j);
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException("Nodo nullo.");
        if (!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Il nodo non appartiene al grafo.");

        // Set da ritornare
        Set<GraphNode<L>> newSet = new HashSet<>();

        //Accedo alla riga del nodo
        for (GraphEdge<L> current  : matrix.get(nodesIndex.get(node)))
            if (current != null) {
                //se il nodo parametro è il primo di un arco
                if (current.getNode2().equals(node))
                    //aggiungo il nodo successore
                    newSet.add(current.getNode1());
                else
                    //senno quello predecessore
                    newSet.add(current.getNode2());
            }
        return newSet;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        return getAdjacentNodesOf(new GraphNode<>(label));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        if (i < 0)
            throw new IndexOutOfBoundsException("I valori devono essere maggiori o uguali a 0.");
        if (i > nodeCount())
            throw new IndexOutOfBoundsException("I valori devono essere minori di " + nodeCount() + ".");

        return getAdjacentNodesOf(this.getNode(i));
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException("node non può essere nullo.");
        if (!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Il nodo non appartiene al grafo.");
        //creo un nuovo set da ritornare
        Set<GraphEdge<L>> newSet = new HashSet<>();

        //Accedo alla riga del nodo
        for (GraphEdge<L> current : matrix.get(nodesIndex.get(node)))
            if (current != null)
                //aggiungo l'arco al set
                newSet.add(current);

        return newSet;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        return getEdgesOf(new GraphNode<>(label));
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        if (i < 0)
            throw new IndexOutOfBoundsException("Valore minore di 0.");
        if (i > nodeCount())
            throw new IndexOutOfBoundsException("Valore maggiore dei nodi presenti: " + nodeCount() + ".");

        Set<GraphEdge<L>> newSet = new HashSet<>();
        //Accedo alla riga del nodo
        for (GraphEdge<L> current : matrix.get(i))
            if (current!=null)
                //aggiungo l'arco al set
                newSet.add(current);

        return newSet;
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        Set<GraphEdge<L>> newSet = new HashSet<>();

        //scorro all'interno della matrice ed aggiungo al nuovo set tutti gli archi presenti
        for (int index = 0; index < nodeCount(); index++)
            for (GraphEdge<L> current : matrix.get(index))
                if (current != null)

                    newSet.add(current);

        return newSet;
    }
    }



