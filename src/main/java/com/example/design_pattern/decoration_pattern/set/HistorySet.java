package com.example.design_pattern.decoration_pattern.set;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author L494264Tt@outlook.com
 * @date 2025/8/28 21:50
 */

public class HistorySet<E> implements Set<E> {

    private List<E> removeList = new ArrayList<>();

    private final Set<E> hashSet;

    public HistorySet(Set<E> delegate) {
        this.hashSet = delegate;
    }

    @Override
    public int size() {
        return hashSet.size();
    }

    @Override
    public boolean isEmpty() {
        return hashSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return hashSet.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return hashSet.iterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return hashSet.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return hashSet.add(e);
    }

    @Override
    public boolean remove(Object o) {
        if (hashSet.remove(o)) {
            removeList.add((E) o);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public String toString() {
        return "HistorySet{" +
                "removeList=" + removeList +
                ", hashSet=" + hashSet +
                '}';
    }
}
