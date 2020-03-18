package net.etfbl.lab.util.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4860501623398353178L;
	private String name;
	private Map<String, Picture> pictures = new HashMap<>();

	public Map<String, Picture> getPictures() {
		return pictures;
	}

	public void setPictures(Map<String, Picture> pictures) {
		this.pictures = pictures;
	}

	public List<String> getPicturesKeys() {
		List<String> keys = new ArrayList<>();
		keys.addAll(getPictures().keySet());
		return keys;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
