package list;

import java.util.NoSuchElementException;

// lista simple, solo con head (sin tail). Todo lo que toque el final de la
// lista (pushBack, popBack, topBack) o necesite el nodo anterior a uno dado
// (erase, addBefore) tiene que recorrerla desde el principio -> O(n).
// addAfter si es O(1) porque ahi si tengo el nodo de referencia directo.
public class SinglyLinkedListNoTail<T> implements MyList<T> {

    private Node<T> head;
    private int count;

    @Override
    public Node<T> pushFront(T value) {
        Node<T> node = new Node<>(value);
        node.next = head;
        head = node;
        count++;
        return node;
    }

    @Override
    public Node<T> pushBack(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = node;
        } else {
            Node<T> cur = head;
            while (cur.next != null) cur = cur.next;
            cur.next = node;
        }
        count++;
        return node;
    }

    @Override
    public T popFront() {
        if (head == null) throw new NoSuchElementException("lista vacía");
        Node<T> old = head;
        head = head.next;
        count--;
        return old.value;
    }

    @Override
    public T popBack() {
        if (head == null) throw new NoSuchElementException("lista vacía");
        T value;
        if (head.next == null) {
            value = head.value;
            head = null;
        } else {
            Node<T> prev = head;
            while (prev.next.next != null) prev = prev.next;
            value = prev.next.value;
            prev.next = null;
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
            count--;
            return;
        }
        Node<T> prev = head;
        while (prev.next != null && prev.next != node) prev = prev.next;
        if (prev.next == node) {
            prev.next = node.next;
            count--;
        }
    }

    @Override
    public Node<T> addBefore(Node<T> node, T value) {
        if (node == null) return null;
        if (node == head) return pushFront(value);
        Node<T> prev = head;
        while (prev != null && prev.next != node) prev = prev.next;
        if (prev == null) return null; // node no pertenece a esta lista
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
        if (head == null) throw new NoSuchElementException("lista vacía");
        Node<T> cur = head;
        while (cur.next != null) cur = cur.next;
        return cur.value;
    }
}
