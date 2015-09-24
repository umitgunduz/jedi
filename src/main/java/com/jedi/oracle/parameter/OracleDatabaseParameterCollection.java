/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle.parameter;

import com.jedi.common.ParameterCollectionImpl;

import java.util.*;

/**
 * @author umitgunduz
 */
public final class OracleDatabaseParameterCollection extends ParameterCollectionImpl<OracleDatabaseParameter> {

    final List<OracleDatabaseParameter> parameters = new ArrayList<OracleDatabaseParameter>();

    @Override
    public boolean add(OracleDatabaseParameter e) {
        return parameters.add(e);
    }

    @Override
    public void add(int i, OracleDatabaseParameter e) {
        parameters.add(i, e);
    }

    @Override
    public boolean addAll(Collection<? extends OracleDatabaseParameter> clctn) {
        return parameters.addAll(clctn);
    }

    @Override
    public boolean addAll(int i, Collection<? extends OracleDatabaseParameter> clctn) {
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
    public OracleDatabaseParameter get(int i) {
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
    public Iterator<OracleDatabaseParameter> iterator() {
        return parameters.iterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return parameters.lastIndexOf(o);
    }

    @Override
    public ListIterator<OracleDatabaseParameter> listIterator() {
        return parameters.listIterator();
    }

    @Override
    public ListIterator<OracleDatabaseParameter> listIterator(int i) {
        return parameters.listIterator(i);
    }

    @Override
    public boolean remove(Object o) {
        return parameters.remove(o);
    }

    @Override
    public OracleDatabaseParameter remove(int i) {
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
    public OracleDatabaseParameter set(int i, OracleDatabaseParameter e) {
        return parameters.set(i, e);
    }

    @Override
    public int size() {
        return parameters.size();
    }

    @Override
    public List<OracleDatabaseParameter> subList(int i, int i1) {
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

