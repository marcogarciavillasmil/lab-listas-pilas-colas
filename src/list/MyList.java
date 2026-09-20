package list;

public interface MyList<T> {

    int size();

    boolean empty();

    Node<T> pushFront(T value);
    Node<T> pushBack(T value);

    T popFront();
    T popBack();

    T topFront();
    T topBack();

    Node<T> find(T value);
    void erase(Node<T> node);

    Node<T> addBefore(Node<T> node, T value);
    Node<T> addAfter(Node<T> node, T value);
}
