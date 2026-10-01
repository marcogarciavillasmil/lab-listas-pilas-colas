package queue;

import java.util.NoSuchElementException;

public class CircularArrayQueue<T> implements MyQueue<T> {

    private Object[] data;
    private int head;
    private int size;

    public CircularArrayQueue() {
        this(16);
    }

    public CircularArrayQueue(int initialCapacity) {
        data = new Object[Math.max(1, initialCapacity)];
        head = 0;
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public int size() {
        return size;
    }

    private int pos(int k) {
        return (head + k) % data.length;
    }

    private void grow() {
        Object[] nuevo = new Object[data.length * 2];
        for (int i = 0; i < size; i++) nuevo[i] = data[pos(i)];
        data = nuevo;
        head = 0;
    }

    @Override
    public void enqueue(T x) {
        if (size == data.length) grow();
        data[pos(size)] = x;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T dequeue()
    {
        if (size == 0) throw new NoSuchElementException("cola vacía");
        T val = (T) data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        size--;
        return val;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T front() {
        if (size == 0) throw new NoSuchElementException("cola vacía");
        return (T) data[head];
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(T n) {
        int idx = -1;
        for (int i = 0; i < size; i++) {
            Object v = data[pos(i)];
            if (v == null ? n == null : v.equals(n)) {
                idx = i;
                break;
            }
        }
        if (idx == -1) return;
        for (int i = idx; i < size - 1; i++)
            data[pos(i)] = data[pos(i + 1)];
        data[pos(size - 1)] = null;
        size--;
    }
}
