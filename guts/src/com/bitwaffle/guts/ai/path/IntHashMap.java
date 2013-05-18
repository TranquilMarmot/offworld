/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Note: originally released under the GNU LGPL v2.1, 
 * but rereleased by the original author under the ASF license (above).
 */

/*
 * I made a few changes to this class, which was originally from Apache Commons
 * I got rid of some serialization stuff and changed comments and some curly braces
 * to match the style of my code.
 * 
 * TranquilMarmot
 */

package com.bitwaffle.guts.ai.path;

/**
 * <p>A hash map that uses primitive ints for the key rather than objects.</p>
 *
 * Note from TranquilMarmot:
 * I chose to use this over regular HashMap since I will only be using integers
 * to reference nodes and java.util.HashMap does extra unnecessary things.
 *
 * @author Justin Couch
 * @author Alex Chaffee (alex@apache.org)
 * @author Stephen Colebourne
 * @author TranquilMarmot
 * @since 2.0
 * @version $Revision: 561230 $
 * @see java.util.HashMap
 */
public class IntHashMap {

    /** The hash table data. */
    private Entry table[];

    /** The total number of entries in the hash table. */
    private int count;

    /**
     * The table is rehashed when its size exceeds this threshold.
     * The value of this field is (int)(capacity * loadFactor).
     */
    private int threshold;

    /** The load factor for the hashtable. */
    private float loadFactor;

    /** Inner class that acts as a data structure to create a new entry in the table. */
    private static class Entry {
        protected int hash;
        protected Object value;
        protected Entry next;

        /** Create a new entry with the given values. */
        protected Entry(int hash, Object value, Entry next) {
            this.hash = hash;
            this.value = value;
            this.next = next;
        }
    }

    /**
     * Constructs a new, empty hashtable with a default capacity and load
     * factor, which is <code>20</code> and <code>0.75</code> respectively.
     */
    public IntHashMap() {
        this(20, 0.75f);
    }

    /**
     * Constructs a new, empty hashtable with the specified initial capacity
     * and default load factor, which is <code>0.75</code>.
     *
     * @param  initialCapacity the initial capacity of the hashtable.
     * @throws IllegalArgumentException if the initial capacity is less than zero.
     */
    public IntHashMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    /**
     * Constructs a new, empty hashtable with the specified initial
     * capacity and the specified load factor.
     *
     * @param initialCapacity the initial capacity of the hashtable.
     * @param loadFactor the load factor of the hashtable.
     * @throws IllegalArgumentException  if the initial capacity is less than zero, or if the load factor is nonpositive.
     */
    public IntHashMap(int initialCapacity, float loadFactor) {
        super();
        if (initialCapacity < 0) 
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        if (loadFactor <= 0) 
            throw new IllegalArgumentException("Illegal Load: " + loadFactor);
        
        if (initialCapacity == 0)
            initialCapacity = 1;

        this.loadFactor = loadFactor;
        table = new Entry[initialCapacity];
        threshold = (int) (initialCapacity * loadFactor);
    }

    /** @return  the number of keys in this hashtable. */
    public int size() { return count; }

    /** @return  true if this hashtable maps no keys to values, false otherwise. */
    public boolean isEmpty() { return count == 0; }

    /**
     * Tests if some key maps into the specified value in this hashtable.
     * This operation is more expensive than the <code>containsKey</code>
     * method.
     *
     * Note that this method is identical in functionality to containsValue,
     * (which is part of the Map interface in the collections framework).
     */
    public boolean contains(Object value) {
        if (value == null)
            throw new NullPointerException();

        Entry tab[] = table;
        for (int i = tab.length; i-- > 0;) 
            for (Entry e = tab[i]; e != null; e = e.next)
                if (e.value.equals(value))
                    return true;
        
        return false;
    }

    /**
     * Returns true if this HashMap maps one or more keys to this value.
     *
     * Note that this method is identical in functionality to contains
     * (which predates the Map interface).
     */
    public boolean containsValue(Object value) { return contains(value); }

    /** Tests if the specified object is a key in this hashtable */
    public boolean containsKey(int key) {
        Entry tab[] = table;
        int hash = key;
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash)
                return true;
        }
        return false;
    }

    /** Returns the value to which the specified key is mapped in this map. */
    public Object get(int key) {
        Entry tab[] = table;
        int hash = key;
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index]; e != null; e = e.next)
            if (e.hash == hash)
                return e.value;
        return null;
    }

    /**
     * Increases the capacity of and internally reorganizes this
     * hashtable, in order to accommodate and access its entries more
     * efficiently.
     *
     * This method is called automatically when the number of keys
     * in the hashtable exceeds this hashtable's capacity and load
     * factor.
     */
    protected void rehash() {
        int oldCapacity = table.length;
        Entry oldMap[] = table;

        int newCapacity = oldCapacity * 2 + 1;
        Entry newMap[] = new Entry[newCapacity];

        threshold = (int) (newCapacity * loadFactor);
        table = newMap;

        for (int i = oldCapacity; i-- > 0;) {
            for (Entry old = oldMap[i]; old != null;) {
                Entry e = old;
                old = old.next;

                int index = (e.hash & 0x7FFFFFFF) % newCapacity;
                e.next = newMap[index];
                newMap[index] = e;
            }
        }
    }

    /**
     * Maps the specified key to the specified value in this hashtable.
     * The key cannot be null.
     * @return the previous value of the specified key in this hashtable,
     *         or <code>null</code> if it did not have one.
     */
    public Object put(int key, Object value) {
        // Makes sure the key is not already in the hashtable.
        Entry tab[] = table;
        int hash = key;
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash) {
                Object old = e.value;
                e.value = value;
                return old;
            }
        }

        if (count >= threshold) {
            // Rehash the table if the threshold is exceeded
            rehash();

            tab = table;
            index = (hash & 0x7FFFFFFF) % tab.length;
        }

        // Creates the new entry.
        Entry e = new Entry(hash, value, tab[index]);
        tab[index] = e;
        count++;
        return null;
    }

    /**
     * Removes the key (and its corresponding value) from this hashtable.
     * This method does nothing if the key is not present in the hashtable.
     *
     * @return  the value to which the key had been mapped in this hashtable,
     *          or <code>null</code> if the key did not have a mapping.
     */
    public Object remove(int key) {
        Entry tab[] = table;
        int hash = key;
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index], prev = null; e != null; prev = e, e = e.next) {
            if (e.hash == hash) {
                if (prev != null)
                    prev.next = e.next;
                else
                    tab[index] = e.next;
                count--;
                Object oldValue = e.value;
                e.value = null;
                return oldValue;
            }
        }
        return null;
    }

    /** Clears this hashtable so that it contains no keys. */
    public void clear() {
        Entry tab[] = table;
        for (int index = tab.length; --index >= 0;)
            tab[index] = null;
        count = 0;
    }
    
}