package list;

// nodo generico que usan las 4 listas. lo dejo publico (con getValue) porque
// find() necesita devolver algo que despues sirva para erase/addBefore/addAfter
public class Node<T> {
    T value;
    Node<T> next;
    Node<T> prev; // solo se usa en las listas doblemente enlazadas

    public Node(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
