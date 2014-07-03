package com.skillbox.boxes.get_classy;

import java.io.IOException;

interface StorageSystem {
	public abstract void store(String key, Object value) throws IOException;

	public abstract Object retrieve(String key) throws IOException,
			ClassNotFoundException;

	public abstract boolean exists(String key);

	public abstract void discard();
}
