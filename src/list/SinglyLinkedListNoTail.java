package list;

import java.util.NoSuchElementException;

public class SinglyLinkedListNoTail<T> implements MyList<T> {

    private int count;
    private Node<T> head;

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean empty() { return head == null; }

    @Override
    public Node<T> pushFront(T value)
    {
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
            while (cur.next != null)
                cur = cur.next;
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
    public T popBack()
    {
        if (head == null) throw new NoSuchElementException("lista vacía");
        T val;
        if (head.next == null) {
            val = head.value;
            head = null;
        }
        else {
            Node<T> prev = head;
            while (prev.next.next != null) { prev = prev.next; }
            val = prev.next.value;
            prev.next = null;
        }
        count--;
        return val;
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

    @Override
    public Node<T> find(T value) {
        Node<T> cur = head;
        while (cur != null) {
            if (cur.value.equals(value)) {
                return cur;
            }
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
        Node<T> p = head;
        while (p.next != null && p.next != node)
            p = p.next;
        if (p.next == node) {
            p.next = node.next;
            count--;
        }
    }

    @Override
    public Node<T> addBefore(Node<T> node, T value) {
        if (node == null) return null;
        if (node == head) return pushFront(value);
        Node<T> p = head;
        while (p != null && p.next != node) p = p.next;
        if (p == null) return null;
        Node<T> nn = new Node<>(value);
        nn.next = node;
        p.next = nn;
        count++;
        return nn;
    }

    @Override
    public Node<T> addAfter(Node<T> node, T value)
    {
        if (node == null) return null;
        Node<T> nn = new Node<>(value);
        nn.next = node.next;
        node.next = nn;
        count++;
        return nn;
    }
}
