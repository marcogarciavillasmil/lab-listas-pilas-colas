package list;

// interfaz comun a las 4 listas (simple/doble x con-sin cola).
// find() devuelve el Node y no un bool/indice: asi despues lo puedo pasar
// directo a erase/addBefore/addAfter sin tener que buscar dos veces.
public interface MyList<T> {

    Node<T> pushFront(T value);

    Node<T> pushBack(T value);

    T popFront();

    T popBack();

    Node<T> find(T value);

    void erase(Node<T> node);

    Node<T> addBefore(Node<T> node, T value);

    Node<T> addAfter(Node<T> node, T value);

    boolean empty();

    int size();

    T topFront();

    T topBack();
}
