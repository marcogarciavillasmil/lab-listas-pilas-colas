package list;

public class Node<T> {
    Node<T> next;
    Node<T> prev;
    T value;

    public Node(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
