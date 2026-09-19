package list;

// Interfaz comun para las 4 versiones de lista enlazada que pide el enunciado:
// simple sin cola, simple con cola, doble sin cola y doble con cola.
// find() devuelve el Node en vez de un booleano/indice para poder usarlo
// despues en erase/addBefore/addAfter sin tener que volver a buscar.
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
