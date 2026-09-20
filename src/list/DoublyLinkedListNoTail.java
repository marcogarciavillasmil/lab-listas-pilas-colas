package list;

import java.util.NoSuchElementException;

public class DoublyLinkedListNoTail<T> implements MyList<T> {

    private Node<T> head;
    private int count;

    private Node<T> lastNode() {
        Node<T> cur = head;
        while (cur != null && cur.next != null)
            cur = cur.next;
        return cur;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean empty() { return head == null; }

    @Override
    public Node<T> pushFront(T value) {
        Node<T> node = new Node<>(value);
        node.next = head;
        if (head != null) head.prev = node;
        head = node;
        count++;
        return node;
    }

    @Override
    public Node<T> pushBack(T value)
    {
        Node<T> last = lastNode();
        Node<T> node = new Node<>(value);
        if (last == null) {
            head = node;
        } else {
            last.next = node;
            node.prev = last;
        }
        count++;
        return node;
    }

    @Override
    public T popFront() {
        if (head == null) throw new NoSuchElementException("lista vacía");
        Node<T> old = head;
        head = head.next;
        if (head != null) head.prev = null;
        count--;
        return old.value;
    }

    @Override
    public T popBack() {
        Node<T> last = lastNode();
        if (last == null) throw new NoSuchElementException("lista vacía");
        T val = last.value;
        erase(last);
        return val;
    }

    @Override
    public T topFront() {
        if (head == null) throw new NoSuchElementException("lista vacía");
        return head.value;
    }

    @Override
    public T topBack()
    {
        Node<T> last = lastNode();
        if (last == null) throw new NoSuchElementException("lista vacía");
        return last.value;
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
        if (node == null) return;
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;
        if (node.next != null) node.next.prev = node.prev;
        count--;
    }

    @Override
    public Node<T> addBefore(Node<T> node, T value) {
        if (node == null) return null;
        if (node == head) return pushFront(value);
        Node<T> nn = new Node<>(value);
        Node<T> prev = node.prev;
        nn.prev = prev;
        nn.next = node;
        prev.next = nn;
        node.prev = nn;
        count++;
        return nn;
    }

    @Override
    public Node<T> addAfter(Node<T> node, T value) {
        if (node == null) return null;
        Node<T> nn = new Node<>(value);
        Node<T> next = node.next;
        nn.prev = node;
        nn.next = next;
        node.next = nn;
        if (next != null) next.prev = nn;
        count++;
        return nn;
    }
}
