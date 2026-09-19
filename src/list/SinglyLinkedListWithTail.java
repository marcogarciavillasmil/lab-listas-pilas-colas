package list;

import java.util.NoSuchElementException;

// misma idea que la anterior pero ahora guardo tail. Con eso pushBack y
// topBack quedan en O(1). popBack sigue en O(n) porque los nodos no tienen
// puntero al anterior (es simple, no doble) y para actualizar tail hay que
// recorrer buscando quien apunta al ultimo nodo.
public class SinglyLinkedListWithTail<T> implements MyList<T> {

    private Node<T> head;
    private Node<T> tail;
    private int count;

    @Override
    public Node<T> pushFront(T value) {
        Node<T> node = new Node<>(value);
        node.next = head;
        head = node;
        if (tail == null) tail = node;
        count++;
        return node;
    }

    @Override
    public Node<T> pushBack(T value) {
        Node<T> node = new Node<>(value);
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        count++;
        return node;
    }

    @Override
    public T popFront() {
        if (head == null) throw new NoSuchElementException("lista vacía");
        Node<T> old = head;
        head = head.next;
        if (head == null) tail = null;
        count--;
        return old.value;
    }

    @Override
    public T popBack() {
        if (head == null) throw new NoSuchElementException("lista vacía");
        T value;
        if (head == tail) {
            value = head.value;
            head = tail = null;
        } else {
            Node<T> prev = head;
            while (prev.next != tail) prev = prev.next;
            value = tail.value;
            prev.next = null;
            tail = prev;
        }
        count--;
        return value;
    }

    @Override
    public Node<T> find(T value) {
        Node<T> cur = head;
        while (cur != null) {
            if (cur.value.equals(value)) return cur;
            cur = cur.next;
        }
        return null;
    }

    @Override
    public void erase(Node<T> node) {
        if (node == null || head == null) return;
        if (head == node) {
            head = head.next;
            if (head == null) tail = null;
            count--;
            return;
        }
        Node<T> prev = head;
        while (prev.next != null && prev.next != node) prev = prev.next;
        if (prev.next == node) {
            prev.next = node.next;
            if (node == tail) tail = prev;
            count--;
        }
    }

    @Override
    public Node<T> addBefore(Node<T> node, T value) {
        if (node == null) return null;
        if (node == head) return pushFront(value);
        Node<T> prev = head;
        while (prev != null && prev.next != node) prev = prev.next;
        if (prev == null) return null;
        Node<T> newNode = new Node<>(value);
        newNode.next = node;
        prev.next = newNode;
        count++;
        return newNode;
    }

    @Override
    public Node<T> addAfter(Node<T> node, T value) {
        if (node == null) return null;
        Node<T> newNode = new Node<>(value);
        newNode.next = node.next;
        node.next = newNode;
        if (node == tail) tail = newNode;
        count++;
        return newNode;
    }

    @Override
    public boolean empty() {
        return head == null;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public T topFront() {
        if (head == null) throw new NoSuchElementException("lista vacía");
        return head.value;
    }

    @Override
    public T topBack() {
        if (tail == null) throw new NoSuchElementException("lista vacía");
        return tail.value;
    }
}
