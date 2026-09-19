package stack;

import java.util.NoSuchElementException;

// misma pila pero ahora sobre un buffer circular (front + size, indices
// modulo capacidad) en vez de un arreglo "plano". Para una pila esto no
// cambia la complejidad frente a ArrayStack -- push/pop siguen en un solo
// extremo -- lo implemento para comparar con Queue (ahi si hace diferencia)
// y porque el enunciado lo pide explicitamente.
public class CircularArrayStack<T> implements MyStack<T> {

    private Object[] data;
    private int front; // índice del elemento más antiguo (fondo de la pila)
    private int size;

    public CircularArrayStack() {
        this(16);
    }

    public CircularArrayStack(int initialCapacity) {
        data = new Object[Math.max(1, initialCapacity)];
        front = 0;
        size = 0;
    }

    private int indexOf(int logicalPos) {
        return (front + logicalPos) % data.length;
    }

    private void grow() {
        Object[] bigger = new Object[data.length * 2];
        for (int i = 0; i < size; i++) bigger[i] = data[indexOf(i)];
        data = bigger;
        front = 0;
    }

    @Override
    public void push(T x) {
        if (size == data.length) grow();
        data[indexOf(size)] = x;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T pop() {
        if (size == 0) throw new NoSuchElementException("pila vacía");
        int idx = indexOf(size - 1);
        T value = (T) data[idx];
        data[idx] = null;
        size--;
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T peek() {
        if (size == 0) throw new NoSuchElementException("pila vacía");
        return (T) data[indexOf(size - 1)];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(T n) {
        int logicalIdx = -1;
        for (int i = size - 1; i >= 0; i--) {
            Object v = data[indexOf(i)];
            if (v == null ? n == null : v.equals(n)) {
                logicalIdx = i;
                break;
            }
        }
        if (logicalIdx == -1) return;
        for (int i = logicalIdx; i < size - 1; i++) {
            data[indexOf(i)] = data[indexOf(i + 1)];
        }
        data[indexOf(size - 1)] = null;
        size--;
    }
}
