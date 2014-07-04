package com.skillbox.boxes.storage;

import java.io.File;
import java.io.IOException;

public class TestHelper {

  /**
   * A fill for Files.createTempDirectory in Java 7
   *
   * @param testName
   *          used as a part of directory name
   * @return File for the named temporary directory
   * @throws IOException
   */
  static public String createTempDirectory(String testName) throws IOException {

    File tempDir = File.createTempFile(testName,
        Long.toString(System.nanoTime()));
    // Delete the temporary file
    if (!(tempDir.delete())) {
      throw new IOException("Could not delete temp file: "
          + tempDir.getAbsolutePath());
    }
    // Create a temporary directory in it's place
    if (!(tempDir.mkdir())) {
      throw new IOException("Could not create temp directory: "
          + tempDir.getAbsolutePath());
    }
    return tempDir.getAbsolutePath();
  }
}
