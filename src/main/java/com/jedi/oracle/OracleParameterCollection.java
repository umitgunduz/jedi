package com.jedi.oracle;

import java.util.*;

/**
 * Created by EXT0104423 on 9/29/2015.
 */
public class OracleParameterCollection implements List<OracleParameter> {
    final private List<OracleParameter> data;

    public OracleParameterCollection() {
        this.data = new ArrayList<OracleParameter>();
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.data.contains(o);
    }

    @Override
    public Iterator<OracleParameter> iterator() {
        return data.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.data.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return this.data.toArray(ts);
    }

    @Override
    public boolean add(OracleParameter oracleParameter) {
        return this.data.add(oracleParameter);
    }

    @Override
    public boolean remove(Object o) {
        return this.data.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return this.data.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends OracleParameter> collection) {
        return this.data.addAll(collection);
    }

    @Override
    public boolean addAll(int i, Collection<? extends OracleParameter> collection) {
        return this.data.addAll(i, collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return this.data.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return this.data.retainAll(collection);
    }

    @Override
    public void clear() {
        this.data.clear();
    }

    @Override
    public OracleParameter get(int i) {
        return this.data.get(i);
    }

    @Override
    public OracleParameter set(int i, OracleParameter oracleParameter) {
        return this.data.set(i, oracleParameter);
    }

    @Override
    public void add(int i, OracleParameter oracleParameter) {
        this.data.add(i, oracleParameter);
    }

    @Override
    public OracleParameter remove(int i) {
        return this.data.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return this.data.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.data.lastIndexOf(o);
    }

    @Override
    public ListIterator<OracleParameter> listIterator() {
        return this.data.listIterator();
    }

    @Override
    public ListIterator<OracleParameter> listIterator(int i) {
        return this.data.listIterator(i);
    }

    @Override
    public List<OracleParameter> subList(int i, int i1) {
        return this.data.subList(i, i1);
    }
}
