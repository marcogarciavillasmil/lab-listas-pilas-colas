package stack;

import java.util.NoSuchElementException;

public class ArrayStack<T> implements MyStack<T> {

    private Object[] data;
    private int size;

    public ArrayStack() {
        this(16);
    }

    public ArrayStack(int initialCapacity) {
        data = new Object[Math.max(1, initialCapacity)];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() { return size; }

    private void grow() {
        Object[] bigger = new Object[data.length * 2];
        System.arraycopy(data, 0, bigger, 0, size);
        data = bigger;
    }

    @Override
    public void push(T x)
    {
        if (size == data.length) grow();
        data[size++] = x;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T pop() {
        if (size == 0) throw new NoSuchElementException("pila vacía");
        T val = (T) data[--size];
        data[size] = null;
        return val;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T peek() {
        if (size == 0) throw new NoSuchElementException("pila vacía");
        return (T) data[size - 1];
    }

    @Override
    public void delete(T n) {
        int idx = -1;
        for (int i = size - 1; i >= 0; i--) {
            if (data[i] == null ? n == null : data[i].equals(n)) {
                idx = i;
                break;
            }
        }
        if (idx == -1) return;
        for (int i = idx; i < size - 1; i++)
            data[i] = data[i + 1];
        data[size - 1] = null;
        size--;
    }
}
