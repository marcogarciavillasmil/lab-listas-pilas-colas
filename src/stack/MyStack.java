package stack;

public interface MyStack<T> {
    boolean isEmpty();
    int size();
    void push(T x);
    T pop();
    T peek();
    void delete(T n);
}
