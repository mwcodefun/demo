package code.ss.demo1.os;


import java.util.Iterator;
import java.util.LinkedList;

public class Malloc {

    LinkedList<Node> linkedList;

    public Malloc() {

        linkedList = new LinkedList<>();
        Node node = new Node();
        node.addr = 0L;
        node.size = 1024 * 1024 * 1024; //
        linkedList.add(node);
    }

    static class Node {
            Node prev;
            Node next;
            long addr;
            long size;
    }

    private long malloc(long size) {
        Iterator<Node> iterator = linkedList.iterator();
        Node target = new Node();
        if (size <= target.size) {
            Node node = new Node();
            node.addr = target.addr + size;
            node.size = target.size - size;
            linkedList.add(node);
            node.prev = target.prev;
            node.next = target.next;
        }
        return target.addr;
    }

    void free(long addr, long size) {
        Node node = new Node();
        node.addr = addr;
        node.size = size;
        //找到之前释放的node 起始位置，找到适合他的位置，在进行合并
    }

}
