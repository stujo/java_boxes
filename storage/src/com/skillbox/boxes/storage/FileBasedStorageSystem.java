package com.skillbox.boxes.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * An implementation of StorageSystem Stores serialized objects in separate
 * files underneath a named folder on the file system. The named folder is
 * located in the specified RootFolder
 *
 * @see StorageSystem
 */
class FileBasedStorageSystem extends NamedStorageSystem {

  private File mRootFolder;
  private File mStorageFolder;
  private static String FBSS_EXTENSION = ".fbss";

  public FileBasedStorageSystem(String name, String rootFolder)
      throws IOException {
    super(name);

    // Check if root folder exists
    this.mRootFolder = new File(rootFolder);

    if (!this.mRootFolder.exists()) {
      throw new FileNotFoundException(rootFolder);
    }
    if (!this.mRootFolder.isDirectory()) {
      throw new IllegalArgumentException(rootFolder + " is not a directory");
    }

    this.mStorageFolder = new File(this.mRootFolder + File.separator
        + getSafeStorageDirectoryName());

    if (!this.mStorageFolder.exists()) {
      if (!this.mRootFolder.canWrite()) {
        throw new IllegalArgumentException(rootFolder + " is not writable");
      }
      if (!this.mStorageFolder.mkdir()) {
        throw new IllegalArgumentException("Unable to create storage folder "
            + this.mStorageFolder);
      }
    }
  }

  private String getSafeStorageDirectoryName() {
    return getFileSystemSafeName(getName());
  }

  private String getFileSystemSafeName(String key) {
    try {
      return java.net.URLEncoder.encode(key, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException("Unable to encode " + key
          + " as UTF-8 string for filename");
    }
  }

  private File getFileForKey(String key) {
    return new File(this.mStorageFolder.getAbsolutePath() + File.separator
        + getFileSystemSafeName(key) + FBSS_EXTENSION);
  }

  private OutputStream getOutputStream(String key) throws FileNotFoundException {
    return new FileOutputStream(getFileForKey(key));
  }

  private InputStream getInputStream(String key) throws FileNotFoundException {
    return new FileInputStream(getFileForKey(key));
  }

  @Override
  public void storeImpl(String validKey, Object validValue) throws IOException {
    OutputStream out = getOutputStream(validKey);

    /*
     * TODO: Java 7 Use try with close-able
     */
    try {
      ObjectOutputStream oos = new ObjectOutputStream(out);
      try {
        oos.writeObject(validValue);
      } finally {
        if (null != oos) {
          oos.close();
        }
      }
    } finally {
      if (null != out) {
        out.close();
      }
    }
  }

  @Override
  public Object retrieve(String key) throws IOException, ClassNotFoundException {

    InputStream in = getInputStream(key);
    /*
     * TODO: Java 7 Use try with close-able
     */
    try {
      ObjectInputStream ois = new ObjectInputStream(in);
      try {
        return ois.readObject();
      } finally {
        if (null != ois) {
          ois.close();
        }
      }

    } finally {
      if (null != in) {
        in.close();
      }
    }
  }

  @Override
  public boolean exists(String key) {
    return getFileForKey(key).exists();
  }

  @Override
  public void discardAll() {
    File[] files = this.mStorageFolder.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(FBSS_EXTENSION);
      }
    });

    for (File fbssFile : files) {
      fbssFile.delete();
    }
  }

  @Override
  public void discard(String key) {
    getFileForKey(key).delete();
  }
}
