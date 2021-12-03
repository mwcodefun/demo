package code.ss.demo1.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MwMap<K, V> implements Map<K, V> {

    MapEntry<K, V>[] table;

    public static final int INIT_SIZE = 8;
    private int capacity;
    int size;
    int threshold;
    public static void main(String[] args) {
        MwMap<SamehashString, String> map = new MwMap<>();
        int sameHash = 123;
        for (int i = 0; i < 1000; i++) {
            SamehashString key = new SamehashString("12" + i, sameHash );
            map.put(key, "12" + i);
        }
        System.out.println(map.size);
        System.out.println(map.table.length);
        MapEntry<SamehashString, String> entry = map.table[0];
        int entrySize = 0;
        while (entry != null) {
            entrySize++;
            entry = entry.next;
        }
        System.out.println(map.table[0]);
        System.out.println(entrySize);
    }

    static class MapEntry<K, V> {

        MapEntry next;

        public MapEntry getNext() {
            return next;
        }

        public void setNext(MapEntry next) {
            this.next = next;
        }

        MapEntry(K k, V v) {
            this.k = k;
            this.v = v;
        }


        K k;
        V v;


    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    private int sizeOfEntry(MapEntry entry) {
        int eSize = 0;
        while (entry.next != null) {
            entry = entry.next;
            eSize ++;
        }
        return eSize;
    }

    @Override
    public V get(Object key) {

        int hash = hash(key);
        int index = hash % table.length;

        MapEntry<K, V> kvMapEntry = table[index];
        if (kvMapEntry == null) {
            return null;
        }
        if (kvMapEntry.k.equals(key)) {
            return kvMapEntry.v;
        }
        while (kvMapEntry.next != null) {
            if (kvMapEntry.k.equals(key)) {
                return kvMapEntry.v;
            }
            kvMapEntry = kvMapEntry.next;
        }
        return null;
    }

    private int hash(Object k) {
        return k.hashCode();
    }

    public void resize() {
        MapEntry<K, V>[] newtab = new MapEntry[table.length << 1];
        System.arraycopy(table, 0, newtab, 0, table.length);

        for (int i = 0; i < table.length; i++) {
            MapEntry<K, V> e = table[i];
            int newIndex = (hash(e.k) % newtab.length);
            if (i == newIndex) {
                continue;
            }else{

                newtab[newIndex] = e;
            }
        }
    }

    @Override
    public V put(K key, V value) {
        if (table == null) {
            initTable();
        }
        if (size >= threshold) {
            resize();
        }
        int index = hash(key) % table.length;
        MapEntry<K, V> mapEntry = table[index];
        MapEntry<K, V> kvMapEntry = new MapEntry<>(key, value);
        if (mapEntry == null) {
            table[index] = kvMapEntry;
            size++;
        } else {
            if (key.equals(mapEntry.k)) {
                V oldValue = mapEntry.v;
                mapEntry.v = value;
                return oldValue;
            }
            while (mapEntry.next != null) {
                mapEntry = mapEntry.next;
                if (key.equals(mapEntry.k)) {
                    V old = mapEntry.v;
                    mapEntry.v = value;
                    return old;
                }
            }
            mapEntry.next = kvMapEntry;
            size++;
        }
        return null;
    }

    private void initTable() {
        table = new MapEntry[INIT_SIZE];
        this.capacity = INIT_SIZE;
        this.threshold = (int) (capacity * 0.75);
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
}
