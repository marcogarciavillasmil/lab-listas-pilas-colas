package queue;

import java.util.NoSuchElementException;

// cola con arreglo dinamico "de la forma obvia": enqueue mete al final
// (igual que ArrayStack.push), pero dequeue saca la posicion 0 y tiene que
// correr TODO lo demas una posicion a la izquierda. Por eso dequeue queda en
// O(n) -- esta es a proposito la version "mala" para compararla con la
// circular de abajo, que arregla justo este problema.
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
        Object[] bigger = new Object[data.length * 2];
        System.arraycopy(data, 0, bigger, 0, size);
        data = bigger;
    }

    @Override
    public void enqueue(T x) {
        if (size == data.length) grow();
        data[size++] = x;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (size == 0) throw new NoSuchElementException("cola vacía");
        T value = (T) data[0];
        for (int i = 0; i < size - 1; i++) data[i] = data[i + 1];
        data[size - 1] = null;
        size--;
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T front() {
        if (size == 0) throw new NoSuchElementException("cola vacía");
        return (T) data[0];
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
        data[size - 1] = null;
        size--;
    }
}
