package code.ss.demo1.mq1;

import java.util.*;

public class Answer {

    private final TreeMap<Integer, Integer> treemap;

    public static void main(String[] args) {
        int n = 20;
        int[] arr = new int[n];
        for (int i = 0; i < n - 1; i++) {
            arr[i] = i;
        }
        Answer answer = new Answer(arr);
        Random r = new Random();
        HashSet<Integer> resultSet = new HashSet<>();
        for (int i = 0; i < n - 1; i++) {
            resultSet.add(answer.popByTreeMap(r.nextInt(answer.treemap.size())));
        }
        assert resultSet.size() == 20;


    }

    int[] src;
    public int size;
    List<Integer> list = new ArrayList<Integer>();

    public Answer(int[] src) {
        this.src = src;
        this.size = src.length - 1;
        treemap = new TreeMap<>();
        for (int i = 0; i < src.length; i++) {
            treemap.put(i, i);
        }
    }

    public int pop(int m) {
        if (m > this.size) {
            throw new NoSuchElementException();
        }
        int result = src[m];
        for (int i = m; i < size; i++) {
            src[i] = src[i + 1];
        }
        this.size--;
        return result;
    }


    public int popByArrayList(int m) {
        if (m > list.size()) {
            throw new IndexOutOfBoundsException();
        }
        Integer remove = list.remove(m);
        return remove;
    }

    public int popByTreeMap(int m) {
        Map.Entry<Integer, Integer> entry = treemap.ceilingEntry(m);
        System.out.println(treemap.size());
        return entry.getValue();
    }


}