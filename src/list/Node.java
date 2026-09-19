package list;

// nodo que usan las 4 listas. getValue es publico porque find() devuelve
// el nodo entero, no solo el valor -- lo necesito para erase/addBefore/addAfter
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
