package queue;

// interfaz de cola, lo minimo que pide el enunciado (igual que MyStack pero para cola)
public interface MyQueue<T> {
    void enqueue(T x);
    T dequeue();
    T front();
    boolean isEmpty();
    int size();
    void delete(T n); // borra la primera ocurrencia de n buscando desde el frente
}
