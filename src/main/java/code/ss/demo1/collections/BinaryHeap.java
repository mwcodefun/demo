package code.ss.demo1.collections;

public class BinaryHeap<Key extends Comparable<Key>> {

    private final Key[] heap;

    private int N = 0;

    public BinaryHeap(int capacity) {
        heap = (Key[]) new Comparable[capacity + 1];
    }

    public Key max() {
        return heap[1];
    }

    public void insert(Key key) {
        N ++;
        heap[N] = key;
        swim(N);
    }

    public Key delMax() {
        Key max = heap[1];
        exch(1, N);
        heap[N] = null;
        N --;
        sink(1);
        return max;
    }

    private void swim(int k) {
        while (k > 1 && less(parent(k), k)) {
            int parent = parent(k);
            exch(parent, k);
            k = parent;
        }
    }

    /**
     * sink node
     * if k is less than child  need sink
     * and should exchange with child which larger
     * if k is bigger than larger one should stop sink
     *
     *  exch(k,larger(left(k),right(k))
     *
     *
     *
     * @param k
     */
    private void sink(int k) {
        while (left(k) > N && less(k, left(k))) {
            int older = left(k);
            if (right(k) < N && less(older, right(k))) {
                older = right(k);
            }
            if (less(older,k)) break;
            exch(older, k);
            k = older;
        }
    }


    private void exch(int i, int j) {
        Key temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private int parent(int k) {
        return k / 2;
    }

    private int left(int k) {
        return k * 2;
    }

    private int right(int k) {
        return left(k) + 1;
    }

    private boolean less(int i, int j) {
        return heap[i].compareTo(heap[j]) < 0;
    }

}
