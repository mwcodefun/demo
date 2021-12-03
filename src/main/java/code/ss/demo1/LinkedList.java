package code.ss.demo1;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

public class LinkedList<T> implements Iterable<T> {

    Node<T> head;
    Node<T> tail;
    int size;
    int modCount;

    public static void main(String[] args) {
        LinkedList<String> stringLinkedList = new LinkedList<>();
        stringLinkedList.add("123");
        stringLinkedList.remove(0);

        for (String s : stringLinkedList) {
            System.out.println(s);
        }

    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedIterator();
    }

    static class Node<T> {
        T value;

        public Node(T value) {
            this.value = value;
        }

        Node prev;
        Node next;
    }

    class LinkedIterator implements Iterator<T> {

        private Node<T> cur;
        private int expectModCount;

        public LinkedIterator() {
            this.cur = head;
            this.expectModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            return cur != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T value = cur.value;
            cur = cur.next;
            return value;
        }

        @Override
        public void remove() {
            if (cur != null) {
                Node<T> t = head;
                while (t != null) {
                    if (t == cur) {

                    }
                    t = t.next;

                }
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            Iterator.super.forEachRemaining(action);
        }
    }

    public void add(T value) {
        Node<T> tNode = new Node<>(value);
        if (head == null) {
            this.head = tNode;
            this.tail = tNode;
        } else {
            this.tail.next = tNode;
            tNode.prev = tail;
            this.tail = tNode;
        }
        size++;
        modCount++;
    }

    public void removeNode(Node<T> node) {
        if (node == head) {
            if (node.next == null) {
                this.tail = null;
                this.head = null;
                return;
            } else {
                Node<T> n = node.next;
                head = n;
                n.prev = null;
            }
        }else{
            Node p = node.prev;
            p.next = node.next;
            node.next.prev = p;
        }

        size--;
        modCount++;

    }

    public void remove(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> t = head;
        for (int i = 1; i < index; i++) {
            t = head.next;
        }
        Node p = t.prev;
        Node next = t.next;

        if (next == null) {
            tail = p;
            return;
        }


        p.next = next;
        next.prev = p;
        size--;
        modCount++;
    }

    public T get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> cur = head;
        for (int i = 0; i < index; i++) {
            cur = head.next;
        }
        return cur.value;
    }

}
