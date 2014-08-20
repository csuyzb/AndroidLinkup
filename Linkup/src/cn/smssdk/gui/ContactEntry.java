package cn.smssdk.gui;

import java.util.HashMap;
import java.util.Map.Entry;

public class ContactEntry implements Entry<String, HashMap<String, Object>> {
	private String key;
	private HashMap<String, Object> value;

	public ContactEntry(String key, HashMap<String, Object> value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public HashMap<String, Object> getValue() {
		return value;
	}

	public HashMap<String, Object> setValue(HashMap<String, Object> object) {
		value = object;
		return value;
	}

	public String toString() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(key, value);
		return map.toString();
	}

}
