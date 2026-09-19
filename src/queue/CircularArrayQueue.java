package queue;

import java.util.NoSuchElementException;

// aca la cola guarda un indice front y calcula posiciones modulo la capacidad
// (buffer circular). ya no hay que correr nada en dequeue: se mueve el indice
// front y listo, O(1). este es el punto fuerte que comparo contra ArrayQueue
// en el informe (ahi si vale la pena el circular, a diferencia de la pila).
public class CircularArrayQueue<T> implements MyQueue<T> {

    private Object[] data;
    private int front;
    private int size;

    public CircularArrayQueue() {
        this(16);
    }

    public CircularArrayQueue(int initialCapacity) {
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
    public void enqueue(T x) {
        if (size == data.length) grow();
        data[indexOf(size)] = x;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (size == 0) throw new NoSuchElementException("cola vacía");
        T value = (T) data[front];
        data[front] = null;
        front = (front + 1) % data.length;
        size--;
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T front() {
        if (size == 0) throw new NoSuchElementException("cola vacía");
        return (T) data[front];
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
        for (int i = 0; i < size; i++) {
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
