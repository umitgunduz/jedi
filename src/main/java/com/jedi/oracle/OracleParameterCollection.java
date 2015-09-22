/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle;

import com.jedi.common.DbParameterCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author EXT0104423
 */
public final class OracleParameterCollection extends DbParameterCollection<OracleParameter> {

    final List<OracleParameter> parameters = new ArrayList<OracleParameter>();

    @Override
    public boolean add(OracleParameter e) {
        return parameters.add(e);
    }

    @Override
    public void add(int i, OracleParameter e) {
        parameters.add(i, e);
    }

    @Override
    public boolean addAll(Collection<? extends OracleParameter> clctn) {
        return parameters.addAll(clctn);
    }

    @Override
    public boolean addAll(int i, Collection<? extends OracleParameter> clctn) {
        return parameters.addAll(i, clctn);
    }

    @Override
    public void clear() {
        parameters.clear();
    }

    @Override
    public boolean contains(Object o) {
        return parameters.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        return parameters.containsAll(clctn);
    }

    @Override
    public OracleParameter get(int i) {
        return parameters.get(i);
    }

    @Override
    public int indexOf(Object o) {
        return parameters.indexOf(o);
    }

    @Override
    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    @Override
    public Iterator<OracleParameter> iterator() {
        return parameters.iterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return parameters.lastIndexOf(o);
    }

    @Override
    public ListIterator<OracleParameter> listIterator() {
        return parameters.listIterator();
    }

    @Override
    public ListIterator<OracleParameter> listIterator(int i) {
        return parameters.listIterator(i);
    }

    @Override
    public boolean remove(Object o) {
        return parameters.remove(o);
    }

    @Override
    public OracleParameter remove(int i) {
        return parameters.remove(i);
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        return parameters.removeAll(clctn);
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        return parameters.retainAll(clctn);
    }

    @Override
    public OracleParameter set(int i, OracleParameter e) {
        return parameters.set(i, e);
    }

    @Override
    public int size() {
        return parameters.size();
    }

    @Override
    public List<OracleParameter> subList(int i, int i1) {
        return parameters.subList(i, i1);
    }

    @Override
    public Object[] toArray() {
        return parameters.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return parameters.toArray(ts);
    }

}
