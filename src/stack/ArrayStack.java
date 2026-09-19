package stack;

import java.util.NoSuchElementException;

// pila con arreglo dinamico tipo ArrayList: cuando se llena, duplico el
// arreglo. El tope siempre es la posicion size-1, entonces push/pop no
// mueven nada, son O(1) amortizado (el resize es O(n) pero pasa poco).
// delete si es O(n) porque busca y despues corre los elementos.
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

    private void grow() {
        Object[] bigger = new Object[data.length * 2];
        System.arraycopy(data, 0, bigger, 0, size);
        data = bigger;
    }

    @Override
    public void push(T x) {
        if (size == data.length) grow();
        data[size++] = x;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T pop() {
        if (size == 0) throw new NoSuchElementException("pila vacía");
        T value = (T) data[--size];
        data[size] = null;
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T peek() {
        if (size == 0) throw new NoSuchElementException("pila vacía");
        return (T) data[size - 1];
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
        for (int i = size - 1; i >= 0; i--) { // desde el tope
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
