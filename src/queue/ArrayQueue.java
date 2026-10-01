package queue;

import java.util.NoSuchElementException;

public class ArrayQueue<T> implements MyQueue<T> {

    private Object[] data;
    private int size;

    public ArrayQueue() {
        this(16);
    }

    public ArrayQueue(int initialCapacity) {
        data = new Object[Math.max(1, initialCapacity)];
        size = 0;
    }

    private void grow() {
        Object[] nuevo = new Object[data.length * 2];
        System.arraycopy(data, 0, nuevo, 0, size);
        data = nuevo;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void enqueue(T x)
    {
        if (size == data.length) grow();
        data[size++] = x;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (size == 0) throw new NoSuchElementException("cola vacía");
        T primero = (T) data[0];
        for (int i = 1; i < size; i++) data[i - 1] = data[i];
        data[--size] = null;
        return primero;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T front() {
        if (size == 0) throw new NoSuchElementException("cola vacía");
        return (T) data[0];
    }

    @Override
    public void delete(T n) {
        int idx = -1;
        for (int i = 0; i < size; i++) {
            if (data[i] == null ? n == null : data[i].equals(n)) {
                idx = i;
                break;
            }
        }
        if (idx == -1) return;
        for (int i = idx; i < size - 1; i++) data[i] = data[i + 1];
        data[--size] = null;
    }
}
