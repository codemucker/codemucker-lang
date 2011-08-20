package com.bertvanbrakel.lang;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V> {

	private final Map<K, V> map = new HashMap<K, V>();

	public static <K, V> MapBuilder<K, V> builder(K key, V value) {
		MapBuilder<K, V> builder = new MapBuilder<K, V>();
		builder.put(key, value);
		return builder;
	}

	public MapBuilder<K, V> put(K key, V value) {
		map.put(key, value);
		return this;
	}

	public Map<K, V> create() {
		return map;
	}
	
	public int size(){
		return map.size();
	}
}
