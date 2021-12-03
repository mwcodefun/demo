package code.ss.demo1.collections;

import java.util.Objects;

public class SamehashString {

    private final String value;

    private final int hash;


    public SamehashString(String value, int hash) {
        this.value = value;
        this.hash = hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SamehashString that = (SamehashString) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
