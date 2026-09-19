package stack;

// interfaz de pila, metodos minimos que pide el enunciado
public interface MyStack<T> {
    void push(T x);
    T pop();
    T peek();
    boolean isEmpty();
    int size();
    void delete(T n); // borra la primera ocurrencia de n buscando desde el tope
}
