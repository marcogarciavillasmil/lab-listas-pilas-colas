package stack;

import java.util.NoSuchElementException;

public class CircularArrayStack<T> implements MyStack<T> {

    private Object[] data;
    private int base;
    private int size;

    public CircularArrayStack() {
        this(16);
    }

    public CircularArrayStack(int initialCapacity) {
        data = new Object[Math.max(1, initialCapacity)];
        base = 0;
        size = 0;
    }

    private int pos(int k) {
        return (base + k) % data.length;
    }

    private void grow() {
        Object[] nuevo = new Object[data.length * 2];
        for (int i = 0; i < size; i++) nuevo[i] = data[pos(i)];
        data = nuevo;
        base = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() { return size; }

    @Override
    public void push(T x) {
        if (size == data.length) grow();
        data[pos(size)] = x;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T pop()
    {
        if (size == 0) throw new NoSuchElementException("pila vacía");
        int i = pos(size - 1);
        T val = (T) data[i];
        data[i] = null;
        size--;
        return val;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T peek() {
        if (size == 0) throw new NoSuchElementException("pila vacía");
        return (T) data[pos(size - 1)];
    }

    @Override
    public void delete(T n) {
        int idx = -1;
        for (int i = size - 1; i >= 0; i--) {
            Object v = data[pos(i)];
            if (v == null ? n == null : v.equals(n)) {
                idx = i;
                break;
            }
        }
        if (idx == -1) return;
        for (int i = idx; i < size - 1; i++) data[pos(i)] = data[pos(i + 1)];
        data[pos(size - 1)] = null;
        size--;
    }
}
