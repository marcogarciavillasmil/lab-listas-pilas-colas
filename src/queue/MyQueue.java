package queue;

public interface MyQueue<T> {
    boolean isEmpty();
    int size();
    void enqueue(T x);
    T dequeue();
    T front();
    void delete(T n);
}
