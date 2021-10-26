/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2017 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.core.caches; import gplx.*;
import java.util.HashMap;
import java.util.Map;

//The Node class for doubly linked list
class Node<K, V> {

	K key;
	V value;
	long size;
	Node<K, V> next;
	Node<K, V> prev;

	public Node(Node<K, V> prev, Node<K, V> next, K key, V value, long size) {
		this.prev = prev;
		this.next = next;
		this.key = key;
		this.value = value;
		this.size = size;
	}
}

//The class for LRU Cache storage and its operations
class LRUCache<K, V> {
	private boolean started = false;

	// Variable to store the least recently used element
	private Node<K, V> lruElement;

	// Variable to store the most recently used element
	private Node<K, V> mruElement;

	private Map<K, Node<K, V>> container;
	long evicts;
	long currentSize, min, max;

	// Constructor for setting the values in instance variables
	public LRUCache(long min, long max) {
		this.currentSize = 0;
		this.min = min;
		this.max = max;
		lruElement = new Node<K, V>(null, null, null, null, 0);
		mruElement = lruElement;
		container = new HashMap<K, Node<K, V>>();
	}

	// The get method to perform the retrieve operations on data
	public V get(K key) {
		Node<K, V> tempNode = container.get(key);
		if (tempNode == null) {
			return null;
		}
		// In case the MRU leave the list as it is :
		else if (tempNode.key == mruElement.key) {
			return mruElement.value;
		}

		// Getting the Next and Previous Nodes
		Node<K, V> nextNode = tempNode.next;
		Node<K, V> prevNode = tempNode.prev;

		// If LRU is updated at the left-most
		if (tempNode.key == lruElement.key) {
			nextNode.prev = null;
			lruElement = nextNode;
		}

		// In case we are in the middle, we are required to update the items before and
		// after our item
		else if (tempNode.key != mruElement.key) {
			prevNode.next = nextNode;
			nextNode.prev = prevNode;
		}

		// And here we are finally moving our item to MRU
		tempNode.prev = mruElement;
		mruElement.next = tempNode;
		mruElement = tempNode;
		mruElement.next = null;

		return tempNode.value;
	}

	// The put method to perform the insert operations on cache

	public void put(K key, V value, long size) {
		started = true;
	// should we overwrite????
		if (container.containsKey(key)) {
			return;
		}

		// Inserting the new Node at the right-most end position of the linked-list
		Node<K, V> myNode = new Node<K, V>(mruElement, null, key, value, size);
		mruElement.next = myNode;
		container.put(key, myNode);
		mruElement = myNode;

		// Deleting the entry of position left-most of LRU cache and also updating the
		// LRU pointer
		if (currentSize >= max) {
			while (currentSize > min) {
				currentSize -= lruElement.size;
				container.remove(lruElement.key);
				lruElement = lruElement.next;
				lruElement.prev = null;
				evicts++;
			}
		}

		// Updating the size of container for the firstly added entry and updating the
		// LRU pointer
		else {
			if (currentSize == 0) {
				lruElement = myNode;
			}
			currentSize += size;
		}
	}
	public void del(K key) {
		Node<K, V> tempNode = container.get(key);
		if (tempNode == null) {
			return;
		}
		// unlink from list
		Node<K, V> tempNode_temp = tempNode.prev;
		tempNode.prev = tempNode.next;
		tempNode.next = tempNode_temp;
		currentSize -= tempNode.size;
		container.remove(key);
	}
	public boolean set_minmax(long min, long max) {
		if (!started) {
			this.min = min;
			this.max = max;
			return true;
		}
		else
			return false;
	}
}
public class xLru_cache {
	private LRUCache<String, Object> c;

	public String Key() {return key;} private final    String key;
	public long Evicts() {return c.evicts;}
	public long Cur() {return c.currentSize;}

	private long min, max;
	public xLru_cache(boolean auto_reg, String key, long min, long max) {
		this.key = key;
		this.min = min;
		this.max = max;
		reset();
	}
	private void reset() {
		this.c = new LRUCache<>(min, max);
	}
	public Object Get_or_null(Object key) {
		synchronized (c) {
			return c.get((String)key);
		}
	}
	public void Set(Object key, Object val, long size) {
		synchronized (c) {
			c.put((String)key, val, size);
		}
	}
	public void Del(Object key) {
		synchronized (c) {
			c.del((String)key);
		}
	}
	public void Min_max_(long min, long max) {
		if (c.set_minmax(min, max)) {
			this.min = min;
			this.max = max;
		}
	}
	public void Clear_all() {
		synchronized (c) {
			reset();
		}
	}
	public static final xLru_cache Instance = new xLru_cache(Bool_.Y, "page_cache", 8 * Io_mgr.Len_mb, 16 * Io_mgr.Len_mb);
}
