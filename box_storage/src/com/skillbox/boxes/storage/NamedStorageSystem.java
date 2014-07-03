package com.skillbox.boxes.storage;


public abstract class NamedStorageSystem implements StorageSystem {

	private String mName;

	public NamedStorageSystem(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

}
