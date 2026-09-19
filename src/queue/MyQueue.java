package queue;

// interfaz de cola, metodos minimos que pide el enunciado
public interface MyQueue<T> {
    void enqueue(T x);
    T dequeue();
    T front();
    boolean isEmpty();
    int size();
    void delete(T n); // borra la primera ocurrencia de n buscando desde el frente
}
