package com.skillbox.boxes.get_classy;


public abstract class NamedStorageSystem implements StorageSystem {

	private String mName;

	public NamedStorageSystem(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

}
