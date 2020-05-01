/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.util.datatypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import toxi.math.MathUtils;

/**
 *
 * @author tux
 * @param <T>
 */
public class GenericSet<T> implements Iterable<T> {

    /**
     *
     */
    protected ArrayList<T> items;

    /**
     *
     */
    protected int currID = -1;

    /**
     *
     */
    protected T current;

    /**
     *
     */
    protected Random random = new Random();

    /**
     *
     * @param items
     */
    public GenericSet(Collection<T> items) {
        this.items = new ArrayList<>(items);
        pickRandom();
    }

    /**
     *
     * @param obj
     */
    public GenericSet(T... obj) {
        items = new ArrayList<>(obj.length);
        items.addAll(Arrays.asList(obj));
        if (items.size() > 0) {
            pickRandom();
        }
    }

    /**
     *
     * @param obj
     * @return
     */
    public boolean add(T obj) {
        boolean isAdded = items.add(obj);
        if (items.size() == 1) {
            pickRandom();
        }
        return isAdded;
    }

    /**
     *
     * @param coll
     * @return
     */
    public boolean addAll(Collection<T> coll) {
        return items.addAll(coll);
    }

    /**
     *
     */
    public void clear() {
        items.clear();
    }

    /**
     *
     * @param obj
     * @return
     */
    public boolean contains(T obj) {
        return items.contains(obj);
    }

    /**
     *
     * @return
     */
    public GenericSet<T> copy() {
        GenericSet<T> set = new GenericSet<>(items);
        set.current = current;
        set.currID = currID;
        set.random = random;
        return set;
    }

    /**
     *
     * @return
     */
    public T getCurrent() {
        return current;
    }

    /**
     *
     * @return
     */
    public ArrayList<T> getItems() {
        return items;
    }

    /**
     *
     * @return
     */
    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }

    /**
     *
     * @return
     */
    public final T pickRandom() {
        currID = MathUtils.random(random, items.size());
        current = items.get(currID);
        return current;
    }

    /**
     *
     * @return
     */
    public T pickRandomUnique() {
        int size = items.size();
        if (size > 1) {
            int newID = currID;
            while (newID == currID) {
                newID = MathUtils.random(random, size);
            }
            currID = newID;
        } else {
            currID = 0;
        }
        current = items.get(currID);
        return current;
    }

    /**
     *
     * @param seed
     * @return
     */
    public GenericSet<T> seed(long seed) {
        random.setSeed(seed);
        return this;
    }

    /**
     *
     * @param rnd
     */
    public void setRandom(Random rnd) {
        random = rnd;
    }

    /**
     *
     * @return
     */
    public int size() {
        return items.size();
    }
}
